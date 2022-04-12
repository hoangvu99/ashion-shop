package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	@RequestMapping(value = "/")
	public String homeController() {
		
		return "index";
	}
	
	@RequestMapping(value = "/shop")
	public String shopController() {				
		return "shop";
	}
	
	@RequestMapping(value = "/blog")
	public String blogController() {				
		return "blog";
	}
	
	@RequestMapping(value = "/blog-details")
	public String blogDetailsController() {				
		return "blog-details";
	}
	@RequestMapping(value = "/checkout")
	public String checkoutDetailsController() {				
		return "checkout";
	}
	@RequestMapping(value = "/contact")
	public String contactDetailsController() {				
		return "contact";
	}
	
	@RequestMapping(value = "/product-details")
	public String productDetailsController() {				
		return "product-details";
	}
	
	@RequestMapping(value = "/cart")
	public String cartController() {				
		return "shop-cart";
	}
	
	@RequestMapping(value = "/login")
	public String loginController() {				
		return "login";
	}
	
	
}
