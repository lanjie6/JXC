package com.java1234.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.CustomerReturnList;

/**
 * 客户退货Dao
 * @author 兰杰
 *
 */
public interface CustomerReturnListDao extends JpaRepository<CustomerReturnList, Integer>,JpaSpecificationExecutor<CustomerReturnList>{

	@Query(value="UPDATE t_customer_return_list SET state=?1 WHERE id=?2",nativeQuery=true)
	@Modifying
	public void updateState(Integer state ,Integer id);
}
