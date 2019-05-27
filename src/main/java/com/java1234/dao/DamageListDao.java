package com.java1234.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.java1234.entity.DamageList;

/**
 * 商品报损单Dao
 * @author 兰杰
 *
 */
public interface DamageListDao extends JpaRepository<DamageList, Integer>,JpaSpecificationExecutor<DamageList>{

}
