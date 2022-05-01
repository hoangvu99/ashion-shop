package com.example.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dto.ProductDTO;
import com.example.dto.SizeDTO;

import com.example.model.order.Orders;
import com.example.model.product.Product;
import com.example.model.size.Size;
import com.example.service.CategoryService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.ProductSizeService;
import com.example.service.SizeService;
import com.example.service.UploadFileService;
import com.example.service.UserService;

@Controller
public class AdminController {
	
	
	
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
	
	@RequestMapping("/list-product")
	public String listProductView(Model model, @RequestParam(name = "s", defaultValue = "") String s, @RequestParam(name="page", defaultValue = "1") int page) {
			
		
			if(s.equalsIgnoreCase("")== true) {
				int lastPage = (page == 1)? 1: page-1;
			
				model.addAttribute("lastPage", lastPage);
				model.addAttribute("page", page);
				model.addAttribute("nextPage", page+1);
				model.addAttribute("listProduct", productService.getPageProduct(page));
				
			}else {
				model.addAttribute("s", s);
				model.addAttribute("listProduct", productService.searchProductByName(s));
			}
			
			return"list-product";
	}
	
	@RequestMapping(value = "/admin")
	public String add() {			
		
		
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
		Orders orders = orderService.findOrderById(id);
		model.addAttribute("order", orders);
		model.addAttribute("message","Phê duyệt thành công");
		return "view-order";
	}
	
	
	
	
}
