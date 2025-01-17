package com.example.webthiennguyen_spring.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.webthiennguyen_spring.entities.User;
import com.example.webthiennguyen_spring.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	
	public List<User> getAllUsers() {
	    return repo.findAll(); // Trả về tất cả người dùng
	}
	 
	
	public User getUser(String name) {
		return repo.getUser(name);
	}
	
	public User getUserByEmail(String email) {
	    return repo.findOptionalByEmail(email)
	               .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng."));
	}

	
	// Thêm chức năng đăng ký
    public User registerUser(User user, String passwordConfirmation) {
    	
        // Kiểm tra xem email đã tồn tại hay chưa
        if (repo.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email đã được sử dụng.");
        }

        // Kiểm tra xem username đã tồn tại hay chưa
        if (repo.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Tên người dùng đã tồn tại.");
        }
        
        // Kiểm tra xem phoneNumber đã tồn tại hay chưa
        if (repo.findByPhoneNumber(user.getPhoneNumber()) != null) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại.");
        }
        
        if (user.getPhoneNumber().length() != 10) {
            throw new IllegalArgumentException("Số điện thoại phải có 10 chữ số.");
        }
                
        // Kiểm tra mật khẩu và xác nhận mật khẩu
        if (!user.getPassword().equals(passwordConfirmation)) {
            throw new IllegalArgumentException("Mật khẩu và xác nhận mật khẩu không khớp.");
        }
        // Lưu người dùng vào cơ sở dữ liệu
        return repo.save(user);
    }
    
    //login  
    public User loginUser(String email, String password) {
    	
    	
        User user = repo.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new IllegalArgumentException("Email hoặc mật khẩu không đúng.");
        }
        return user; 	
    }
    
    //edit-profile
    public void updateUserProfile(User updatedUser) {
        User existingUser = repo.findById(updatedUser.getId())
                                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại."));
        System.out.println("ID nhận được để cập nhật: " + updatedUser.getId());

        if (updatedUser.getName() == null || updatedUser.getName().isEmpty()) {
            throw new IllegalArgumentException("Họ và tên không được để trống.");
        }

        if (updatedUser.getPhoneNumber().length() != 10) {
            throw new IllegalArgumentException("Số điện thoại phải có 10 chữ số.");
        }

        // Cập nhật thông tin
        existingUser.setName(updatedUser.getName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        
        repo.save(existingUser);
    }
    
    
    
    public void changePassword(int userId, String currentPassword, String newPassword, String confirmPassword) {
        User user = repo.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại."));

        // Kiểm tra mật khẩu hiện tại
        if (!user.getPassword().equals(currentPassword)) {
            throw new IllegalArgumentException("Mật khẩu hiện tại không đúng.");
        }

        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 6 ký tự.");
        }
        if (!newPassword.matches(".*\\d.*") || !newPassword.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("Mật khẩu mới phải bao gồm cả chữ và số.");
        }
        
        // Kiểm tra mật khẩu mới và xác nhận mật khẩu
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu không khớp.");
        }

        // Cập nhật mật khẩu
        user.setPassword(newPassword);
        repo.save(user);
         
    }
    
 // Tìm kiếm người dùng theo tên hoặc email
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        return repo.searchByNameOrEmail(keyword, pageable);
    }
    
 // Tìm người dùng có role = false
    public Page<User> findUsersWithRoleZero(Pageable pageable) {
        return repo.findUsersWithRoleZero(pageable);
    }
    
    public void deleteUserById(Integer id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("ID không tồn tại: " + id);
        }
        repo.deleteById(id);
    }
}
