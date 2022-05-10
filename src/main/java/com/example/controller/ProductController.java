package com.example.controller;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import com.example.model.product.Product;
import com.example.model.size.ProductSize;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.service.ProductSizeService;
import com.example.service.SizeService;
import com.example.service.UploadFileService;
import com.example.service.UserAddressService;
import com.example.service.UserService;

@Controller
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	UploadFileService uploadFileService;
	
	
	
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
	
	@RequestMapping(value = "/product-details")
	public String productDetailsController(@RequestParam(name="id")long id, Model model, HttpSession httpSession) {	
		Product p = productService.findProductById(id);
		List<Product>related = productService.relatedProducts(p.getCategory().getId(), id);
		if(related == null) {
			related = new ArrayList<Product>();
		}
		model.addAttribute("product", p);
		model.addAttribute("relatedProducts", related);
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
	
	@RequestMapping(value = "/add-product", method = RequestMethod.GET )
	public String addProductView(Model model) {
		model.addAttribute("listCategory", categoryService.listCategories());
		List<SizeDTO>sizeDTOs = new ArrayList<SizeDTO>();
		List<SizeDTO>sizesInNum = new ArrayList<SizeDTO>();
		SizeDTO freeSize = new SizeDTO("Free size",0);
		List<String>sizes = sizeService.listSizeName();
		sizeDTOs.add(new SizeDTO("XS", 0));
		sizeDTOs.add(new SizeDTO("S", 0));
		sizeDTOs.add(new SizeDTO("M", 0));
		sizeDTOs.add(new SizeDTO("L", 0));
		sizeDTOs.add(new SizeDTO("XL", 0));
		sizeDTOs.add(new SizeDTO("XXL", 0));
		
		for (int i = 32; i < 45; i++) {
			
			sizesInNum.add(new SizeDTO(String.valueOf(i), 0));
		}
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setSizes(sizeDTOs);
		productDTO.setFreeSize(0);
		productDTO.setSizesInNum(sizesInNum);
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
		
		List<SizeDTO>sizeNum = new ArrayList<SizeDTO>();
		List<SizeDTO>sizeText = new ArrayList<SizeDTO>();
		int freeSize = 0;
		
		if(p.getCategory().getId() == 3) {
			p.getProductSizes().stream().forEach(i ->{
				sizeNum.add(new SizeDTO(i.getSize().getSizeName(), i.getQuantity()));
			});
		}else if(p.getCategory().getId() == 5){
			ProductSize productSize = (ProductSize) p.getProductSizes().toArray()[0];
			freeSize = productSize.getQuantity();
		}else {
			
			p.getProductSizes().stream().forEach(s -> {
				sizeText.add(new SizeDTO(s.getSize().getSizeName(), s.getQuantity()));
			});
		}
		

		
		
		
		
		productDTO.setPrice(p.getPriceInNum()/1000);
		productDTO.setSizes(sizeText);
		productDTO.setFreeSize(freeSize);
		productDTO.setSizesInNum(sizeNum);
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
}
