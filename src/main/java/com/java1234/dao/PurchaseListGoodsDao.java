package com.java1234.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.PurchaseListGoods;

/**
 * 进货商品Dao
 * @author 兰杰
 *
 */
public interface PurchaseListGoodsDao extends JpaRepository<PurchaseListGoods, Integer>,JpaSpecificationExecutor<PurchaseListGoods>{
	
	@Query(value="SELECT * FROM t_purchase_list_goods WHERE purchase_list_id=?1",nativeQuery=true)
	public List<PurchaseListGoods> getPurchaseListGoodsByPurchaseListId(Integer purchaseListId);
	
	@Query(value="DELETE FROM t_purchase_list_goods WHERE purchase_list_id=?1",nativeQuery=true)
	@Modifying
	public void deletePurchaseListGoodsByPurchaseListId(Integer purchaseListId);
}
