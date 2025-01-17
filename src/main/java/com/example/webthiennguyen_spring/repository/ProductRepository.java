package com.example.webthiennguyen_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.webthiennguyen_spring.entities.Products;
import com.example.webthiennguyen_spring.entities.User;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer>{

	@Query("SELECT pro FROM Products pro")
	List<Products> getProducts();
	
	@Query("SELECT c.name AS categoryName, p FROM Products p JOIN p.id_category c WHERE p.isExist = true ORDER BY c.name")
	List<Object[]> findProductsGroupedByCategory();

	@Query("SELECT c.name AS categoryName, p, u.name AS userName " +
		       "FROM Products p " +
		       "JOIN p.id_category c " +
		       "JOIN p.id_user u " +  
		       "WHERE p.isExist = true ORDER BY c.name")
		List<Object[]> findProductsGroupedByCategoryAndUser();

		
	@Query("SELECT p FROM Products p " +
		       "LEFT JOIN FETCH p.id_category c " +
		       "LEFT JOIN FETCH p.id_user u " +
		       "WHERE p.id = :id")
		Products findProductById(@Param("id") Long id);

	
	@Query("SELECT c FROM Category c WHERE c.id = :id")
    Products findProductByIdCategory(@Param("id") Long id);

	@Query("SELECT p FROM Products p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Products> findByNameContainingIgnoreCase(@Param("name") String name);


	@Query("SELECT c.name AS categoryName, p, u.name AS userName, a.ward, a.district, a.province " +
		       "FROM Products p " +
		       "JOIN p.id_category c " +
		       "JOIN p.id_user u " +
		       "JOIN p.id_address a " +  // Tham gia bảng Address
		       "WHERE p.isExist = true " +
		       "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
		       "OR (LOWER(a.ward) LIKE LOWER(CONCAT('%', :searchText, '%')) " +  // Lọc theo ward
		       "OR LOWER(a.district) LIKE LOWER(CONCAT('%', :searchText, '%')) " +  // Lọc theo district
		       "OR LOWER(a.province) LIKE LOWER(CONCAT('%', :searchText, '%')))) " +  // Lọc theo province
		       "ORDER BY c.name")
		List<Object[]> findProductsGroupedByCategoryAndUserAndSearchAndAddress(
		    @Param("searchText") String searchText);
		
	@Query("SELECT c.name AS categoryName, COUNT(p.id) AS productCount " +
		       "FROM Products p " +
		       "JOIN p.id_category c " +
		       "WHERE p.isExist = true " +
		       "GROUP BY c.name " +
		       "ORDER BY c.name")
		List<Object[]> countProductsByCategory();

		
	@Query("SELECT MONTH(p.create_day) AS month, COUNT(p) AS productCount " +
		       "FROM Products p " +
		       "WHERE p.isExist = true AND YEAR(p.create_day) = :year " +
		       "GROUP BY MONTH(p.create_day) " +
		       "ORDER BY month ASC")
		List<Object[]> findProductStatisticsByYear(@Param("year") int year);

		@Query("SELECT p FROM Products p " +
			       "LEFT JOIN FETCH p.id_user u " +
			       "WHERE u.id = :id")
			List<Products> listProductByIdUser(@Param("id") Integer id);
		
		@Query("SELECT p FROM Products p WHERE p.id_user.id = :userId")
		List<Products> findByUserId(@Param("userId") Integer userId);

		@Query("SELECT p FROM Products p " +
			       "WHERE p.isExist = false")
			List<Products> listProductNotApprove();

		
		
}
