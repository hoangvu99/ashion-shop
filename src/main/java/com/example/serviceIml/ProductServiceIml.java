package com.example.serviceIml;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dao.ProductDao;
import com.example.dao.ProductSizeDao;
import com.example.dao.SizeDao;
import com.example.dto.ProductDTO;
import com.example.model.category.Category;
import com.example.model.product.Product;
import com.example.model.product.ProductImages;
import com.example.model.size.ProductSize;
import com.example.model.size.Size;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.service.UploadFileService;



@Service
public class ProductServiceIml implements ProductService{
	
	@Autowired
	ProductDao productDao;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductSizeDao productSizeDao;
	
	@Autowired
	SizeDao sizeDao;
	
	@Autowired
	UploadFileService uploadFileService;
	
	@Autowired
	SimpleDateFormat simpleDateFormat;
	@Override
	public Product findProductById(long id) {
		Product product = productDao.findProductById(id);
		return product;
		
	}

	@Override
	public void saveProduct(ProductDTO productDTO) {
		Category category = categoryService.findCategoryById(Integer.valueOf(productDTO.getCategory()));
		Product product = new Product();
		
		product.setCategory(category);
		product.setName(productDTO.getProductName());
		product.setPrice(productDTO.getPrice());
		product.setThumnail(productDTO.getThumnail().getOriginalFilename());
		
		Collection<ProductImages>images = new ArrayList<ProductImages>();
		
		for(MultipartFile f: productDTO.getImages()) {
			if(f.getOriginalFilename() != "") {
				
				ProductImages productImages = new ProductImages();
				productImages.setUrl(f.getOriginalFilename());
				productImages.setProduct(product);
				images.add(productImages);
				try {
					uploadFileService.saveUploadFile(f);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		product.setCreatedAt(simpleDateFormat.format(new Date()));
		product.setCreatedBy("ADMIN");
		
		product.setProductImages(images);
		
		List<Size> sizes = sizeDao.findAll();
		List<ProductSize>productSizes = new ArrayList<ProductSize>();
		sizes.stream().forEach(s ->{
			ProductSize productSize = new ProductSize();
			productSize.setProduct(product);
			productSize.setSize(s);
			productSize.setCreatedAt(simpleDateFormat.format(new Date()));
			switch (s.getSizeName()) {
			case "XS":
				productSize.setQuantity(productDTO.getXsSize());
				break;
			case "S":
				productSize.setQuantity(productDTO.getSSize());
				break;
			case "M":
				productSize.setQuantity(productDTO.getMSize());
				break;
			case "L":
				productSize.setQuantity(productDTO.getLSize());
				break;
			case "XL":
				productSize.setQuantity(productDTO.getXlSize());
				break;

			default:
				break;
			}
			productSizes.add(productSize);
		});
		product.setProductSizes(productSizes);
		try {
			uploadFileService.saveUploadFile(productDTO.getThumnail());
		} catch (Exception e) {
			// TODO: handle exception
		}
		product.setDescription(productDTO.getDescription());
		product.setDetailsContent(productDTO.getDetail());
		productDao.save(product);
		
		
	}

	@Override
	public List<Product> getListProduct() {
		
		
		return productDao.findAll();
	}

	@Override
	public Page<Product> getPageProduct(int page) {
		Page<Product>p = productDao.findAll(PageRequest.of(page-1, 5, Sort.by("id").descending()));
		return p;
	}
	
	@Override
	public List<Product> searchProductByName(String name) {
		
		return productDao.searchProductByName(name);
	}
	
	@Override
	public void saveProduct(Product p) {
		
		productDao.saveAndFlush(p);
		
	}
	
	@Override
	public void updateProduct(ProductDTO productDTO, long id) {
		
		Product product = productDao.findProductById(id);
		
		
		if(productDTO.getCategory()!="0") {
			Category category = categoryService.findCategoryById(Integer.valueOf(productDTO.getCategory()));
			product.setCategory(category);
		}
			
		
		
		product.setName(productDTO.getProductName());
		product.setPrice(productDTO.getPrice());
		if(productDTO.getThumnail().getOriginalFilename() !="") {
			product.setThumnail(productDTO.getThumnail().getOriginalFilename());
			try {
				uploadFileService.saveUploadFile(productDTO.getThumnail());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		Collection<ProductImages>images = new ArrayList<ProductImages>();
		
		for(MultipartFile f: productDTO.getImages()) {
			if(f.getOriginalFilename() != "") {
				
				ProductImages productImages = new ProductImages();
				productImages.setUrl(f.getOriginalFilename());
				productImages.setProduct(product);
				images.add(productImages);
				try {
					uploadFileService.saveUploadFile(f);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		product.setUpdatedAt(simpleDateFormat.format(new Date()));
		product.setCreatedBy("ADMIN");
		
		if(images.size() !=0) {
			product.setProductImages(images);
		}
		
		List<Size> sizes = sizeDao.findAll();
		List<ProductSize>productSizes = new ArrayList<ProductSize>();
		sizes.stream().forEach(s ->{
			ProductSize productSize = new ProductSize();
			productSize.setProduct(product);
			productSize.setSize(s);
			productSize.setCreatedAt(simpleDateFormat.format(new Date()));
			switch (s.getSizeName()) {
			case "XS":
				productSize.setQuantity(productDTO.getXsSize());
				break;
			case "S":
				productSize.setQuantity(productDTO.getSSize());
				break;
			case "M":
				productSize.setQuantity(productDTO.getMSize());
				break;
			case "L":
				productSize.setQuantity(productDTO.getLSize());
				break;
			case "XL":
				productSize.setQuantity(productDTO.getXlSize());
				break;

			default:
				break;
			}
			productSizes.add(productSize);
		});
		product.setProductSizes(productSizes);
		
		product.setDescription(productDTO.getDescription());
		product.setDetailsContent(productDTO.getDetail());
		productDao.saveAndFlush(product);
		
	}

}
