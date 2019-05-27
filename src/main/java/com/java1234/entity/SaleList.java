package com.java1234.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 销售单实体
 * @author 兰杰
 *
 */
@Entity
@Table(name="t_saleList")
public class SaleList {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id; // 编号
	
	@Column(length=100)
	private String saleNumber; // 销售单号
	
	@ManyToOne
	@JoinColumn(name="customerId")
	private Customer customer; // 供应商
	
	@Column(length=20)
	private String saleDate; // 销售日期
	
	private float amountPayable; // 应付金额
	
	private float amountPaid; // 实付金额
	
	private Integer state; // 交易状态 1 已付 2 未付
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user; // 操作用户
	
	@Column(length=1000)
	private String remarks; // 备注

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSaleNumber() {
		return saleNumber;
	}

	public void setSaleNumber(String saleNumber) {
		this.saleNumber = saleNumber;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public String getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}

	public float getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(float amountPayable) {
		this.amountPayable = amountPayable;
	}

	public float getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(float amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
