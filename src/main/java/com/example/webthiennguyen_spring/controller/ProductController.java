package com.example.webthiennguyen_spring.controller;


import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.webthiennguyen_spring.entities.Address;
import com.example.webthiennguyen_spring.entities.Category;
import com.example.webthiennguyen_spring.entities.Products;
import com.example.webthiennguyen_spring.entities.User;
import com.example.webthiennguyen_spring.repository.AddressRepository;
import com.example.webthiennguyen_spring.repository.CategoryRepository;
import com.example.webthiennguyen_spring.repository.ProductRepository;
import com.example.webthiennguyen_spring.service.AddressService;
import com.example.webthiennguyen_spring.service.ProductService;
import com.example.webthiennguyen_spring.service.UserService;

import jakarta.servlet.http.HttpSession;




@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private AddressService addressService;
	
	@GetMapping("/list-post")
	private String view(Model model) {
		List<Products> productsList = productService.getProduct();
		Map<String, Map<String, List<Products>>> productsByCategory = productService.getProductsByCategory();
		model.addAttribute("listpost", productsByCategory);
		return "views/list_post";
	}
	
	@GetMapping("/product/{id}")
    public String viewProductDetail(@PathVariable("id") Long productId, Model model) {
        Products product = productService.getProductById(productId);
        model.addAttribute("product", product);
        System.out.print("Product: "+product);
        return "views/product_detail"; // Tên trang chi tiết sản phẩm
    }

	@GetMapping("/search")
	public String searchProduct(@RequestParam(value = "inf_search", required = false) String productName, Model model) {
	    if (productName == null || productName.isEmpty()) {
	        // Nếu không có giá trị tìm kiếm, trả về tất cả sản phẩm
	        Map<String, Map<String, List<Products>>> productsByCategory = productService.getProductsByCategory();
	        model.addAttribute("listpost", productsByCategory);
	    } else {
	        // Nếu có giá trị tìm kiếm, gọi hàm tìm kiếm sản phẩm
	        Map<String, Map<String, List<Products>>> productsByCategory = productService.searchProductsGroupedByCategoryAndUser(productName);
	        model.addAttribute("listpost", productsByCategory);
	    }
	    return "views/list_post";
	}
	
	@GetMapping("/statistics")
    public String showStatisticsPage() {
        // Trả về view ban đầu với form (chưa có bảng thống kê)
        return "views/statistics";
    }

    @PostMapping("/statistics/category")
    public String getProductStatistics(Model model) {
        // Lấy dữ liệu thống kê từ service
        Map<String, Long> statistics = productService.getProductStatisticsByCategory();

        // Truyền dữ liệu vào model
        model.addAttribute("statistics", statistics);

        // Trả về view với dữ liệu thống kê
        return "views/statistics";
    }
    
    @PostMapping("/statistics-by-month")
    public String statisticsByMonth(@RequestParam("year") int year, Model model) {
        // Giả sử bạn có phương thức lấy thống kê theo tháng
        Map<Integer, Long> statisticsMonth = productService.getProductStatisticsByMonth(year);

        // Tạo một map cho tất cả các tháng từ 1 đến 12
        Map<Integer, Long> allMonthsStatistics = new LinkedHashMap<>();
        for (int month = 1; month <= 12; month++) {
            // Nếu có thống kê cho tháng, dùng giá trị thống kê, nếu không thì gán 0
            allMonthsStatistics.put(month, statisticsMonth.getOrDefault(month, 0L));
        }

        // Truyền giá trị năm và thống kê vào model
        model.addAttribute("year", year);
        model.addAttribute("statisticsMonth", allMonthsStatistics);

        return "views/statistics";
    }
    
    @GetMapping("/list-products-by-user")
    public String listProductsByUser(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user"); // Lấy thông tin người dùng từ session

        if (user == null) {
            model.addAttribute("errorMessage", "Vui lòng đăng nhập để xem danh sách sản phẩm.");
            return "views/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }

        List<Products> products = productService.listProductsByUserId(user.getId());
        model.addAttribute("products", products);
        System.out.print("Product: "+products);


        return "views/list-products-by-user"; // Tên trang hiển thị danh sách sản phẩm
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int productId, RedirectAttributes redirectAttributes) {
        if (productService.deleteProduct(productId)) {
            redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công!");
            redirectAttributes.addFlashAttribute("alertClass", "success");
        } else {
            redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thất bại. Sản phẩm không tồn tại.");
            redirectAttributes.addFlashAttribute("alertClass", "danger");
        }
        return "redirect:/list-products-by-user";
    }

    @GetMapping("/navbar")
	private String viewNavbar(Model model) {
		return "views/navbar";
	}

    
    @GetMapping("/product/not-approve/{id}")
	@ResponseBody
	public Products getProductById(@PathVariable("id") Long productId) {
	    return productService.getProductById(productId);
	}
    
    @GetMapping("/list-post-not-approve")
    public String listProductNotApprove(Model model) {
        List<Products> products = productService.listProductNotApprove();
        model.addAttribute("products", products);
        System.out.print("Product: "+products);
        return "views/list-post-not-approve"; 
    }
    
    @PutMapping("/approve/{id}")
    public ResponseEntity<Map<String, String>> approveProduct(@PathVariable("id") Integer productId) {
        Optional<Products> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Products product = productOptional.get();

            // Cập nhật isExist thành true
            product.setExist(true);
            productRepository.save(product);

            // Tạo thông báo thành công
            Map<String, String> response = new HashMap<>();

            return ResponseEntity.ok(response);
        } else {
            // Tạo thông báo thất bại nếu sản phẩm không tồn tại
            Map<String, String> response = new HashMap<>();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

 // Hiển thị form thêm sản phẩm
 	@GetMapping("/products/create")
 	public String showAddProductForm(Model model, @RequestParam(required = false) String error) {
 		// Lấy danh sách loại sản phẩm
 		List<Category> categories = categoryRepository.findAll();
 		model.addAttribute("categories", categories);
 		model.addAttribute("product", new Products());
 		model.addAttribute("address", new Address());

 		if ("true".equals(error)) {
 			model.addAttribute("error", true); // Hiển thị lỗi nếu có tham số 'error'
 		}

 		return "views/products/create-product"; // Tên file HTML
 	}

// 	// Lấy danh sách loại sản phẩm
// 	@GetMapping("/categories")
// 	public ResponseEntity<List<Category>> getCategories() {
// 		List<Category> categories = categoryRepository.findAll();
// 		return ResponseEntity.ok(categories);
// 	}

 	@PostMapping("/products/create")
 	public String createProduct(@ModelAttribute Products product, 
 	        @RequestParam("provinceName") String provinceName,
 	        @RequestParam("districtName") String districtName,
 	        @RequestParam("wardName") String wardName,
 	        @RequestParam("createdDate") String createdDate, 
 	        @RequestParam("img") List<MultipartFile> images,
 	        HttpSession session, Model model) {

 	    try {
 	        // Kiểm tra người dùng đã đăng nhập chưa
 	        User loggedInUser = (User) session.getAttribute("user");
 	        if (loggedInUser == null) {
 	            model.addAttribute("errorMessage", "Bạn cần đăng nhập để tạo sản phẩm.");
 	            return "views/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
 	        }

 	        // Liên kết sản phẩm với người dùng đã đăng nhập
 	        product.setId_user(loggedInUser);

 	        // Tạo đối tượng Address và lưu vào DB
 	        Address address = new Address();
 	        //address.setProvince(provinceName); // Lưu tên tỉnh
 	        String cleanedProvinceName = provinceName.trim().replaceAll(",$", "");
 	        address.setProvince(cleanedProvinceName);
 	        
 	        String cleanedDistrictName = districtName.trim().replace(",$", "");
 	        address.setDistrict(cleanedDistrictName);
 	        
 	        String cleanedWardName = wardName.trim().replace(",$", "");
 	        address.setWard(cleanedWardName);
 	        
 	        Address savedAddress = addressService.saveAddress(address);
 	        product.setId_address(savedAddress);

 	        // Chuyển đổi ngày tạo từ chuỗi sang kiểu Date
 	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 	        Date dateCreated = sdf.parse(createdDate);
 	        product.setCreate_day(dateCreated);

 	        // Xử lý ảnh và lưu vào DB
 	        List<String> imagePaths = new ArrayList<>();
 	        for (MultipartFile image : images) {
 	            String imagePath = saveImage(image);
 	            imagePaths.add(imagePath);
 	        }
 product.setImgPaths(String.join(",", imagePaths));

 	        // Lưu sản phẩm vào DB
 	        productService.addProduct(product);

 	        return "redirect:/products/create?success=true"; // Redirect thành công
 	    } catch (Exception e) {
 	        e.printStackTrace();
 	        return "redirect:/products/create?error=true"; // Redirect nếu có lỗi
 	    }
 	}
 	// Hàm để lưu ảnh
 	private String saveImage(MultipartFile image) throws IOException {
 		String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
 		Path directory = Paths.get("src/main/resources/static/image_products/");

 		// Kiểm tra nếu thư mục chưa tồn tại thì tạo
 		if (!Files.exists(directory)) {
 			Files.createDirectories(directory);
 		}

 		Path filePath = directory.resolve(fileName); // Đường dẫn đầy đủ
 		Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING); // Lưu ảnh vào thư mục
 		return "/image_products/" + fileName; // Trả về đường dẫn ảnh
 	}
 	
 	
 	
 	/*****************************************************/
 	
 	
 	 private boolean isValidImageType(MultipartFile image) {
			String contentType = image.getContentType();
			return contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg");
		}
 	 
 	@GetMapping("/products/update/{id}")
	public String showEditProductForm(@PathVariable("id") Integer id, Model model) {
		// Lấy thông tin sản phẩm từ DB
		Products product = productService.getProductById(id);
		if (product == null) {
			model.addAttribute("errorMessage", "Sản phẩm không tồn tại!");
			return "redirect:/products?error=not_found";
		}

		Address address = product.getId_address();
		if (address == null) {
			model.addAttribute("errorMessage", "Địa chỉ không tồn tại cho sản phẩm này!");
			return "redirect:/products?error=address_not_found";
		}
		
		// Truyền dữ liệu hình ảnh
		String imgPaths = product.getImgPaths();
		model.addAttribute("imgPaths", imgPaths != null ? imgPaths.split(",") : new String[0]);
	

		 // Lấy danh sách tỉnh từ cơ sở dữ liệu (danh sách tên tỉnh, không phải Address)
        List<String> provinces = addressRepository.findDistinctProvinces();
        provinces.removeIf(province -> province.trim().equalsIgnoreCase(address.getProvince().trim()));
        model.addAttribute("provinces", provinces);
        
        List<Address> districts = addressRepository.findByProvince(address.getProvince()); // Lấy danh sách quận theo tỉnh
        List<Address> wards = addressRepository.findByDistrict(address.getDistrict()); // Lấy danh sách phường theo quận
		
	    // Thêm thông tin vào model để hiển thị trong form
	 // Truyền dữ liệu vào model
	    model.addAttribute("provinces", provinces);
	    model.addAttribute("districts", districts);
	    model.addAttribute("wards", wards);
		model.addAttribute("product", product);
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("address", address);

		return "views/products/update-product";
	}
	
	

	@PostMapping("/products/update/{id}")
	public String updateProduct(@PathVariable("id") Integer id, @ModelAttribute Products product,
			@RequestParam("provinceName") String province, @RequestParam("districtName") String district,
			@RequestParam("wardName") String ward, @RequestParam("createdDate") String createdDate,
			@RequestParam(value = "img", required = false) List<MultipartFile> images) {

		try {
			// Lấy sản phẩm từ database
			Products existingProduct = productService.getProductById(id);
			if (existingProduct == null) {
				return "redirect:/products?error=not_found"; // Không tìm thấy sản phẩm
			}
			
			// Cập nhật thông tin cơ bản
			product.setId(id);
			product.setId_user(existingProduct.getId_user());

			// Cập nhật địa chỉ
			Address address = existingProduct.getId_address();
			address.setProvince(province);
			address.setDistrict(district);
			address.setWard(ward);
			addressService.saveAddress(address); // Lưu địa chỉ

			product.setId_address(address);

			// Cập nhật ngày tạo
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			product.setCreate_day(sdf.parse(createdDate));

			// Xử lý hình ảnh
if (images != null && !images.isEmpty() && images.get(0).getSize() > 0) {
				List<String> imagePaths = new ArrayList<>();
				for (MultipartFile image : images) {
					// Kiểm tra loại file
					if (!isValidImageType(image)) {
						return "redirect:/products/update/" + id + "?error=invalid_image";
					}
					imagePaths.add(saveImage(image));
				}
				product.setImgPaths(String.join(",", imagePaths));
				
			} else {
				// Giữ nguyên ảnh cũ nếu không tải lên ảnh mới
				product.setImgPaths(existingProduct.getImgPaths() != null ? existingProduct.getImgPaths() : "");
			}

			// Cập nhật sản phẩm
			 productService.updateProduct(product);
//			 productRepository.updateProduct(product);

			return "redirect:/list-products-by-user";

		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/products/update/" + id + "?error=true";
		}
	}


}

