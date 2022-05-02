package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.product.Product;
import com.example.service.CategoryService;
import com.example.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CategoryService categoryService;
	@RequestMapping(value = "/product-details")
	public String productDetailsController(@RequestParam(name="id")long id, Model model, HttpSession httpSession) {			
		
		model.addAttribute("product", productService.findProductById(id));
		
		return "product-details";
	}
	
	
	
	@RequestMapping(value = "/shop")
	public String shopController(@RequestParam(name = "page", defaultValue = "1")int page,  @RequestParam(name = "s", defaultValue = "") String s, @RequestParam(name ="c", defaultValue = "") String c  ,Model model) {
		
		String param = "";
		
		
//		if(s.equalsIgnoreCase("")== true) {
//		
//			int lastPage = (page == 1)? 1: page-1;
//		
//			model.addAttribute("lastPage", lastPage);
//			model.addAttribute("page", page);
//			model.addAttribute("nextPage", page+1);
//			model.addAttribute("listProduct", productService.getPageProduct(3,page));
//			
//		}else {
//			model.addAttribute("s", s);
//			model.addAttribute("listProduct", productService.searchProductByName(s));
//		}
		List<Product> products = null;
		if(s.equalsIgnoreCase("")== false) {
			param+= "&s="+s;
			products = productService.listProductByName(s, 6, page);
			
		}
		if(c.equalsIgnoreCase("")== false) {
			param+= "&c="+c;
			products = productService.listProductByCategory(Integer.valueOf(c), 6, page);
		}
		
		if(products == null) {
			 products = productService.getPageProduct(6, page);
		}
		int lastPage = (page == 1)? 1: page-1;
		
		
		
		model.addAttribute("lastPage","?page="+lastPage+param);
		model.addAttribute("page", page);
		model.addAttribute("nextPage", "?page="+(page+1)+param);
		
		model.addAttribute("listProduct",products);
		model.addAttribute("categories", categoryService.listCategories());
		return "shop";
	}
}
