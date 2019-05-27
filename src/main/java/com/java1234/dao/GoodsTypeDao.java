package com.java1234.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.GoodsType;

/**
 * 商品类别Dao层
 * @author 兰杰 
 *
 */
public interface GoodsTypeDao extends JpaRepository<GoodsType, Integer>{
	
	/**
	 * 根据父商品类别ID查询所有子商品类别
	 * @param parentId 父ID
	 * @return
	 */
	@Query(value="SELECT * FROM t_goods_type WHERE p_id=?1",nativeQuery=true)
	public List<GoodsType> getAllGoodsTypeByParentId(Integer parentId);
	
}
