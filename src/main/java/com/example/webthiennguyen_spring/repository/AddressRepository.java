package com.example.webthiennguyen_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.webthiennguyen_spring.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

	// Lấy danh sách tỉnh (distinct) từ bảng Address
    @Query("SELECT DISTINCT a.province FROM Address a")
    List<String> findDistinctProvinces();  // Thay vì trả về List<Address>, trả về List<String> chỉ chứa tên tỉnh.

    // Lấy các quận theo tỉnh
    @Query("SELECT a FROM Address a WHERE a.province = :province")
    List<Address> findByProvince(String province);

    // Lấy các phường theo quận
    @Query("SELECT a FROM Address a WHERE a.district = :district")
    List<Address> findByDistrict(String district);

}
