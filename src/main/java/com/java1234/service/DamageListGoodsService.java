package com.java1234.service;

import java.util.List;

import com.java1234.entity.DamageList;
import com.java1234.entity.DamageListGoods;

/**
 * 商品报损商品service接口
 * @author 兰杰
 *
 */
public interface DamageListGoodsService {
	
	/**
	 * 保存商品商品报损清单，商品商品报损列表，修改商品属性
	 * @param damageList 商品商品报损单
	 * @param damageListGoodsList 商品商品报损列表
	 */
	public void save(DamageList damageList, List<DamageListGoods> damageListGoodsList);
	
	/**
	 * 商品报损单查询
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	public List<DamageList> getDamagelist(String sTime, String eTime);
	
	/**
	 * 根据商品报损单ID查询商品报损商品信息
	 * @param damageListId 商品报损单ID
	 * @return
	 */
	public List<DamageListGoods> getDamageListGoodsByDamageListId(Integer damageListId);
	
	/**
	 * 根据商品报损单ID查询商品报损单信息
	 * @param damageListId 商品报损单ID
	 * @return
	 */
	public DamageList getDamageList(Integer damageListId);

}
