package com.example.webthiennguyen_spring.entities;

import java.util.Date;
import java.util.Arrays;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
@Entity
public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", nullable = false)
	private String name;
	

	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "price", nullable = false)
	private int price;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	
	@ManyToOne
	@JoinColumn(name = "id_category", nullable = false, referencedColumnName = "id") 
	private Category id_category;
	
	@Column(name = "create_day", nullable = false)
	private  Date create_day;
		
	@ManyToOne
	@JoinColumn(name = "id_address", nullable = false, referencedColumnName = "id") 
	private Address id_address;
	

	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id") 
	private User id_user;
	
	@Column(name = "img", nullable = false, columnDefinition = "TEXT")
	private String imgPaths;
	
	@Column(name = "isExist", nullable = false)
	private boolean isExist;
	
	@Transient
	private String[] images;

	public String getImgPaths() {
		return imgPaths;
	}

	public void setImgPaths(String imgPaths) {
		this.imgPaths = imgPaths;
	}
	
	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Category getId_category() {
		return id_category;
	}

	public void setId_category(Category id_category) {
		this.id_category = id_category;
	}

	public Date getCreate_day() {
		return create_day;
	}

	public void setCreate_day(Date create_day) {
		this.create_day = create_day;
	}

	public Address getId_address() {
		return id_address;
	}

	public void setId_address(Address id_address) {
		this.id_address = id_address;
	}

	public User getId_user() {
		return id_user;
	}

	public void setId_user(User id_user) {
		this.id_user = id_user;
	}

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

	

	

}
