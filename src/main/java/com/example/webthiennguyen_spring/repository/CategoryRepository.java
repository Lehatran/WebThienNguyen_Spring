package com.example.webthiennguyen_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.webthiennguyen_spring.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
