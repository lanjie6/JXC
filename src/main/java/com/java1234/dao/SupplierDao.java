package com.java1234.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.java1234.entity.Supplier;

/**
 * 供应商Dao接口
 * @author 兰杰 2018.11.06
 * @since 1.0
 *
 */
public interface SupplierDao extends JpaRepository<Supplier, Integer>,JpaSpecificationExecutor<Supplier>{
	
	
}
