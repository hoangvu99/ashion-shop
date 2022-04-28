package com.example.serviceIml;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dao.ProductDao;
import com.example.dao.ProductImageDao;
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
	
	@Autowired
	ProductImageDao productImageDao;
	
	@Override
	public Product findProductById(long id) {
		Product product = productDao.findProductById(id);
		return product;
		
	}

	@Override
	public void saveProduct(ProductDTO productDTO) {
		String formattedDate = simpleDateFormat.format(new Date());
		Category category = categoryService.findCategoryById(Integer.valueOf(productDTO.getCategory()));
		Product product = new Product();
		
		product.setCategory(category);
		product.setName(productDTO.getProductName());
		product.setPrice(productDTO.getPrice());
		
		String fileName= formattedDate.replaceAll("[:\\s]", "-")+".jpg";
		product.setThumnail(fileName);
		try {
			
			uploadFileService.saveUploadFile(productDTO.getThumnail(), fileName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collection<ProductImages>images = new ArrayList<ProductImages>();
		
		for (int i = 0; i < productDTO.getImages().size(); i++) {
			ProductImages productImages = new ProductImages();
			if(productDTO.getImages().get(i).getOriginalFilename() != "") {		
				productImages.setUrl(fileName+"dt.jpg");				
				try {
					uploadFileService.saveUploadFile(productDTO.getImages().get(i),fileName+"dt-"+i+".jpg");
					productImages.setCreatedAt(formattedDate);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}else {
				productImages.setUrl("null.jpg");
				
			}
			productImages.setProduct(product);
			
			images.add(productImages);
		}
		
		
		
		
		product.setCreatedAt(formattedDate);
		product.setCreatedBy("ADMIN");
		
		product.setProductImages(images);
		
		List<Size> sizes = sizeDao.findAll();
		List<ProductSize>productSizes = new ArrayList<ProductSize>();
		int index = 0;
		productDTO.getSizes().stream().forEach(i ->{
			ProductSize productSize = new ProductSize();
			productSize.setProduct(product);
			productSize.setSize(sizes.get(index));
			productSize.setQuantity(i.getQuantity());
			productSize.setCreatedAt(formattedDate);
			productSizes.add(productSize);
		});
		
		
		product.setProductSizes(productSizes);
		
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
		String formattedDate = simpleDateFormat.format(new Date());
		String fileName = formattedDate.replace("[:\\s]", "-");
		Product product = productDao.findProductById(id);

		
		if(productDTO.getCategory().compareTo("0")!=0) {
			
			Category category = categoryService.findCategoryById(Integer.valueOf(productDTO.getCategory()));
			product.setCategory(category);
		}
			
		
		
		product.setName(productDTO.getProductName());
		product.setPrice(productDTO.getPrice());
		
	
		if(!productDTO.getThumnail().getOriginalFilename().equalsIgnoreCase("")) {
			
			product.setThumnail(fileName+".jpg");
			try {
				uploadFileService.saveUploadFile(productDTO.getThumnail(),fileName+".jpg");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		
		
		
			

		List<ProductImages> listImages = productImageDao.findByProductId(id);
		
		List<MultipartFile> listImagesDTO = productDTO.getImages();
		
		for (int i = 0; i < 4; i++) {
			
			if(listImagesDTO.get(i).getOriginalFilename()!="") {
				
				if(listImages.get(i).getUrl()=="null.jpg") {
					listImages.get(i).setCreatedAt(formattedDate);
				}else {
					listImages.get(i).setUpdatedAt(formattedDate);
				}
				listImages.get(i).setUrl(fileName+"dt-"+i+".jpg");
				
				try {
					uploadFileService.saveUploadFile(listImagesDTO.get(i),fileName+"dt-"+i+".jpg");
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				
			}
		}
		
		
		product.setUpdatedAt(formattedDate);
		product.setCreatedBy("ADMIN");
		
		
			product.setProductImages(listImages);
		
		
		List<Size> sizes = sizeDao.findAll();
		List<ProductSize>productSizes = productSizeDao.findProductSizeByProductId(id);
		
		
		
		product.setProductSizes(productSizes);
		
		product.setDescription(productDTO.getDescription());
		product.setDetailsContent(productDTO.getDetail());
		productDao.saveAndFlush(product);
		
	}
	
	@Override
	public List<Product> lastestProducts() {
		// TODO Auto-generated method stub
		return productDao.listLastestProduct();
	}
	
	

}
