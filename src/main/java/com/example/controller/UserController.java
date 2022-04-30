package com.example.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dto.UserDTO;
import com.example.model.user.User;
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
public class UserController {
	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;
	@RequestMapping(value = "/login")
	public String loginController(@RequestParam(name="emailRegisted", defaultValue = "", required = false) String emailRegisted ,Model model) {
		
		if(emailRegisted == "") {
			model.addAttribute("emailRegisted", "");
		}else {
			System.out.println("co email");
			model.addAttribute("emailRegisted", emailRegisted);
		}
		return "login";
	}
	
	@RequestMapping(value ="sign-up", method = RequestMethod.GET)
	public String signUpController(Model model) {
		model.addAttribute("userDTO", new UserDTO());
		model.addAttribute("isRegistered", 0);
		return "signup";
	}
	
	@RequestMapping(value = "/account")
	public String acc() {				
		return "user/account";
	}
	
	@RequestMapping(value ="/sign-up", method = RequestMethod.POST)
	public String signUpSubmit(@ModelAttribute UserDTO userDTO, Model model,RedirectAttributes redirectAttributes) {
		
		User u = userService.findUserByEmail(userDTO.getEmail());
		if(u != null ) {
			redirectAttributes.addFlashAttribute("emailExistMsg","Email "+userDTO.getEmail()+" đã tồn tại.");
			
			return "redirect:/sign-up";
		}else {
			
			
			userService.saveUser(userDTO);
			
		}

		model.addAttribute("isRegistered", 1);
		return "signup";
	}
	
	@RequestMapping("/user/confirm-email")
	public String confirmEmail(@RequestParam(name ="email") String email, RedirectAttributes redirectAttributes) {
		try {
			userService.updateUserConfirmEmail(email);
		} catch (Exception e) {
			
		}
		
		return "redirect:/login?emailRegisted="+ email;
	}
}
