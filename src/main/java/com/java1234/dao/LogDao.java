package com.java1234.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.java1234.entity.Log;

/**
 * 日志Dao接口
 * @author 兰杰 2018.11.1
 * @since 1.0
 *
 */
public interface LogDao extends JpaRepository<Log, Integer>,JpaSpecificationExecutor<Log>{

}
