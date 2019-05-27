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
 * 商品报溢单实体
 * @author 兰杰
 *
 */
@Entity
@Table(name="t_overflowList")
public class OverflowList {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id; // 编号
	
	@Column(length=100)
	private String overflowNumber; // 商品报溢单号
	
	@Column(length=20)
	private String overflowDate; // 商品报溢日期
	
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

	public String getOverflowNumber() {
		return overflowNumber;
	}

	public void setOverflowNumber(String overflowNumber) {
		this.overflowNumber = overflowNumber;
	}
	
	public String getOverflowDate() {
		return overflowDate;
	}

	public void setOverflowDate(String overflowDate) {
		this.overflowDate = overflowDate;
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
