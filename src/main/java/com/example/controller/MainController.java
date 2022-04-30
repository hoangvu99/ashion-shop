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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/403")
	public String accessDeniedPage() {
		return"403";
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
	
	
	
}
