package com.example.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.annotations.Parameter;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dao.OrderItemDao;
import com.example.dao.UserAddressDao;
import com.example.dto.CartDTO;
import com.example.dto.CartItemDTO;
import com.example.dto.ProductDTO;
import com.example.dto.SizeDTO;
import com.example.model.cart.Cart;
import com.example.model.cart.CartItem;
import com.example.model.order.OrderItems;
import com.example.model.order.Orders;
import com.example.model.product.Product;
import com.example.model.size.Size;
import com.example.model.user.User;
import com.example.model.user.UserAddress;
import com.example.service.CategoryService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.ProductSizeService;
import com.example.service.SizeService;
import com.example.service.UploadFileService;
import com.example.service.UserAddressService;
import com.example.service.UserService;

@Controller
public class AdminController {
	
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
	UserAddressService addressService;
	
	@Autowired
	OrderService orderService;
	
	
	
	@RequestMapping(value = "/admin")
	public String add( Model model) {			
		
		model.addAttribute("newOrderCount", orderService.countNewOrders());
		model.addAttribute("acceptedOrderCount", orderService.countAcceptedOrders());
		return "admin";
	}
	
	
	
	
	
	
	
	@RequestMapping("/delete-product")
	public String deleteProduct(@RequestParam(name="id")long id) {
		productService.deleteProduct(id);
		return "redirect:/list-product";
	}
	
	
	@RequestMapping("/new-orders")
	public String newOrders(Model model,@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="s", defaultValue = "") String s) {
		
		if(s.equalsIgnoreCase("")) {
			List<Orders>orders = orderService.newOrders(5,(page-1)*5);
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
					list = orderService.userNewOrders(users.get(i).getId());
				}
			}
			model.addAttribute("orders",list);
		}
		return "list-order";
	}
	
	
	
	
	
	
	
}
