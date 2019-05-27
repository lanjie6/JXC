package com.java1234.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.Goods;

/**
 * 商品Dao层
 * @author 兰杰 
 *
 */
public interface GoodsDao extends JpaRepository<Goods, Integer>,JpaSpecificationExecutor<Goods>{
	
	/**
	 * 根据商品类别ID查询商品
	 * @param type 商品类别ID
	 * @return
	 */
	@Query(value="SELECT * FROM t_goods WHERE type_id=?1",nativeQuery=true)
	public List<Goods> getGoodsByTypeId(Integer typeId);
	
	/**
	 * 查询当前商品最大的编码号
	 * @return
	 */
	@Query(value="SELECT MAX(CODE) FROM t_goods",nativeQuery=true)
	public String getMaxCode();
	
	/**
	 * 查询当前库存小于最小库存的商品
	 * @return
	 */
	@Query(value="SELECT * FROM t_goods WHERE inventory_quantity<min_num ORDER BY id;",nativeQuery=true)
	public List<Goods> getGoodsAlarm();
	
}
