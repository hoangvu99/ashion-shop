package com.example.serviceIml;

import java.io.IOException;
import java.text.NumberFormat;
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
	
	@Autowired
	NumberFormat numberFormat;
	
	
	@Override
	public Product findProductById(long id) {
		Product product = productDao.findProductById(id);
		return product;
		
	}

	@Override
	public void saveProduct(ProductDTO productDTO) {
		String fomattedDate = simpleDateFormat.format(new Date());
		String fileName = fomattedDate.replaceAll("[:\\s]+", "-");
		
		Category category = categoryService.findCategoryById(Integer.valueOf(productDTO.getCategory()));
		
		Product product = new Product();		
		product.setCategory(category);
		product.setName(productDTO.getProductName());
		
		String[] priceInNum = productDTO.getPriceInNum();
		String text = "";		
		text +=priceInNum[0];		
		text+=priceInNum[1];		
		text+=priceInNum[2];
		
		long priceInNumber =Long.valueOf(text);
		product.setPriceInNum(priceInNumber);
		product.setPrice(numberFormat.format(priceInNumber));
		
		
		if(productDTO.getThumnail().getOriginalFilename() != "") {
			product.setThumnail(fileName+".jpg");
			try {
				uploadFileService.saveUploadFile(productDTO.getThumnail(),"products",fileName+".jpg");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else {
			product.setThumnail("null.jpg");
		}
		
		Collection<ProductImages>images = new ArrayList<ProductImages>();
		

		List<MultipartFile>filesDTO = productDTO.getImages();
		for (int i = 0; i < 4; i++) {
			ProductImages productImages = new ProductImages();
			productImages.setCreatedAt(fomattedDate);
			productImages.setProduct(product);
			
			if(filesDTO.get(i).getOriginalFilename() =="") {
				System.out.println("ảnh chi tiết null");
				productImages.setUrl("null.jpg");
			}else {
				productImages.setUrl(fileName+"dt-"+i+".jpg");
				try {
					uploadFileService.saveUploadFile(filesDTO.get(i), "products",fileName+"dt-"+i+".jpg");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			images.add(productImages);
		}
		
		product.setCreatedAt(fomattedDate);
		product.setCreatedBy("ADMIN");
		
		product.setProductImages(images);
		
		List<Size> sizes = sizeDao.findAll();
		List<ProductSize>productSizes = new ArrayList<ProductSize>();
		
		
		
		for (int i = 0; i < sizes.size(); i++) {
			ProductSize productSize = new ProductSize();
			productSize.setProduct(product);
			productSize.setSize(sizes.get(i));
			productSize.setCreatedAt(fomattedDate);
			productSize.setQuantity(productDTO.getSizes().get(i).getQuantity());
			productSizes.add(productSize);
			
		}
		
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
		try {
			String fomattedDate = simpleDateFormat.format(new Date());
			String fileName = fomattedDate.replaceAll("[:\\s]+", "-");
			Product product = productDao.findProductById(id);
			
			
			if(productDTO.getCategory().equalsIgnoreCase("0") != true) {
				
				
				Category category = categoryService.findCategoryById(Integer.valueOf(productDTO.getCategory()));
				product.setCategory(category);
			}
				
			
			
			product.setName(productDTO.getProductName());
			
			String[] priceInNum = productDTO.getPriceInNum();
			String text = "";	
			for (int i = 0; i < priceInNum.length; i++) {
				text+= priceInNum[i];
			}
			
			
			long priceInNumber =Long.valueOf(text);
			product.setPriceInNum(priceInNumber);
			product.setPrice(numberFormat.format(priceInNumber));
			
			
			if(productDTO.getThumnail().getOriginalFilename() !="") {
				product.setThumnail(fileName+".jpg");
				try {
					uploadFileService.saveUploadFile(productDTO.getThumnail(), "products",fileName+".jpg");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
//			
			List<ProductImages>images = productImageDao.findByProductId(id);
			List<MultipartFile>filesDTO = productDTO.getImages();
			for (int i = 0; i < 4; i++) {
				if(filesDTO.get(i).getOriginalFilename() != "") {
					images.get(i).setUrl(fileName+"dt-"+i+".jpg");
					images.get(i).setCreatedAt(fomattedDate);
					try {
						uploadFileService.saveUploadFile(filesDTO.get(i), "products", fileName+"dt-"+i+".jpg");
					} catch (Exception e) {
						// TODO: handle exception
					}
//				
//					
//					
				}
				
				
			}
			
			
			product.setUpdatedAt(fomattedDate);
//			
//			
//			
			product.setProductImages(images);
//			
//			
			List<Size> sizes = sizeDao.findAll();
			List<ProductSize>productSizes = new ArrayList<ProductSize>();
			for (int i = 0; i < sizes.size(); i++) {
				ProductSize productSize = productSizeDao.findProductSize(id,i+1);
				
				productSize.setUpdatedAt(fomattedDate);
				productSize.setQuantity(productDTO.getSizes().get(i).getQuantity());
				productSizes.add(productSize);
				
			}
			product.setProductSizes(productSizes);
			
			product.setDescription(productDTO.getDescription());
			product.setDetailsContent(productDTO.getDetail());
			
			productDao.saveAndFlush(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<Product> lastestProducts() {
		// TODO Auto-generated method stub
		return productDao.listLastestProduct();
	}

}
