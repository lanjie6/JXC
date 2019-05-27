package com.java1234.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.SaleListGoods;

/**
 * 销售商品Dao
 * @author 兰杰
 *
 */
public interface SaleListGoodsDao extends JpaRepository<SaleListGoods, Integer>,JpaSpecificationExecutor<SaleListGoods>{
	
	@Query(value="SELECT * FROM t_sale_list_goods WHERE sale_list_id=?1",nativeQuery=true)
	public List<SaleListGoods> getSaleListGoodsBySaleListId(Integer saleListId);
	
	@Query(value="DELETE FROM t_sale_list_goods WHERE sale_list_id=?1",nativeQuery=true)
	@Modifying
	public void deleteSaleListGoodsBySaleListId(Integer saleListId);
	
	@Query(value="SELECT SUM(num) FROM t_sale_list_goods WHERE goods_id=?1",nativeQuery=true)
	public Integer getSaleTotalByGoodsId(Integer goodsId);
}
