package com.example.webthiennguyen_spring.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;


@Entity
public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	
		
	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id") 
	private User id_user; 
	
	@ManyToOne
	@JoinColumn(name = "id_product", nullable = false, referencedColumnName = "id")
	private Products id_product;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getId_user() {
		return id_user;
	}

	public void setId_user(User id_user) {
		this.id_user = id_user;
	}

	public Products getId_product() {
		return id_product;
	}

	public void setId_product(Products id_product) {
		this.id_product = id_product;
	} 
	
	
}
