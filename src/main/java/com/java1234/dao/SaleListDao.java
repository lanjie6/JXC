package com.java1234.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.SaleList;

/**
 * 销售单Dao
 * @author 兰杰
 *
 */
public interface SaleListDao extends JpaRepository<SaleList, Integer>,JpaSpecificationExecutor<SaleList>{

	@Query(value="UPDATE t_sale_list SET state=?1 WHERE id=?2",nativeQuery=true)
	@Modifying
	public void updateState(Integer state ,Integer id);
	
	@Query(value = "SELECT t2.sale_date AS DATE ,SUM(t1.num*t1.price) AS saleTotal ,SUM(t1.num*t3.purchasing_price) AS purchasingTotal  "
			+ "FROM t_sale_list_goods t1 LEFT JOIN t_sale_list t2 ON t1.sale_list_id=t2.id LEFT JOIN t_goods t3 ON t1.goods_id=t3.id "
			+ "WHERE t2.sale_date BETWEEN ?1 AND ?2 GROUP BY t2.sale_date", nativeQuery = true)
	public List<Object> getSaleDataByDay(String sTime,String eTime);
	
	@Query(value = "SELECT DATE_FORMAT(t2.sale_date,'%Y-%m') AS DATE ,SUM(t1.num*t1.price) AS saleTotal ,SUM(t1.num*t3.purchasing_price) AS purchasingTotal  "
			+ "FROM t_sale_list_goods t1 LEFT JOIN t_sale_list t2 ON t1.sale_list_id=t2.id LEFT JOIN t_goods t3 ON t1.goods_id=t3.id "
			+ "WHERE DATE_FORMAT(t2.sale_date,'%Y-%m') BETWEEN ?1 AND ?2 GROUP BY DATE_FORMAT(t2.sale_date,'%Y-%m');", nativeQuery = true)
	public List<Object> getSaleDataByMonth(String sTime,String eTime);
}
