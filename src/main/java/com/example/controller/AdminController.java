package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dto.ProductDTO;
import com.example.model.product.Product;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.service.ProductSizeService;
import com.example.service.UploadFileService;
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
	
	@RequestMapping(value = "/add-product", method = RequestMethod.GET )
	public String addProductView(Model model) {
		model.addAttribute("listCategory", categoryService.listCategories());
		model.addAttribute("product", new ProductDTO());
		return"add-product";
	}
	
	@RequestMapping(value ="/add-product", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String addPr(@ModelAttribute ProductDTO productDTO) {
		
		
		productService.saveProduct(productDTO);
		
		
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
		productDTO.setPrice(p.getPrice());
		
		p.getProductSizes().stream().forEach(s -> {
			
			switch (s.getSize().getSizeName()) {
			case "XS":
				productDTO.setXsSize(s.getQuantity());
				break;
			case "S":
				productDTO.setSSize(s.getQuantity());
				break;
			case "M":
				productDTO.setMSize(s.getQuantity());
				break;
			case "L":
				productDTO.setLSize(s.getQuantity());
				break;
			case "XL":
				productDTO.setXlSize(s.getQuantity());
				break;

			default:
				break;
			}
		});
		
		model.addAttribute("product", p);
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("id", id);
		model.addAttribute("listCategory", categoryService.listCategories());
		return "view-product";
	
	}
	
	@RequestMapping(value="/update-product", method = RequestMethod.POST , consumes = "multipart/form-data")
	@ResponseBody
	public String upload(@RequestParam(name="id") long id, @ModelAttribute ProductDTO productDTO) {
		
		 productService.updateProduct(productDTO, id);
		
		return "cap nhap san pham thanh cong";
	}
}
