package com.java1234.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.ReturnListGoods;

/**
 * 退货商品Dao
 * @author 兰杰
 *
 */
public interface ReturnListGoodsDao extends JpaRepository<ReturnListGoods, Integer>,JpaSpecificationExecutor<ReturnListGoods>{
	
	@Query(value="SELECT * FROM t_return_list_goods WHERE return_list_id=?1",nativeQuery=true)
	public List<ReturnListGoods> getReturnListGoodsByReturnListId(Integer returnListId);
	
	@Query(value="DELETE FROM t_return_list_goods WHERE return_list_id=?1",nativeQuery=true)
	@Modifying
	public void deleteReturnListGoodsByReturnListId(Integer returnListId);
}
