package com.java1234.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java1234.entity.Unit;

/**
 * 商品单位Dao层
 * @author 兰杰 
 *
 */
public interface UnitDao extends JpaRepository<Unit, Integer>{
	
}
