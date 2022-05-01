package com.example.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@RequestMapping(value="/place-order")
	@ResponseBody
	public String placeOrder() {
		String date = dateFormat.format(new Date());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.findUserByEmail(((UserDetails) principal).getUsername());
		Cart cart = cartService.findUserCart(user.getId());
		List<CartItem> cartItems =  (List<CartItem>) cart.getCartItems();
		
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
		order.setNote("Ghi chú");
		order.setOrderTotal(cart.getTotal());
		order.setPhoneNumber(user.getPhone());
		
		
		try {
			orderService.saveOrder(order);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "Đặt hàng thành công";
	}
}
