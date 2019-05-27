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
 * 商品报损单实体
 * @author 兰杰
 *
 */
@Entity
@Table(name="t_damageList")
public class DamageList {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id; // 编号
	
	@Column(length=100)
	private String damageNumber; // 商品报损单号
	
	@Column(length=20)
	private String damageDate; // 商品报损日期
	
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

	public String getDamageNumber() {
		return damageNumber;
	}

	public void setDamageNumber(String damageNumber) {
		this.damageNumber = damageNumber;
	}
	
	public String getDamageDate() {
		return damageDate;
	}

	public void setDamageDate(String damageDate) {
		this.damageDate = damageDate;
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
