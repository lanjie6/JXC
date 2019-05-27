package com.java1234.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

/**
 * 用户实体
 * @author Administrator
 *
 */
@Entity
@Table(name="t_user")
public class User {
	
	@Id
	/*
	 * 如果数据库控制主键自增，不加参数就会报错，MYSQL数据库要确保一下表设置了自增
	 * strategy属性提供四种值:
	 * -AUTO主键由程序控制, 是默认选项 ,不设置就是这个
	 * -IDENTITY 主键由数据库生成, 采用数据库自增长, Oracle不支持这种方式
	 * -SEQUENCE 通过数据库的序列产生主键, MYSQL  不支持
	 * -Table 提供特定的数据库产生主键, 该方式更有利于数据库的移植
	 */
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id; // 编号
	
	@Column(length=50)
	private String userName; // 用户名
	
	@Column(length=50)
	private String password; // 密码
	
	@Column(length=50)
	private String trueName; // 真实姓名
	
	@Column(length=1000)
	private String remarks; // 备注
	
	@Transient//不映射到数据库
	private String roles;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
}
