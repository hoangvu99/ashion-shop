package com.example.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.ProductDTO;
import com.example.model.category.Category;
import com.example.model.product.Product;
import com.example.model.product.ProductImages;
import com.example.model.user.User;
import com.example.service.CategoryService;
import com.example.service.UploadFileService;
import com.example.service.UserService;

@Controller
public class MainController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	UploadFileService uploadFileService;
	
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
	
	@RequestMapping(value = "/account")
	public String acc() {				
		return "account";
	}
	
	@RequestMapping(value = "/admin")
	public String add() {			
		
		
		return "admin";
	}
	
	@RequestMapping(value ="sign-up", method = RequestMethod.GET)
	public String signUpController(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("isRegistered", 0);
		return "signup";
	}
	
	@RequestMapping(value ="sign-up", method = RequestMethod.POST)
	public String signUpSubmit(@ModelAttribute User user, Model model) {
		userService.saveUser(user);
		try {
			userService.sendVerifyEmail(user.getEmail());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("isRegistered", 1);
		return "signup";
	}
	
	@RequestMapping("/user/confirm-email")
	public String confirmEmail(@RequestParam(name ="email") String email) {
		try {
			userService.updateUserConfirmEmail(email);
		} catch (Exception e) {
			
		}
		
		return "redirect:http://localhost:8080/login";
	}
	
	@RequestMapping("/list-product")
	public String listProductView() {
		return"list-product";
	}
	
	@RequestMapping(value = "/add-product", method = RequestMethod.GET )
	public String addProductView(Model model) {
		model.addAttribute("listCategory", categoryService.listCategories());
		model.addAttribute("product", new ProductDTO());
		return"add-product";
	}
	
	@RequestMapping(value ="/add-product", method = RequestMethod.POST, consumes = "multipart/form-data")
	@ResponseBody
	public String addPr(@ModelAttribute ProductDTO productDTO) {
		
		Category category = categoryService.findCategoryById(Integer.valueOf(productDTO.getCategory()));
		Product product = new Product();
		
		product.setCategory(category);
		product.setName(productDTO.getProductName());
		product.setPrice(Double.valueOf(productDTO.getPrice()*1000));
		product.setThumnail(productDTO.getThumnail().getOriginalFilename());
		
		Collection<ProductImages>images = new ArrayList<ProductImages>();
		
		for(MultipartFile f: productDTO.getImages()) {
			ProductImages productImages = new ProductImages();
			productImages.setUrl(f.getOriginalFilename());
			images.add(productImages);
		}
		
		product.setProductImages(images);
		
		try {
			uploadFileService.saveUploadFile(productDTO.getThumnail());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "redirect:/add-product";
	}
	
	
	
}
