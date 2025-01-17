package com.example.webthiennguyen_spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.webthiennguyen_spring.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByEmail(String email);
    User findByUsername(String username);
    User findByPhoneNumber(String phoneNumber);
    
	@Query("SELECT u FROM User u WHERE u.name = :name")
	User getUser(@Param("name") String name);
	
	Optional<User> findOptionalByEmail(String email);
	Optional<User> findById(int id);
	User findByEmailAndPassword(String email, String password);
	

	@Query("SELECT u FROM User u WHERE u.role = false AND " +
		       "(u.name LIKE %:keyword% OR u.email LIKE %:keyword% OR u.phoneNumber LIKE %:keyword%)")
		Page<User> searchByNameOrEmail(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.role = false")
	Page<User> findUsersWithRoleZero(Pageable pageable);

	 void deleteById(Integer id);

}
