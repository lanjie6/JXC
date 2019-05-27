package com.java1234.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 供应商实体
 * @author 兰杰 2018.11.06
 *
 */
@Entity
@Table(name="t_supplier")
public class Supplier implements Serializable{

	private static final long serialVersionUID = 7499099396057566715L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;//编号
	
	@Column(length=100)
	private String name;//供应商名称
	
	@Column(length=50)
	private String contacts;//联系人
	
	@Column(length=50)
	private String phoneNumber;//联系人电话
	
	@Column(length=200)
	private String address;//供应商地址
	
	@Column(length=500)
	private String remarks;//备注

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	

}
