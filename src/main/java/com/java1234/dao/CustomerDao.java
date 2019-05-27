package com.java1234.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.java1234.entity.Customer;

/**
 * 客户Dao接口
 * @author 兰杰 2018.11.06
 * @since 1.0
 *
 */
public interface CustomerDao extends JpaRepository<Customer, Integer>,JpaSpecificationExecutor<Customer>{
	
	
}
