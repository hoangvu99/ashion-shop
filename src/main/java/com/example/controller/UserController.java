package com.example.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dto.UserDTO;
import com.example.dto.UserEditDTO;
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
public class UserController {
	@Autowired
	UserService userService;
	
	@Autowired 
	UserAddressService addressService;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	UploadFileService uploader;
	
	@Autowired
	SimpleDateFormat dateFormat;
	
	
	@RequestMapping(value = "/login")
	public String loginController(@RequestParam(name="emailRegisted", defaultValue = "", required = false) String emailRegisted ,Model model) {
		
		if(emailRegisted == "") {
			model.addAttribute("emailRegisted", "");
		}else {
			
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
	public String acc(Model model) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String email ="";
		if(principal instanceof UserDetails) {
				email =	((UserDetails) principal).getUsername();
		}
		
		User user = userService.findUserByEmail(email);
		UserAddress address = addressService.findUserAddressByUserId(user.getId());
		List<Orders> userOrders = orderService.listUserOrder(user.getId());
		
		model.addAttribute("user", user);
		model.addAttribute("orders", userOrders);
		model.addAttribute("userAddress", address);
		return "user/account";
	}
	
	@RequestMapping(value ="/edit")
	public String editAccountView(Model model) {
			
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			String email ="";
			if(principal instanceof UserDetails) {
							email =	((UserDetails) principal).getUsername();
			}
			User user = userService.findUserByEmail(email);
			UserEditDTO editDTO = new UserEditDTO();
			editDTO.setUserName(user.getUserName());
			editDTO.setPhone(user.getPhone());
			editDTO.setEmail(user.getEmail());
			
			UserAddress address = addressService.findUserAddressByUserId(user.getId());
			if(address != null) {
				editDTO.setProvince(address.getProvince());
				editDTO.setDistrict(address.getDistrict());
				editDTO.setCommune(address.getCommune());
				editDTO.setAddressDetail(address.getAddressDetail());
			}
			
			
			model.addAttribute("user", editDTO);
			model.addAttribute("avatar", user.getAvatarURL());
			return "user/edit-account";
		}
	@RequestMapping(value ="/reset-avatar", method = RequestMethod.GET)
	public String changeAvatar(Model model) {
			
			
		
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			String email ="";
			if(principal instanceof UserDetails) {
							email =	((UserDetails) principal).getUsername();
			}
			User user = userService.findUserByEmail(email);
			
			model.addAttribute("userName", user.getUserName());
			model.addAttribute("avatar", user.getAvatarURL());
			return "user/reset-avatar";
		}
	
	@RequestMapping(value ="/reset-avatar", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String changeAvatarSubmit(RedirectAttributes model, @RequestParam(name = "avatar") MultipartFile file) {
			
			
			
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			String email ="";
			if(principal instanceof UserDetails) {
				email =	((UserDetails) principal).getUsername();
			}
			User user = userService.findUserByEmail(email);
			userService.saveUserAvatar(file, user.getId());
			
			UserAddress address = addressService.findUserAddressByUserId(user.getId());
			user.setAvatarURL(file.getOriginalFilename());
			userService.updateUserInfo(user);
			
			
			return "redirect:/account";
		}
	
	@RequestMapping(value ="/edit", method = RequestMethod.POST)
	public String editAccountSubmit(@ModelAttribute UserEditDTO u, Model model,RedirectAttributes redirectAttributes) {
			
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			String email ="";
			if(principal instanceof UserDetails) {
				email =	((UserDetails) principal).getUsername();
			}
			User user = userService.findUserByEmail(email);
			UserAddress address = addressService.findUserAddressByUserId(user.getId());
			
			user.setUserName(u.getUserName());						
			user.setPhone(u.getPhone());
			if(address == null ) {
				address = new UserAddress();
			}
			address.setDistrict(u.getDistrict());
			address.setCommune(u.getCommune());
			address.setProvince(u.getProvince());
			address.setAddressDetail(u.getAddressDetail());
			
			address.setUser(user);
			
			
			
			userService.updateUserInfo(user);
			addressService.saveAddress(address);
			
			
			redirectAttributes.addFlashAttribute("user", user);
			return "redirect:/account";
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
	
	@RequestMapping(value = "/forget-password")
	public String forgetPass() {
		return "user/forget-pass";
	}
	
	@RequestMapping(value = "/forget-password", method = RequestMethod.POST)
	public String forgetPass(@RequestParam(name = "email") String email, Model model) {
		
		User user = userService.findUserByEmail(email);
		if(user == null) {
			model.addAttribute("emailNotExistMsg", "Email "+email+" không tồn tại!");
			
		}else {
			
			model.addAttribute("resetMsg", "Chúng tôi đã gởi đường dẫn thiết lập lại mật khẩu cho bạn. Xin vui lòng kiểm tra hòm thư");
			try {
				userService.sendResetPassToMail(email);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return "user/forget-pass";
	}
	
	@RequestMapping(value ="/reset-password")
	public String resetPass(@RequestParam(name ="email") String email, Model model) {
		
		model.addAttribute("email", email);
		return "user/reset-password";
	}
	
	@RequestMapping(value ="/reset-new-password", method = RequestMethod.POST)
	public String resetPassSubmit(@RequestParam(name ="email") String email, @RequestParam(name = "pass") String pass,Model model) {
		
		userService.resetPass(email, pass);
		model.addAttribute("msg", "Thay đổi password thành công");
		return "user/reset-password";
	}
	
	@RequestMapping(value="/order-details")
	public String orderDetail(@RequestParam(name = "orderId") long orderId, Model model) {
		
		Orders orders = orderService.findOrderById(orderId);
		
		model.addAttribute("order", orders);
		
		return "user/order-detail";
	}
	
	@RequestMapping("/view-user")
	public String viewUser(@RequestParam(name="id")long id, Model model) {
		
		User user = userService.findUserByid(id);
		UserAddress address = addressService.findUserAddressByUserId(user.getId());
		List<Orders>orders = (List<Orders>) user.getOrders();
		model.addAttribute("user", user);
		model.addAttribute("address", address);
		model.addAttribute("orders", orders);
		return "view-user";
	}
	
	
	
	@RequestMapping("/delete-user")
	public String deleteUser(@RequestParam(name="id")long id) {
		User u = userService.findUserByid(id);
		u.setDeletedAt(dateFormat.format(new Date()));
		u.setIsDeleted(1);
		userService.updateUserInfo(u);
		return "redirect:/list-users";
	}
	@RequestMapping("/list-users")
	public String listUser(Model model, @RequestParam(name = "page", defaultValue = "1") int page,@RequestParam(name="s", defaultValue = "")String s) {
		
		if(s.equalsIgnoreCase("")) {
			List<User> users = userService.getListUser(5, (page-1)*5);
			int lastPage = (page == 1)? 1: page-1;
			model.addAttribute("lastPage", lastPage);
			model.addAttribute("page", page);
			model.addAttribute("nextPage", page+1);
			model.addAttribute("users", users);
		}else {
			model.addAttribute("s", s);
			model.addAttribute("users", userService.searchUserByName(s));
		}
		
		
		return "list-users";
	}
	
	
}
