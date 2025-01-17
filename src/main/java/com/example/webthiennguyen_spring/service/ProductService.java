package com.example.webthiennguyen_spring.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.webthiennguyen_spring.entities.Address;
import com.example.webthiennguyen_spring.entities.Category;
import com.example.webthiennguyen_spring.entities.Products;
import com.example.webthiennguyen_spring.entities.User;
import com.example.webthiennguyen_spring.repository.AddressRepository;
import com.example.webthiennguyen_spring.repository.CategoryRepository;
import com.example.webthiennguyen_spring.repository.ProductRepository;
import com.example.webthiennguyen_spring.repository.UserRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private UserRepository userRepository;
	
	public List<Products> getProduct() {
		return productRepository.getProducts();
	}
	
	public Map<String, Map<String, List<Products>>> getProductsByCategory() {
	    List<Object[]> data = productRepository.findProductsGroupedByCategoryAndUser();
	    Map<String, Map<String, List<Products>>> productsByCategoryAndUser = new LinkedHashMap<>();

	    for (Object[] row : data) {
	        String categoryName = (String) row[0];  // Category name
	        Products product = (Products) row[1];   // Product
	        String userName = (String) row[2];      // User name

	        String images = product.getImgPaths(); // Lấy chuỗi hình ảnh
	        String[] imageArray = images.split(",");

	        product.setImages(imageArray); 
	        
	        // Ensure the category exists, then add the product under the user's name
	        productsByCategoryAndUser
	            .computeIfAbsent(categoryName, k -> new LinkedHashMap<>())
	            .computeIfAbsent(userName, k -> new ArrayList<>())
	            .add(product);
	    }
	    System.out.println(productsByCategoryAndUser);

	    return productsByCategoryAndUser;
	}

	public Products getProductById(Long productId) {
		Products product = productRepository.findProductById(productId);
	    
		String images = product.getImgPaths(); 
        String[] imageArray = images.split(",");

        product.setImages(imageArray); 
	    
	    return product;
    }
	

	public List<Products> searchProductByName(String productName) {
        return productRepository.findByNameContainingIgnoreCase(productName); 
    }
	
	public Map<String, Map<String, List<Products>>> searchProductsGroupedByCategoryAndUser(String productName) {
        // Tìm kiếm sản phẩm theo tên
        List<Object[]> data = productRepository.findProductsGroupedByCategoryAndUserAndSearchAndAddress(productName);
        
        Map<String, Map<String, List<Products>>> productsByCategoryAndUser = new LinkedHashMap<>();

        for (Object[] row : data) {
            String categoryName = (String) row[0];  // Tên danh mục
            Products product = (Products) row[1];   // Sản phẩm
            String userName = (String) row[2];     // Tên người dùng
            
            String images = product.getImgPaths(); // Lấy chuỗi hình ảnh
	        String[] imageArray = images.split(",");
	        product.setImages(imageArray); 
	        
            // Đảm bảo danh mục đã tồn tại, sau đó thêm sản phẩm vào dưới tên người dùng
            productsByCategoryAndUser
                .computeIfAbsent(categoryName, k -> new LinkedHashMap<>())
                .computeIfAbsent(userName, k -> new ArrayList<>())
                .add(product);
        }

        return productsByCategoryAndUser;
    }
	
	
	public Map<String, Long> getProductStatisticsByCategory() {
	    List<Object[]> data = productRepository.countProductsByCategory();
	    Map<String, Long> statistics = new LinkedHashMap<>();

	    for (Object[] row : data) {
	        String categoryName = (String) row[0];  // Tên danh mục
	        Long productCount = (Long) row[1];     // Số lượng sản phẩm
	        statistics.put(categoryName, productCount);
	    }

	    return statistics;
	}
	
	public Map<Integer, Long> getProductStatisticsByMonth(int year) {
	    List<Object[]> results = productRepository.findProductStatisticsByYear(year);
	    Map<Integer, Long> statistics = new LinkedHashMap<>();

	    for (Object[] row : results) {
	        Integer month = (Integer) row[0];
	        Long count = (Long) row[1];
	        statistics.put(month, count);
	    }

	    return statistics;
	}
	

	
	public boolean deleteProduct(int productId) {
	    if (productRepository.existsById(productId)) {
	        System.out.println("Product exists, proceeding to delete.");
	        productRepository.deleteById(productId);
	        return true;
	    } else {
	        System.out.println("Product does not exist.");
	    }
	    return false;
	}


	 public List<Products> listProductsByUserId(Integer userId) {
		 List<Products> products = productRepository.listProductByIdUser(userId);
			for (Products product : products) {
				String images = product.getImgPaths(); 
		        String[] imageArray = images.split(",");
		
		        product.setImages(imageArray); 
			}
		    
		    return products;
	    }

	 public List<Products> listProductNotApprove() {
			List<Products> products = productRepository.listProductNotApprove();
			for (Products product : products) {
				String images = product.getImgPaths(); 
		        String[] imageArray = images.split(",");
		
		        product.setImages(imageArray); 
			}
		    
		    return products;
	    }
	 
	 
	 public Products addProduct(Products product) {
			// Kiểm tra sự tồn tại của các thực thể liên quan
			Category category = categoryRepository.findById(product.getId_category().getId())
					.orElseThrow(() -> new RuntimeException("Category not found"));
			Address address = addressRepository.findById(product.getId_address().getId())
					.orElseThrow(() -> new RuntimeException("Address not found"));
			User user = userRepository.findById(product.getId_user().getId())
					.orElseThrow(() -> new RuntimeException("User not found"));

			// Gán lại các thực thể đã lấy
			product.setId_category(category);
			product.setId_address(address);
			product.setId_user(user);
			product.setCreate_day(new Date());

			// Lưu sản phẩm vào cơ sở dữ liệu
			return productRepository.save(product);
		}
	 
	 
	 /********************************************************/
	 
	 public Products getProductById(int id) {
			return productRepository.findById(id).orElse(null);
		}
	 public void updateProduct(Products product) {
	        // Kiểm tra và thực hiện logic cập nhật nếu cần
	        productRepository.save(product); // Lưu sản phẩm sau khi sửa
	    }
	 
	
}

