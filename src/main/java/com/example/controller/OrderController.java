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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dao.OrderItemDao;
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
			
			List<CartItem>removed = new ArrayList<CartItem>();
			for (int j = 0; j < cartItems.size(); j++) {
				cartItemService.deleteCartItem(cartItems.get(j).getId());
				removed.add(cartItems.get(j));
			}
			cartItems.removeAll(removed);
			
			cart.setTotal(0);
			cart.setCounter(0);
			cart.setCartItems(cartItems);
			httpSession.setAttribute("counterCart", 0);
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
	
	@RequestMapping(value="/cancel-order")
	public String de(@RequestParam(name="id")long id) {
		orderService.deleteOrder(id);
		return "redirect:/account";
	}
	
	
	@RequestMapping("/list-order")
	public String listOrder(Model model) {
		List<Orders>orders = orderService.findAllOrders();
		model.addAttribute("orders", orders);
		return "list-order";
	}
	
	
	
	@RequestMapping(value ="/view-order")
	public String viewOrder(@RequestParam(name = "id") long id, Model model) {
		Orders orders = orderService.findOrderById(id);
		model.addAttribute("order", orders);
		return "view-order";
	}
	
	@RequestMapping(value ="/acceptOrder")
	public String acceptOrder(@RequestParam(name = "id") long id, Model model) {
		try {
			orderService.acceptOrder(id);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		return "redirect:/new-orders";
	}
	
	@RequestMapping(value ="/successOrder")
	public String successOrder(@RequestParam(name = "id") long id, Model model) {
		try {
			orderService.setSuccessOrder(id);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "redirect:/accepted-orders";
	}
	
	@RequestMapping("/accepted-orders")
	public String acceptedOrders(Model model, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="s", defaultValue = "") String s) {
		
		
		
		if(s.equalsIgnoreCase("")) {
			List<Orders>orders = orderService.acceptedOrders(5,(page-1)*5);
			model.addAttribute("orders", orders);
			int lastPage = (page == 1)? 1: page-1;
			model.addAttribute("lastPage", lastPage);
			model.addAttribute("page", page);
			model.addAttribute("nextPage", page+1);
		}else {
			model.addAttribute("s", s);
			List<User> users = userService.searchUserByName(s);
			List<Orders>list = new ArrayList<Orders>();
			if(users != null) {
				for (int i = 0; i < users.size(); i++) {
					list = orderService.userAcceptedOrders(users.get(i).getId());
				}
			}
			model.addAttribute("orders",list);
		}
		return "accepted-orders";
	}
	
	@RequestMapping("/success-orders")
	public String successOrders(Model model, @RequestParam(name="page", defaultValue = "1") int page,  @RequestParam(name="s", defaultValue = "") String s) {
		
		
		if(s.equalsIgnoreCase("")) {
			List<Orders>orders = orderService.successOrders(5,(page-1)*5);
			model.addAttribute("orders", orders);
			int lastPage = (page == 1)? 1: page-1;
			model.addAttribute("lastPage", lastPage);
			model.addAttribute("page", page);
			model.addAttribute("nextPage", page+1);
		}else {
			model.addAttribute("s", s);
			List<User> users = userService.searchUserByName(s);
			List<Orders>list = new ArrayList<Orders>();
			if(users != null) {
				for (int i = 0; i < users.size(); i++) {
					list = orderService.userSuccessOrders(users.get(i).getId());
				}
			}
			model.addAttribute("orders",list);
		}
		return "success-orders";
	}
	
	@Autowired
	OrderItemDao orderItemDao;
	@RequestMapping("/delete-orderItem")
	public String deleteOrderItem(@RequestParam(name="id"  )long id, @RequestParam(name="orderId" ) long orderId) {
		orderItemDao.deleteOrderItem(id);
		Orders order = orderService.findOrderById(orderId);
		order.calTotal();
		orderService.saveOrder(order);
		return "redirect:/view-order?id="+orderId;
	}
	
	@RequestMapping("/delete-order")
	public String deleteOrder( @RequestParam(name="id" ) long orderId) {
		orderService.deleteOrder(orderId);
		
		return "redirect:/new-orders";
	}
	
	
	
	@RequestMapping(value="/updateOrder", method = RequestMethod.POST)
	@ResponseBody
	public String updateCart(@RequestBody String[] data, HttpSession httpSession, Model model) {
		String date = dateFormat.format(new Date());
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Orders orders = orderService.findOrderById(Long.valueOf(data[0]));
		
		List<OrderItems>orderItems = orders.getOrderItems();
		for (int i = 1; i < data.length; i++) {
				int quantity = Integer.valueOf(data[i]);
				if(quantity != orderItems.get(i-1).getQuantity()) {
					orderItems.get(i-1).setQuantity(quantity);
					orderItems.get(i-1).calSubTotal();
					orderItems.get(i-1).setUpdatedAt(date);
				}
			}
			orders.setOrderItems(orderItems);
			orders.calTotal();
			orderService.saveOrder(orders);
			
		
		
		return data[0];
	}
	
	@RequestMapping("/view-order-details")
	public String viewOrderDeetails(@RequestParam(name="orderId")long id, Model model) {
		
		Orders orders = orderService.findOrderById(id);
	
		model.addAttribute("order", orders);
		return "view-order-detail";
	}
	
}
