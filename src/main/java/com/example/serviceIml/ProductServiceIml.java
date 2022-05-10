package com.example.serviceIml;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
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
		Product product = new Product();	
		String fomattedDate = simpleDateFormat.format(new Date());
		String fileName = fomattedDate.replaceAll("[:\\s]+", "-");
		
		Category category = categoryService.findCategoryById(Integer.valueOf(productDTO.getCategory()));
		List<Size> sizes = sizeDao.findAll();
		List<ProductSize>productSizes = new ArrayList<ProductSize>();
		
		
		
		if(category.getId() == 3) {
			List<Size>sizeNum = getSize(sizes, 6, 19);
			for (int i = 0; i < productDTO.getSizesInNum().size(); i++) {
				ProductSize productSize = new ProductSize();
				productSize.setProduct(product);
				productSize.setSize(sizeNum.get(i));
				productSize.setCreatedAt(fomattedDate);
				productSize.setQuantity(productDTO.getSizesInNum().get(i).getQuantity());
				productSizes.add(productSize);
				
			}
			
		}else if(category.getId() == 5) {
			ProductSize productSize = new ProductSize();
			productSize.setProduct(product);
			productSize.setSize(sizes.get(19));
			productSize.setCreatedAt(fomattedDate);
			productSize.setQuantity(productDTO.getFreeSize());
			productSizes.add(productSize);
		}else {
			List<Size>sizeText = getSize(sizes, 0, 6);
			for (int i = 0; i < sizes.size(); i++) {
				ProductSize productSize = new ProductSize();
				productSize.setProduct(product);
				productSize.setSize(sizeText.get(i));
				productSize.setCreatedAt(fomattedDate);
				productSize.setQuantity(productDTO.getSizes().get(i).getQuantity());
				productSizes.add(productSize);
				
			}
		}
		
		
		
		product.setProductSizes(productSizes);
		
		
		
		
		
			
		product.setCategory(category);
		product.setName(productDTO.getProductName());
		

		product.setPriceInNum(productDTO.getPrice()*1000);
		
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
		
		
		
		product.setDescription(productDTO.getDescription());
		product.setDetailsContent(productDTO.getDetail());
		productDao.save(product);
		
		
	}

	@Override
	public List<Product> getListProduct() {
		
		
		return productDao.findAll();
	}

	@Override
	public List<Product> getPageProduct(int limit,int page) {
		List<Product>p = productDao.getPageProduct(limit,(page-1)*5);
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
			
			
			
				
			
			List<ProductSize>productSizes = productSizeDao.findProductSizeByProductId(product.getId());
			if(product.getCategory().getId() == 3) {
				
				for (int i = 0; i < productSizes.size(); i++) {
					productSizes.get(i).setQuantity(productDTO.getSizesInNum().get(i).getQuantity());
					productSizes.get(i).setUpdatedAt(fomattedDate);
					
				}
				
			}else if(product.getCategory().getId() == 5) {
				for (int i = 0; i < productSizes.size(); i++) {
					productSizes.get(i).setQuantity(productDTO.getFreeSize());
					productSizes.get(i).setUpdatedAt(fomattedDate);
					
				}
			}else {
				for (int i = 0; i < productSizes.size(); i++) {
					productSizes.get(i).setQuantity(productDTO.getSizes().get(i).getQuantity());
					productSizes.get(i).setUpdatedAt(fomattedDate);
					
				}
			}
			product.setProductSizes(productSizes);
			
			
			product.setName(productDTO.getProductName());
			
			if(product.getPriceInNum() != productDTO.getPrice()) {
				product.setOldPrice(product.getPriceInNum()*1000);
				product.setPriceInNum(productDTO.getPrice()*1000);
				
			}
			
			
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
	
	@Override
	public void deleteProduct(long id) {
		productDao.deleteProduct(id);
		
	}
	
	@Override
	public List<Product> listProductByCategory(int category, int limit, int offset) {
		
		return productDao.productsByCategory(category, limit, (offset -1 )*5);
	}
	
	@Override
	public List<Product> listProductByName(String s, int limit, int offset) {
		// TODO Auto-generated method stub
		return productDao.productsByName(s, limit,  (offset -1 )*5);
	}
	
	@Override
	public List<Size> getSize(List<Size>s,int begin, int end) {
		List<Size> sizes = new ArrayList<Size>();
		for (int i = begin; i < end; i++) {
			sizes.add(s.get(i));
		}
		
		return sizes;
	}
	@Override
	public List<Product> relatedProducts(int categoryId, long productId) {
		// TODO Auto-generated method stub
		return productDao.getListRelatedProducts(categoryId, productId);
	}
	

}
