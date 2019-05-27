package com.java1234.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品单位实体
 * @author 兰杰
 *
 */
@Entity
@Table(name="t_unit")
public class Unit implements Serializable{

	private static final long serialVersionUID = -5914772517458018928L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id; //编码
	
	@Column(length=10)
	private String name;//单位名称

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

}
