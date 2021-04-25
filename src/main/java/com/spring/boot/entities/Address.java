package com.spring.boot.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Address {
	
	@Id
	private Integer id;
	private String city;
	private String dbPath;
	
	
	
	public Address(Integer id, String city, String dbPath) {
		super();
		this.id = id;
		this.city = city;
		this.dbPath = dbPath;
	}
	public String getDbPath() {
		return dbPath;
	}
	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}
	@Override
	public String toString() {
		return "Address [id=" + id + ", city=" + city + ", dbPath=" + dbPath + "]";
	}
	public Address(Integer id, String city) {
		super();
		this.id = id;
		this.city = city;
	}
	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
	

}
