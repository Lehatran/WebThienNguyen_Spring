package com.example.webthiennguyen_spring.controller;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.webthiennguyen_spring.entities.User;
import com.example.webthiennguyen_spring.service.UserService;

import jakarta.servlet.http.HttpSession;

import com.example.webthiennguyen_spring.service.UserService;
@Controller
public class UserController {

	@Autowired
	private UserService userService;
		
	@GetMapping("/register")
	private String view(Model model) {
	    model.addAttribute("user", new User()); // Khởi tạo đối tượng user
	    return "views/register";
	}

		
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") User user, 
            @RequestParam("password_confirmation") String passwordConfirmation, Model model) {
	    try {
	    	userService.registerUser(user, passwordConfirmation);
	        model.addAttribute("successMessage", "Đăng ký thành công!");
	        return "redirect:/login"; // Trả về trang register
	    } catch (IllegalArgumentException e) {
	       // model.addAttribute("errorMessage", "Đăng ký thất bại. Vui lòng thử lại!");
	    	 model.addAttribute("errorMessage", e.getMessage());
	    	return "views/register"; // Stay on the registration page if there's an error
	    }
	}
	@GetMapping("/login")
	private String viewLogin(Model model) {
	    model.addAttribute("user", new User()); // Khởi tạo đối tượng user
	    return "views/login";
	}
	
	@PostMapping("/login")
	public String loginUser(@ModelAttribute("user") User user, Model model, HttpSession session) {
	    try {
	        User loggedInUser = userService.loginUser(user.getEmail(), user.getPassword());
	        session.setAttribute("user", loggedInUser);
	        model.addAttribute("successMessage", "Đăng nhập thành công!");
	      
	        return "views/login"; 
	    } catch (IllegalArgumentException e) {
	        model.addAttribute("errorMessage", e.getMessage());
	        return "views/login"; // Stay on the login page if there's an error
	    }
	}
	
	@GetMapping("/edit-profile")
	public String showEditProfile(Model model, HttpSession session) throws Exception {
	    User user = (User) session.getAttribute("user"); // Lấy thông tin người dùng từ session
	    if (user == null) {
	    	  model.addAttribute("errorMessage", "Vui lòng đăng nhập để sửa thông tin.");
	        throw new Exception("Không tìm thấy thông tin người dùng.");
	    }
	    model.addAttribute("user", user); // Hiển thị thông tin người dùng trên trang edit profile
	    return "views/edit-profile"; // Trả về trang view editProfile
	}


	@PostMapping("/edit-profile")
	public String editProfile(@ModelAttribute("user") User updatedUser, Model model,HttpSession session) {
	    try {
	        userService.updateUserProfile(updatedUser);
	        // Cập nhật session với thông tin mới
	        session.setAttribute("user", updatedUser);
	        model.addAttribute("successMessage", "Cập nhật hồ sơ thành công!");
	    } catch (IllegalArgumentException e) {
	        model.addAttribute("errorMessage", e.getMessage());
	    }
	    model.addAttribute("user", updatedUser);
	    return "views/edit-profile";
	}
	
	
	
	@GetMapping("/change-password")
	public String showChangePasswordPage(Model model, HttpSession session) {
	   
		 System.out.println("Session ID1: " + session.getId());
		 System.out.println("Session created at: " + new java.util.Date(session.getCreationTime()));
		
		User user = (User) session.getAttribute("user");
	    if (user == null) {
	        model.addAttribute("errorMessage", "Vui lòng đăng nhập để đổi mật khẩu.");
	        return "views/login";
	    }
	    
	    System.out.println("User in session: " + user.getName());
	    
	    model.addAttribute("user", user);
	    return "views/change-password";
	}


	@PostMapping("/change-password")
	public String changePassword(
	        @RequestParam("current_password") String currentPassword,
	        @RequestParam("new_password") String newPassword,
	        @RequestParam("new_password_confirmation") String confirmPassword,
	        HttpSession session,
	        Model model) {
	    User user = (User) session.getAttribute("user");
	    if (user == null) {
	        model.addAttribute("errorMessage", "Vui lòng đăng nhập để đổi mật khẩu.");
	        return "views/login";
	    }

	    try {
	        userService.changePassword(user.getId(), currentPassword, newPassword, confirmPassword);
	        
	        // Cập nhật lại thông tin người dùng trong session sau khi thay đổi mật khẩu
	        user.setPassword(newPassword);  // Cập nhật mật khẩu mới
	        session.setAttribute("user", user);
	        
	        // Truyền lại thông tin người dùng để hiển thị tên
	        model.addAttribute("user", user);
	        
	        model.addAttribute("successMessage", "Đổi mật khẩu thành công!");
	    } catch (IllegalArgumentException e) {
	        model.addAttribute("errorMessage", e.getMessage());
	        
	        // Truyền lại đối tượng người dùng vào model nếu có lỗi
	        model.addAttribute("user", user);  // Truyền lại thông tin user trong session
	        
	        return "views/change-password";
	    }

	    return "views/change-password";
	}

	@GetMapping("/list-user")
	public String viewUsers(
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "5") int size,
	        @RequestParam(name = "search", required = false) String search, 
	        Model model, HttpSession session) {
	    
	    Pageable pageable = PageRequest.of(page, size);
	    Page<User> userPage;

	    User user = (User) session.getAttribute("user"); // Lấy thông tin người dùng từ session

	    // Kiểm tra nếu người dùng chưa đăng nhập
	    if (user == null) {
	        model.addAttribute("errorMessage", "Vui lòng đăng nhập để xem danh sách người dùng.");
	        return "views/login"; // Chuyển hướng về trang đăng nhập
	    }

	    // Kiểm tra nếu người dùng không phải admin (role != true)
	    if (!user.isRole()) {
	        model.addAttribute("errorMessage", "Bạn không có quyền xem danh sách người dùng.");
	        return "views/login"; // Chuyển hướng đến trang thông báo lỗi
	    }

	    if (search != null && !search.isEmpty()) {
	        userPage = userService.searchUsers(search, pageable);  // Thêm pageable vào trong Service
	    } else {
	        userPage = userService.findUsersWithRoleZero(pageable);  // Thêm pageable vào trong Service
	    }

	    model.addAttribute("users", userPage.getContent());
	    model.addAttribute("totalPages", userPage.getTotalPages());
	    model.addAttribute("currentPage", userPage.getNumber());
	    model.addAttribute("search", search); // Truyền giá trị tìm kiếm để hiển thị lại trên form
	    return "views/list-user"; // Trả về trang hiển thị danh sách người dùng
	}

	@DeleteMapping("/list-user/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            // Gọi service để xóa người dùng
            userService.deleteUserById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xóa người dùng");
        }
    }
	
	@PostMapping("/logout")
	public String logoutUser(HttpSession session) {
	    session.invalidate(); // Xóa toàn bộ session
	    return "redirect:/list-post"; // Chuyển hướng về trang đăng nhập
	}

	 

}
