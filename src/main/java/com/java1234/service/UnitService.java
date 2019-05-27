package com.java1234.service;

import java.util.List;

import com.java1234.entity.Unit;

/**
 * 商品单位ervice接口
 * @author 兰杰
 *
 */
public interface UnitService {
	
	/**
	 * 新增商品单位
	 * @param unit 商品单位实体
	 */
	public void saveUnit(Unit unit);
	
	/**
	 * 删除商品单位
	 * @param id 商品单位ID
	 */
	public void delete(Integer id);
	
	/**
	 * 查询所有单位
	 * @return
	 */
	public List<Unit> listAll();
	
	/**
	 * 根据商品单位ID查询商品单位信息
	 * @param id 商品单位ID
	 * @return
	 */
	public Unit getOne(Integer id);
}
