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
	
	@RequestMapping("/list-product")
	public String listProductView(Model model, @RequestParam(name = "s", defaultValue = "") String s, @RequestParam(name="page", defaultValue = "1") int page) {
			
		
			if(s.equalsIgnoreCase("")== true) {
				int lastPage = (page == 1)? 1: page-1;
			
				model.addAttribute("lastPage", lastPage);
				model.addAttribute("page", page);
				model.addAttribute("nextPage", page+1);
				model.addAttribute("listProduct", productService.getPageProduct(5,page));
				
			}else {
				model.addAttribute("s", s);
				model.addAttribute("listProduct", productService.searchProductByName(s));
			}
			
			return"list-product";
	}
	
	@RequestMapping(value = "/admin")
	public String add( Model model) {			
		
		model.addAttribute("newOrderCount", orderService.countNewOrders());
		model.addAttribute("acceptedOrderCount", orderService.countAcceptedOrders());
		return "admin";
	}
	
	@RequestMapping(value = "/add-product", method = RequestMethod.GET )
	public String addProductView(Model model) {
		model.addAttribute("listCategory", categoryService.listCategories());
		List<SizeDTO>sizeDTOs = new ArrayList<SizeDTO>();
		List<String>sizes = sizeService.listSizeName();
		
		sizes.stream().forEach(i -> {
			
			SizeDTO sizeDTO = new SizeDTO(i, 0);
			sizeDTOs.add(sizeDTO);
		});
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setSizes(sizeDTOs);
		model.addAttribute("product", productDTO);
		return"add-product";
	}
	
	@RequestMapping(value ="/add-product", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String addPr(@ModelAttribute ProductDTO productDTO, RedirectAttributes redirectAttributes) {
		
		
		
		
		
		
		productService.saveProduct(productDTO);
		
		redirectAttributes.addFlashAttribute("message", "Thêm sản phẩm thành công");
		return "redirect:/add-product";
	}
	
	
	@RequestMapping("/view-product")
	public String productDetailView(@RequestParam(name = "id", required = true) long id, Model model) {
		
		Product p = productService.findProductById(id);
		ProductDTO productDTO = new ProductDTO();
		productDTO.setCategory(p.getCategory().getCategoryName());
		productDTO.setProductName(p.getName());
		productDTO.setDescription(p.getDescription());
		productDTO.setDetail(p.getDetailsContent());	
		
		
		List<SizeDTO> sizeDTOs = new ArrayList<SizeDTO>();
		p.getProductSizes().stream().forEach(s -> {
			SizeDTO sizeDTO = new SizeDTO(s.getSize().getSizeName(), s.getQuantity());
			sizeDTOs.add(sizeDTO);
		});
		
		
		
		
		productDTO.setPrice(p.getPriceInNum()/1000);
		productDTO.setSizes(sizeDTOs);
		
		model.addAttribute("product", p);
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("id", id);
		model.addAttribute("listCategory", categoryService.listCategories());
		return "view-product";
	
	}
	
	@RequestMapping(value="/update-product", method = RequestMethod.POST , consumes = "multipart/form-data")
	public ModelAndView upload(@RequestParam(name="id") long id, @ModelAttribute ProductDTO productDTO, RedirectAttributes redirectAttributes) {
		
		 productService.updateProduct(productDTO, id);
		
		 ModelAndView modelAndView = new ModelAndView("redirect:/view-product?id="+id);
		redirectAttributes.addFlashAttribute("messageUpdate", "Cập nhập thành công");
		return modelAndView;
	}
	
	@RequestMapping("/findProductById")
	@ResponseBody
	public String findById(@RequestParam(name ="id")long id) {
		Product p = productService.findProductById(id);
		return p.getCategory().getCategoryName();
	}
	@Autowired
	OrderService orderService;
	@RequestMapping("/list-order")
	public String listOrder(Model model) {
		List<Orders>orders = orderService.findAllOrders();
		model.addAttribute("orders", orders);
		return "list-order";
	}
	
	
	
	@RequestMapping(value ="/view-order")
	public String viewOrder(@RequestParam(name = "id") long id, Model model) {
		Orders orders = orderService.findOrderById(id);
		model.addAttribute("order", orders);
		return "view-order";
	}
	
	@RequestMapping(value ="/acceptOrder")
	public String acceptOrder(@RequestParam(name = "id") long id, Model model) {
		try {
			orderService.acceptOrder(id);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		return "redirect:/new-orders";
	}
	
	@RequestMapping(value ="/successOrder")
	public String successOrder(@RequestParam(name = "id") long id, Model model) {
		try {
			orderService.setSuccessOrder(id);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "redirect:/accepted-orders";
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
	
	@RequestMapping("/accepted-orders")
	public String acceptedOrders(Model model, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="s", defaultValue = "") String s) {
		
		
		
		if(s.equalsIgnoreCase("")) {
			List<Orders>orders = orderService.acceptedOrders(5,(page-1)*5);
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
					list = orderService.userAcceptedOrders(users.get(i).getId());
				}
			}
			model.addAttribute("orders",list);
		}
		return "accepted-orders";
	}
	
	@RequestMapping("/success-orders")
	public String successOrders(Model model, @RequestParam(name="page", defaultValue = "1") int page,  @RequestParam(name="s", defaultValue = "") String s) {
		
		
		if(s.equalsIgnoreCase("")) {
			List<Orders>orders = orderService.successOrders(5,(page-1)*5);
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
					list = orderService.userSuccessOrders(users.get(i).getId());
				}
			}
			model.addAttribute("orders",list);
		}
		return "success-orders";
	}
	
	@Autowired
	OrderItemDao orderItemDao;
	@RequestMapping("/delete-orderItem")
	public String deleteOrderItem(@RequestParam(name="id"  )long id, @RequestParam(name="orderId" ) long orderId) {
		orderItemDao.deleteOrderItem(id);
		Orders order = orderService.findOrderById(orderId);
		order.calTotal();
		orderService.saveOrder(order);
		return "redirect:/view-order?id="+orderId;
	}
	
	@RequestMapping("/delete-order")
	public String deleteOrder( @RequestParam(name="id" ) long orderId) {
		orderService.deleteOrder(orderId);
		
		return "redirect:/new-orders";
	}
	@Autowired
	SimpleDateFormat dateFormat;
	
	
	@RequestMapping(value="/updateOrder", method = RequestMethod.POST)
	@ResponseBody
	public String updateCart(@RequestBody String[] data, HttpSession httpSession, Model model) {
		String date = dateFormat.format(new Date());
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Orders orders = orderService.findOrderById(Long.valueOf(data[0]));
		
		List<OrderItems>orderItems = orders.getOrderItems();
		for (int i = 1; i < data.length; i++) {
				int quantity = Integer.valueOf(data[i]);
				if(quantity != orderItems.get(i-1).getQuantity()) {
					orderItems.get(i-1).setQuantity(quantity);
					orderItems.get(i-1).calSubTotal();
					orderItems.get(i-1).setUpdatedAt(date);
				}
			}
			orders.setOrderItems(orderItems);
			orders.calTotal();
			orderService.saveOrder(orders);
			
		
		
		return data[0];
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
	
	@RequestMapping("/view-order-details")
	public String viewOrderDeetails(@RequestParam(name="orderId")long id, Model model) {
		
		Orders orders = orderService.findOrderById(id);
	
		model.addAttribute("order", orders);
		return "view-order-detail";
	}
	
	@RequestMapping("/delete-user")
	public String deleteUser(@RequestParam(name="id")long id) {
		User u = userService.findUserByid(id);
		u.setDeletedAt(dateFormat.format(new Date()));
		u.setIsDeleted(1);
		userService.updateUserInfo(u);
		return "redirect:/list-users";
	}
	
	
	
}
