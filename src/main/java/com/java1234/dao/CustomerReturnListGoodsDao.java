package com.java1234.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.CustomerReturnListGoods;

/**
 * 客户退货商品Dao
 * @author 兰杰
 *
 */
public interface CustomerReturnListGoodsDao extends JpaRepository<CustomerReturnListGoods, Integer>,JpaSpecificationExecutor<CustomerReturnListGoods>{
	
	@Query(value="SELECT * FROM t_customer_return_list_goods WHERE customer_return_list_id=?1",nativeQuery=true)
	public List<CustomerReturnListGoods> getCustomerReturnListGoodsByCustomerReturnListId(Integer customerReturnListId);
	
	@Query(value="DELETE FROM t_customer_return_list_goods WHERE customer_return_list_id=?1",nativeQuery=true)
	@Modifying
	public void deleteCustomerReturnListGoodsByCustomerReturnListId(Integer customerReturnListId);
	
	@Query(value="SELECT SUM(num) FROM t_customer_return_list_goods WHERE goods_id=?1",nativeQuery=true)
	public Integer getCustomerReturnTotalByGoodsId(Integer goodsId);
}
