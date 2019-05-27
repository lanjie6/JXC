package com.java1234.service;

import java.util.List;

import com.java1234.entity.GoodsType;

/**
 * 商品类别Service接口
 * @author 兰杰
 *
 */
public interface GoodsTypeService {
	
	/**
	 * 根据父商品类别ID查询所有子商品类别
	 * @param parentId 父ID
	 * @return
	 */
	public List<GoodsType> getAllGoodsTypeByParentId(Integer parentId);
	
	/**
	 * 新增商品类别
	 * @param goodsType 商品类别实体
	 */
	public void saveGoodsType(GoodsType goodsType);
	
	/**
	 * 根据商品类别ID查询商品类别信息
	 * @param id 商品类别ID
	 * @return
	 */
	public GoodsType getGoodsTypeById(Integer id);
	
	/**
	 * 删除商品类别
	 * @param id 商品类别ID
	 */
	public void delete(Integer id);
}
