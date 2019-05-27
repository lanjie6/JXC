package com.java1234.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.java1234.entity.OverflowList;

/**
 * 商品报溢单Dao
 * @author 兰杰
 *
 */
public interface OverflowListDao extends JpaRepository<OverflowList, Integer>,JpaSpecificationExecutor<OverflowList>{

}
