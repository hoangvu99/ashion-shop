package com.example.controller;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.dao.SizeDao;
import com.example.dto.CartDTO;
import com.example.dto.CartItemDTO;
import com.example.dto.ProductDTO;
import com.example.model.category.Category;
import com.example.model.product.Product;
import com.example.model.product.ProductImages;
import com.example.model.size.ProductSize;
import com.example.model.size.Size;
import com.example.model.user.User;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.service.ProductSizeService;
import com.example.service.SizeService;
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
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductSizeService productSizeService;
	
	@Autowired
	NumberFormat numberFormat;
	
	@Autowired
	SizeService sizeService;
	
	
	@RequestMapping(value = "/")
	public String homeController(HttpSession httpSession, Model model) {
		
		
		
		List<Product>lastestProducts = productService.lastestProducts();
		
		model.addAttribute("lastestProducts", lastestProducts);
		
		
		return "index";
	}
	
	@RequestMapping(value = "/shop")
	public String shopController(HttpSession httpSession) {	
		
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
	public String checkoutDetailsController(HttpSession httpSession) {	
		
		return "checkout";
	}
	@RequestMapping(value = "/contact")
	public String contactDetailsController( HttpSession httpSession) {		
		
		return "contact";
	}
	
	@RequestMapping(value = "/product-details")
	public String productDetailsController(@RequestParam(name="id")long id, Model model, HttpSession httpSession) {			
		
		model.addAttribute("product", productService.findProductById(id));
		
		return "product-details";
	}
	
	@RequestMapping(value = "/cart")
	public String cartController(HttpSession httpSession, Model model) {	
		
		
		
		CartDTO cartDTO = (CartDTO) httpSession.getAttribute("CartDTO");
		
		
		if(cartDTO == null) {
			model.addAttribute("message","Bạn chư thêm sản phẩm nào vào giỏ hàng");
		}else {
			model.addAttribute("cartDTO", cartDTO);
		}
		
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
	
	
	@RequestMapping("/403")
	public String accessDeniedPage() {
		return"403";
	}
	
	@RequestMapping(value="/addProductToCart", method = RequestMethod.POST)
	@ResponseBody
	public int addToCart(@RequestBody String[] data, HttpSession httpSession) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName;
		if( principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		}else {
			
			long productId = Long.valueOf(data[0]);
			int sizeId = Integer.valueOf(data[1]);
			Product p = productService.findProductById(productId);
			Size size = sizeService.findSizeById(sizeId);
			
			CartDTO cartDTO = (CartDTO) httpSession.getAttribute("CartDTO");
			List<CartItemDTO>itemDTOs = new ArrayList<CartItemDTO>();
			if(cartDTO == null) {
				cartDTO = new CartDTO();
				
									
			}else {
				itemDTOs = cartDTO.getCartItemDTOs();
			}
						
			
			
			boolean checkExist = false;
			int index=0;
			for (int i = 0; i < itemDTOs.size(); i++) {
				if(itemDTOs.get(i).getProductId() == productId) {
					checkExist = true;
					index =i;
					break;
				}
			}
			
			if(checkExist == true) {
				itemDTOs.get(index).setQuantity(itemDTOs.get(index).getQuantity()+1);
				itemDTOs.get(index).convertTotal(p.getPrice(),	 numberFormat);
				
				return 1;
			}else {
				CartItemDTO cartItemDTO = new CartItemDTO();
				cartItemDTO.setImageName(p.getThumnail());
				cartItemDTO.setProductId(p.getId());
				cartItemDTO.setProductName(p.getName());
				cartItemDTO.setQuantity(Integer.valueOf(data[2]));
				cartItemDTO.setSizeId(sizeId);
				cartItemDTO.setSizeName(size.getSizeName());
				cartItemDTO.convertTotal(p.getPrice(), numberFormat);
				cartItemDTO.setPrice(p.getPrice());
				itemDTOs.add(cartItemDTO);
				cartDTO.setCounter(cartDTO.getCounter()+1);
			}
			cartDTO.setCartItemDTOs(itemDTOs);
			cartDTO.calculatorCartTotal(numberFormat);
			httpSession.setAttribute("CartDTO", cartDTO);
			
			
			
			
			
			
			
		}
		
		
		return 1;
	}
	
	
	public long convertTotalTextToNumber(String[] arr) {
		
		String totalText = "";
		for (int i = 0; i < arr.length; i++) {
			totalText+=i;
		}
		
		
		
		return Long.valueOf(totalText);
	}
	
	
	
}
