package com.example.webthiennguyen_spring.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
@Entity
public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", nullable = true)
	private String name;
	

	@Column(name = "description", nullable = true)
	private String description;
	
	@Column(name = "price", nullable = true)
	private int price;
	
	@Column(name = "status", nullable = true)
	private int status;
	
	//@Column(name = "id_category", nullable = true)
	@ManyToOne
	@JoinColumn(name = "id_category", nullable = false, referencedColumnName = "id") 
	private Category id_category;
	
	@Column(name = "create_day", nullable = true)
	private  Date create_day;
	

	//@Column(name = "id_address", nullable = true)
	@ManyToOne
	@JoinColumn(name = "id_address", nullable = false, referencedColumnName = "id") 
	private Address id_address;
	
//	@Column(name = "id_user", nullable = true)
	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id") 
	private User id_user;
	
	@Column(name = "img", nullable = true)
	private String img;
	
	@Column(name = "isExist", nullable = true)
	private boolean isExist;
	

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

	

}
