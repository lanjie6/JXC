package com.java1234.service;

import java.util.List;

import com.java1234.entity.OverflowList;
import com.java1234.entity.OverflowListGoods;

/**
 * 商品报溢商品service接口
 * @author 兰杰
 *
 */
public interface OverflowListGoodsService {
	
	/**
	 * 保存商品商品报溢清单，商品商品报溢列表，修改商品属性
	 * @param overflowList 商品商品报溢单
	 * @param overflowListGoodsList 商品商品报溢列表
	 */
	public void save(OverflowList overflowList, List<OverflowListGoods> overflowListGoodsList);
	
	/**
	 * 商品报溢单查询
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	public List<OverflowList> getOverflowlist(String sTime, String eTime);
	
	/**
	 * 根据商品报溢单ID查询商品报溢商品信息
	 * @param overflowListId 商品报溢单ID
	 * @return
	 */
	public List<OverflowListGoods> getOverflowListGoodsByOverflowListId(Integer overflowListId);
	
	/**
	 * 根据商品报溢单ID查询商品报溢单信息
	 * @param overflowListId 商品报溢单ID
	 * @return
	 */
	public OverflowList getOverflowList(Integer overflowListId);

}
