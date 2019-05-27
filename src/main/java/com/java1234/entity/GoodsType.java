package com.java1234.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品类型实体
 * @author 兰杰
 *
 */
@Entity
@Table(name="t_goodsType")
public class GoodsType {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id; // 编号
	
	@Column(length=50)
	private String name; // 商品类型名称
	
	private Integer state; // 商品节点类型 1 根节点 0 叶子节点
	
	private Integer pId; // 父菜单Id

	public GoodsType() {
		super();
	}

	public GoodsType(String name, Integer state, Integer pId) {
		super();
		this.name = name;
		this.state = state;
		this.pId = pId;
	}

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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}
	
}
