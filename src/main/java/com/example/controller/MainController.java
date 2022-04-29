package com.example.controller;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
import com.example.model.cart.Cart;
import com.example.model.cart.CartItem;
import com.example.model.category.Category;
import com.example.model.order.Order;
import com.example.model.order.OrderItem;
import com.example.model.order.OrderItems;
import com.example.model.order.Orders;
import com.example.model.product.Product;
import com.example.model.product.ProductImages;
import com.example.model.size.ProductSize;
import com.example.model.size.Size;
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
	
	@Autowired
	CartService cartService;
	
	@Autowired
	CartItemService cartItemService;
	
	@Autowired
	SimpleDateFormat dateFormat;
	
	@Autowired
	OrderService orderService;
	
	@RequestMapping(value = "/")
	public String homeController(HttpSession httpSession, Model model) {
		String formatDate = dateFormat.format(new Date());
		CartDTO cartDTO = (CartDTO) httpSession.getAttribute("CartDTO");
		
		
		if(checkIsAuthenticated()) {			
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = userService.findUserByEmail(((UserDetails) principal).getUsername());
			Cart cart = cartService.findUserCart(user.getId());
			if( cart == null) {
				cart = new Cart();
				cart.setUser(user);
				cart.setCreatedAt(formatDate);																
			}
			List<CartItem>cartItems = (List<CartItem>) cart.getCartItems();
			if(cartItems == null) {
				cartItems = new ArrayList<CartItem>();
			}						
			if(cartDTO != null) {
				
				List<CartItemDTO>itemDTOs = cartDTO.getCartItemDTOs();
				for (int i = 0; i < itemDTOs.size(); i++) {
					Product p = productService.findProductById(itemDTOs.get(i).getProductId());
					Size s = sizeService.findSizeById(itemDTOs.get(i).getSizeId());
					ProductSize pz = productSizeService.findProductSizeByProductId(p.getId(), s.getId());					
					boolean checkExist = false;
					int index=0;
					for (int j = 0; j < cartItems.size(); j++) {
						if(p.getId() == cartItems.get(j).getProduct().getId() && s.getId() == cartItems.get(j).getProductSize().getSize().getId()) {
							checkExist = true;
							index=j;
							break;
						}
					}
					
					if(checkExist) {
						cartItems.get(index).setQuantity(cartItems.get(index).getQuantity() +itemDTOs.get(i).getQuantity());
						cartItems.get(index).convertTotal(numberFormat);
						cartItems.get(index).setUpdatedAt(formatDate);
						
					}else {
						CartItem item = convertCartItemDtoToCartItem(itemDTOs.get(i), cart, p, pz,formatDate);												
						cartItems.add(item);
						cart.setCounter(cartDTO.getCounter()+1);
					}															
				}
				cart.setCartItems(cartItems);										
				cart.calculatorCartTotal(numberFormat);
			}			
			cartService.saveCart(cart);
		}		
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
	public String checkoutDetailsController(HttpSession httpSession, Model model) {	
		
		
		String date = dateFormat.format(new Date());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.findUserByEmail(((UserDetails) principal).getUsername());
		Cart cart = cartService.findUserCart(user.getId());
		List<CartItem> cartItems =  (List<CartItem>) cart.getCartItems();
		
		
		
		
		model.addAttribute("cart", cart);
		model.addAttribute("user", user);
		
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
		
		if (checkIsAuthenticated()) {	
			cartDTO = new CartDTO();
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = userService.findUserByEmail(((UserDetails) principal).getUsername());
			Cart cart = cartService.findUserCart(user.getId());
			cartDTO.setCounter(cart.getCounter());
			cartDTO.setCartTotal(cart.getTotal());
			List<CartItemDTO>itemDTOs = new ArrayList<CartItemDTO>();
			List<CartItem>items = (List<CartItem>) cart.getCartItems();
			
			for (int i = 0; i < items.size(); i++) {
				CartItemDTO cartItemDTO = new CartItemDTO();
				cartItemDTO.setImageName(items.get(i).getProduct().getThumnail());
				cartItemDTO.setPrice(items.get(i).getPrice());
				cartItemDTO.setProductId(items.get(i).getProduct().getId());
				cartItemDTO.setProductName(items.get(i).getProduct().getName());
				cartItemDTO.setQuantity(items.get(i).getQuantity());
				cartItemDTO.setSizeId(items.get(i).getProductSize().getSize().getId());
				cartItemDTO.setSizeName(items.get(i).getProductSize().getSize().getSizeName());
				cartItemDTO.setTotal(items.get(i).getSubTotal());
				itemDTOs.add(cartItemDTO);
				
				
			}
			
			cartDTO.setCartItemDTOs(itemDTOs);
			model.addAttribute("cartDTO", cartDTO);
			
		}else {
			if(cartDTO == null) {
				model.addAttribute("message", "Bạn chưa chọn sản phẩm nào");
			}else{
				model.addAttribute("cartDTO", cartDTO);
			}
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
		String formatDate = dateFormat.format(new Date());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long productId = Long.valueOf(data[0]);
		int sizeId = Integer.valueOf(data[1]);
		int quantity = Integer.valueOf(data[2]);
		Product p = productService.findProductById(productId);
		Size size = sizeService.findSizeById(sizeId);
		ProductSize pz = productSizeService.findProductSizeByProductId(p.getId(), size.getId());
		if( principal instanceof UserDetails) {
			User user = userService.findUserByEmail(((UserDetails) principal).getUsername());
			Cart cart = cartService.findUserCart(user.getId());
			List<CartItem>cartItems = (List<CartItem>) cart.getCartItems();
			boolean checkExist = false;
			int index = 0;
			for (int i = 0; i < cartItems.size(); i++) {
				if(cartItems.get(i).getProduct().getId() == p.getId() &&
				   cartItems.get(i).getProductSize().getSize().getId() == size.getId()) {
					checkExist = true;
					index = i;
					break;
				}
			}
			if(checkExist) {
				cartItems.get(index).setQuantity(cartItems.get(index).getQuantity()+ quantity);
				cartItems.get(index).convertTotal(numberFormat);
				cartItems.get(index).setUpdatedAt(formatDate);
			}else {
				CartItem cartItem = new CartItem();
				cartItem.setCart(cart);
				cartItem.setCreatedAt(formatDate);
				cartItem.setPrice(p.getPrice());
				cartItem.setProduct(p);
				cartItem.setProductSize(pz);
				cartItem.setQuantity(quantity);
				cartItem.convertTotal(numberFormat);
				cartItems.add(cartItem);
			}
			
			cart.setCartItems(cartItems);
			cartService.saveCart(cart);
			
			
		}else {								
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
				if(itemDTOs.get(i).getProductId() == productId && itemDTOs.get(i).getSizeId() == sizeId) {
					checkExist = true;
					index =i;
					break;
				}
			}			
			if(checkExist == true) {
				itemDTOs.get(index).setQuantity(itemDTOs.get(index).getQuantity()+quantity);
				itemDTOs.get(index).convertTotal(numberFormat);								
			}else {
				CartItemDTO cartItemDTO = createCartItemDTO(p, size, quantity, numberFormat);									
				itemDTOs.add(cartItemDTO);
				cartDTO.setCounter(cartDTO.getCounter()+1);
			}
			cartDTO.setCartItemDTOs(itemDTOs);
			cartDTO.calculatorCartTotal(numberFormat);
			httpSession.setAttribute("CartDTO", cartDTO);		
		}				
		return 1;
	}
	
	@RequestMapping(value="/deleteProductFromCart")
	public String deleteProductFromCart(@RequestParam(name = "productId")long productId, @RequestParam(name="sizeId" )int sizeId, HttpSession httpSession) {
		
		if(checkIsAuthenticated()) {
			try {
				long productSizeId = productSizeService.findProductSizeByProductId(productId, sizeId).getId();
				Cart cart = cartService.findUserCart(productSizeId);
				CartItem cartItem = cartItemService.findCartItem(productId, productSizeId);
				
				cartItemService.deleteCartItem(cartItem.getId());
				cart.calculatorCartTotal(numberFormat);
				cartService.saveCart(cart);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else {
			CartDTO cartDTO = (CartDTO) httpSession.getAttribute("CartDTO");
			List<CartItemDTO>itemDTOs = cartDTO.getCartItemDTOs();
			CartItemDTO cartItemDTO = itemDTOs.stream().filter(i -> i.getProductId() == productId && i.getSizeId() == sizeId).collect(Collectors.toList()).get(0);
			itemDTOs.remove(cartItemDTO);
			cartDTO.setCounter(cartDTO.getCounter()-1);
			cartDTO.calculatorCartTotal(numberFormat);
			
			httpSession.setAttribute("CartDTO", cartDTO);
		}
		
		return "redirect:/cart";
	}
	
	@RequestMapping(value="/updateCart", method = RequestMethod.POST)
	@ResponseBody
	public String updateCart(@RequestBody String[] data, HttpSession httpSession, Model model) {
		if(checkIsAuthenticated()) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = userService.findUserByEmail(((UserDetails) principal).getUsername());
			Cart cart = cartService.findUserCart(user.getId());
			List<CartItem>cartItems = (List<CartItem>) cart.getCartItems();
			for (int i = 0; i < data.length; i++) {
				int quantity = Integer.valueOf(data[i]);
				if(quantity != cartItems.get(i).getQuantity()) {
					cartItems.get(i).setQuantity(quantity);
					cartItems.get(i).convertTotal(numberFormat);
				}
			}
			cart.calculatorCartTotal(numberFormat);
			cartService.saveCart(cart);
		}else {
			CartDTO cartDTO = (CartDTO) httpSession.getAttribute("CartDTO");
			List<CartItemDTO>itemDTOs = cartDTO.getCartItemDTOs();
			for (int i = 0; i < data.length; i++) {
				int quantity = Integer.valueOf(data[i]);
				if(quantity != itemDTOs.get(i).getQuantity()) {
					itemDTOs.get(i).setQuantity(quantity);
					itemDTOs.get(i).convertTotal(numberFormat);
				}
			}
			cartDTO.calculatorCartTotal(numberFormat);
			httpSession.setAttribute("CartDTO", cartDTO);
		}
		
		return "success";
	}
	
	public long convertTotalTextToNumber(String[] arr) {
		
		String totalText = "";
		for (int i = 0; i < arr.length; i++) {
			totalText+=i;
		}
		
		
		
		return Long.valueOf(totalText);
	}
	
	public boolean checkIsAuthenticated() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			return true;
		}
		
		return false;
	}
	
	public CartItem convertCartItemDtoToCartItem(CartItemDTO cartItemDTO, Cart cart, Product product, ProductSize productSize, String formatDate) {
		
		CartItem item = new CartItem();
		item.setCart(cart);
		item.setQuantity(cartItemDTO.getQuantity());
		item.setPrice(cartItemDTO.getPrice());
		item.setSubTotal(cartItemDTO.getTotal());
		item.setProduct(product);
		item.setProductSize(productSize);
		item.setCreatedAt(formatDate);
		return item;
	}
	
	public CartItemDTO createCartItemDTO(Product p, Size s, int quantity, NumberFormat numberFormat) {
		CartItemDTO cartItemDTO = new CartItemDTO();
		cartItemDTO.setImageName(p.getThumnail());
		cartItemDTO.setPrice(p.getPrice());
		cartItemDTO.setProductId(p.getId());
		cartItemDTO.setProductName(p.getName());
		cartItemDTO.setQuantity(quantity);
		cartItemDTO.setSizeId(s.getId());
		cartItemDTO.setSizeName(s.getSizeName());
		cartItemDTO.convertTotal(numberFormat);
		return cartItemDTO;
		
	}
	
	public List checkExist(Product p, Size s, List<CartItem>cartItems) {
		boolean checkExist = false;
		int index=0;
		for (int j = 0; j < cartItems.size(); j++) {
			if(p.getId() == cartItems.get(j).getProduct().getId() && s.getId() == cartItems.get(j).getProductSize().getSize().getId()) {
				checkExist = true;
				index=j;
				break;
			}
		}
		List l = new ArrayList();
		l.add(checkExist);
		l.add(index);
		return l;
	}
	
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
		List<UserAddress>addresses = (List<UserAddress>) user.getUserAddresses();
		order.setDeliveryAddress(addresses.get(0).getAddressName());
		try {
			orderService.saveOrder(order);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "Đặt hàng thành công";
	}
	
}
