package com.java1234.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 日志实体
 * @author 兰杰 2018.11.1
 * @version 1.0
 *
 */
@Entity
@Table(name="t_log")
public class Log implements Serializable{

	private static final long serialVersionUID = -8744213548180069995L;
	
	public static final String LOGIN_ACTION = "登录操作";
	
	public static final String LOOUT_ACTION = "登出操作";
	
	public static final String SELECT_ACTION = "查询操作";
	
	public static final String INSERT_ACTION = "新增操作";
	
	public static final String UPDATE_ACTION = "修改操作";
	
	public static final String DELETE_ACTION = "删除操作";
	
	public Log() {
		super();
	}

	public Log(String type, String content) {
		super();
		this.type = type;
		this.content = content;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;//编号
	
	@Column(length=50)
	private String type;//操作类型
	
	@Column(length=50)
	private String content;//操作内容
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;//操作人
	
	@Temporal(TemporalType.TIMESTAMP)//设置数据表字段类型为datetime类型
	private Date date;//操作时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


}
