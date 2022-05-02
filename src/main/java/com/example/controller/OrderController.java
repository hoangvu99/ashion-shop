package com.example.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dto.CartDTO;
import com.example.dto.CartItemDTO;
import com.example.dto.CheckOutDTO;
import com.example.model.cart.Cart;
import com.example.model.cart.CartItem;
import com.example.model.order.OrderItems;
import com.example.model.order.Orders;
import com.example.model.user.User;
import com.example.model.user.UserAddress;
import com.example.service.CartItemService;
import com.example.service.CartService;
import com.example.service.CategoryService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.ProductSizeService;
import com.example.service.SizeService;
import com.example.service.UploadFileService;
import com.example.service.UserAddressService;
import com.example.service.UserService;

@Controller
public class OrderController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	UploadFileService uploadFileService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductSizeService productSizeService;
	
	@Autowired
	NumberFormat numberFormat;
	
	@Autowired
	SizeService sizeService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	CartItemService cartItemService;
	
	@Autowired
	SimpleDateFormat dateFormat;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	UserAddressService addressService;
	
	@RequestMapping(value="/place-order", method = RequestMethod.POST)
	public String placeOrder(@ModelAttribute CheckOutDTO checkOutDTO, HttpSession httpSession) {
		String date = dateFormat.format(new Date());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.findUserByEmail(((UserDetails) principal).getUsername());
		Cart cart = cartService.findUserCart(user.getId());
		List<CartItem> cartItems =  (List<CartItem>) cart.getCartItems();
		UserAddress address = addressService.findUserAddressByUserId(user.getId());
		Orders order = new Orders();
		order.setUser(user);
		List<OrderItems>listItems = new ArrayList<OrderItems>();
		cartItems.stream().forEach(i -> {
			OrderItems orderItem = new OrderItems();
			orderItem.setOrders(order);
			orderItem.setPrice(i.getPrice());
			orderItem.setCreatedAt(date);
			orderItem.setProduct(i.getProduct());
			orderItem.setProductSize(i.getProductSize());
			orderItem.setQuantity(i.getQuantity());
			orderItem.setTotal(i.getSubTotal());
			listItems.add(orderItem);
		});
		order.setOrderItems(listItems);
		order.setCreatedAt(date);
		order.setStatus(0);
		if(checkOutDTO.getNote() != "") {
			order.setNote(checkOutDTO.getNote());
		}else {
			order.setNote("");
		}
		if(checkOutDTO.getOtherAddress() !="") {
			order.setDeliveryAddress(checkOutDTO.getOtherAddress());
		}else {
			order.setDeliveryAddress(address.getAddressDetail()+" - " + address.getCommune() + " - " + address.getDistrict() + " - " + address.getProvince());
		}
		
		if(checkOutDTO.getOtherPhone() != 0) {
			order.setPhoneNumber(checkOutDTO.getOtherPhone());
		}else {
			order.setPhoneNumber(user.getPhone());
		}
		order.setOrderTotal(cart.getTotal());
		order.setIsPaid(0);
		order.setPaymentMethod(0);
		
		
		try {
			orderService.saveOrder(order);
			
			CartDTO cartDTO = (CartDTO) httpSession.getAttribute("CartDTO");
			if(cartDTO != null) {
				List<CartItemDTO>cartItemDTOs = new ArrayList<CartItemDTO>();
				cartDTO.setCartItemDTOs(cartItemDTOs);
				cartDTO.setCartTotal(0);
				cartDTO.setCounter(0);
				httpSession.setAttribute("CartDTO", cartDTO);
			}
			
			cartItems.stream().forEach(i -> {
				cartItemService.deleteCartItem(i.getId());
			});
			cartItems =new ArrayList<CartItem>();
			cart.setTotal(0);
			cart.setCounter(0);
			cart.setCartItems(cartItems);
			cartService.saveCart(cart);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "place-order-success";
	}
	@RequestMapping(value = "/checkout")
	public String checkoutDetailsController(HttpSession httpSession, Model model) {	
		
		
		String date = dateFormat.format(new Date());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.findUserByEmail(((UserDetails) principal).getUsername());
		Cart cart = cartService.findUserCart(user.getId());
		List<CartItem> cartItems =  (List<CartItem>) cart.getCartItems();
		UserAddress address = addressService.findUserAddressByUserId(user.getId());
		
		
		CheckOutDTO checkOutDTO = new CheckOutDTO();
		model.addAttribute("cart", cart);
		model.addAttribute("user", user);
		model.addAttribute("address", address);
		model.addAttribute("checkOut", checkOutDTO);
		
		return "checkout";
	}
}
