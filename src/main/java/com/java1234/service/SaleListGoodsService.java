package com.java1234.service;

import java.util.List;

import com.java1234.entity.SaleList;
import com.java1234.entity.SaleListGoods;

/**
 * 销售商品service接口
 * @author 兰杰
 *
 */
public interface SaleListGoodsService {
	
	/**
	 * 保存商品销售清单，商品销售列表，修改商品属性
	 * @param saleList 商品销售单
	 * @param saleListGoodsList 商品销售列表
	 */
	public void save(SaleList saleList, List<SaleListGoods> saleListGoodsList);
	
	/**
	 * 销售单查询
	 * @param saleNumber 单号
	 * @param customerId 客户ID
	 * @param state 付款状态
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	public List<SaleList> getSalelist(String saleNumber, String customerId, String state, String sTime,
			String eTime);
	
	/**
	 * 根据销售单ID查询销售商品信息
	 * @param saleListId 销售单ID
	 * @return
	 */
	public List<SaleListGoods> getSaleListGoodsBySaleListId(Integer saleListId);
	
	/**
	 * 删除销售单及销售单商品信息
	 * @param saleListId 销售单ID
	 */
	public void delete(Integer saleListId);
	
	/**
	 * 根据销售单ID查询销售单信息
	 * @param saleListId 销售单ID
	 * @return
	 */
	public SaleList getSaleList(Integer saleListId);
	
	/**
	 * 根据商品ID查询销售的商品数量
	 * @param goodsId 商品ID
	 * @return
	 */
	public Integer getSaleTotalByGoodsId(Integer goodsId);
	
	/**
	 * 修改销售单付款状态
	 * @param state 1：已付 2：未付
	 * @param id 销售单ID
	 */
	public void updateState(Integer state ,Integer id);
	
	/**
	 * 根据销售单ID条件查询销售商品信息
	 * @param saleListId 销售单ID
	 * @param goodsTypeId 商品类别ID
	 * @param codeOrName 商品编码或名称
	 * @return 
	 */
	public List<SaleListGoods> getSaleListGoodsBySaleListId(Integer saleListId, Integer goodsTypeId, String codeOrName);
	
	/**
	 * 按日统计销售情况
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	public List<Object> getSaleDataByDay(String sTime,String eTime);
	
	/**
	 * 按月统计销售情况
	 * @param sTime
	 * @param eTime
	 * @return
	 */
	public List<Object> getSaleDataByMonth(String sTime,String eTime);

}
