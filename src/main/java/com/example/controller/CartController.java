		package com.example.controller;
		
		import java.text.NumberFormat;
		import java.text.SimpleDateFormat;
		import java.util.ArrayList;
		import java.util.Date;
		import java.util.List;
		import java.util.stream.Collectors;
		
		import javax.servlet.http.HttpSession;
		
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.security.core.context.SecurityContextHolder;
		import org.springframework.security.core.userdetails.UserDetails;
		import org.springframework.stereotype.Controller;
		import org.springframework.ui.Model;
		import org.springframework.web.bind.annotation.RequestBody;
		import org.springframework.web.bind.annotation.RequestMapping;
		import org.springframework.web.bind.annotation.RequestMethod;
		import org.springframework.web.bind.annotation.RequestParam;
		import org.springframework.web.bind.annotation.ResponseBody;
		
		import com.example.dto.CartDTO;
		import com.example.dto.CartItemDTO;
		import com.example.model.cart.Cart;
		import com.example.model.cart.CartItem;
		import com.example.model.product.Product;
		import com.example.model.size.ProductSize;
		import com.example.model.size.Size;
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
		public class CartController {
			
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
			
		public boolean checkIsAuthenticated() {
				
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if(principal instanceof UserDetails) {
					return true;
				}
				
				return false;
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
						index = i;;
						break;
					}
				}
				if(checkExist) {
					cartItems.get(index).setQuantity(cartItems.get(index).getQuantity()+ quantity);
					cartItems.get(index).calSubTotal();
					cartItems.get(index).setUpdatedAt(formatDate);
				}else {
					CartItem cartItem = new CartItem();
					cartItem.setCart(cart);
					cartItem.setCreatedAt(formatDate);
					cartItem.setPrice(p.getPriceInNum());
					cartItem.setProduct(p);
					cartItem.setProductSize(pz);
					cartItem.setQuantity(quantity);
					cartItem.calSubTotal();
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
					itemDTOs.get(index).calSubTotal();				
				}else {
					CartItemDTO cartItemDTO = createCartItemDTO(p, size, quantity, numberFormat);									
					itemDTOs.add(cartItemDTO);
					cartDTO.setCounter(cartDTO.getCounter()+1);
				}
				cartDTO.setCartItemDTOs(itemDTOs);
				cartDTO.calTotal();
				httpSession.setAttribute("CartDTO", cartDTO);		
			}				
			return 1;
		}
			public CartItemDTO createCartItemDTO(Product p, Size s, int quantity, NumberFormat numberFormat) {
					CartItemDTO cartItemDTO = new CartItemDTO();
					cartItemDTO.setImageName(p.getThumnail());
					cartItemDTO.setPrice(p.getPriceInNum());
					cartItemDTO.setProductId(p.getId());
					cartItemDTO.setProductName(p.getName());
					cartItemDTO.setQuantity(quantity);
					cartItemDTO.setSizeId(s.getId());
					cartItemDTO.setSizeName(s.getSizeName());
					cartItemDTO.calSubTotal();
					return cartItemDTO;
					
				}
		
		@RequestMapping(value="/deleteProductFromCart")
		public String deleteProductFromCart(@RequestParam(name = "productId")long productId, @RequestParam(name="sizeId" )int sizeId, HttpSession httpSession) {
			
			if(checkIsAuthenticated()) {
				try {
					long productSizeId = productSizeService.findProductSizeByProductId(productId, sizeId).getId();
					Cart cart = cartService.findUserCart(productSizeId);
					CartItem cartItem = cartItemService.findCartItem(productId, productSizeId);
					
					cartItemService.deleteCartItem(cartItem.getId());
					cart.calTotal();
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
				cartDTO.calTotal();
				
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
						cartItems.get(i).calSubTotal();
					}
				}
				cart.calTotal();
				cartService.saveCart(cart);
			}else {
				CartDTO cartDTO = (CartDTO) httpSession.getAttribute("CartDTO");
				List<CartItemDTO>itemDTOs = cartDTO.getCartItemDTOs();
				for (int i = 0; i < data.length; i++) {
					int quantity = Integer.valueOf(data[i]);
					if(quantity != itemDTOs.get(i).getQuantity()) {
						itemDTOs.get(i).setQuantity(quantity);
						itemDTOs.get(i).calSubTotal();
					}
				}
				cartDTO.calTotal();
				httpSession.setAttribute("CartDTO", cartDTO);
			}
			
			return "success";
		}
		}
