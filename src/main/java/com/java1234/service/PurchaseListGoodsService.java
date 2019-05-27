package com.java1234.service;

import java.util.List;

import com.java1234.entity.PurchaseList;
import com.java1234.entity.PurchaseListGoods;

/**
 * 进货商品service接口
 * @author 兰杰
 *
 */
public interface PurchaseListGoodsService {
	
	/**
	 * 保存商品进货清单，商品进货列表，修改商品属性
	 * @param purchaseList 商品进货单
	 * @param purchaseListGoodsList 商品进货列表
	 */
	public void save(PurchaseList purchaseList, List<PurchaseListGoods> purchaseListGoodsList);
	
	/**
	 * 进货单查询
	 * @param purchaseNumber 单号
	 * @param supplierId 供应商ID
	 * @param state 付款状态
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	public List<PurchaseList> getPurchaselist(String purchaseNumber, String supplierId, String state, String sTime,
			String eTime);
	
	/**
	 * 根据进货单ID查询进货商品信息
	 * @param purchaseListId 进货单ID
	 * @return
	 */
	public List<PurchaseListGoods> getPurchaseListGoodsByPurchaseListId(Integer purchaseListId);
	
	/**
	 * 删除进货单及进货单商品信息
	 * @param purchaseListId 进货单ID
	 */
	public void delete(Integer purchaseListId);
	
	/**
	 * 根据进货单ID查询进货单信息
	 * @param purchaseListId 进货单ID
	 * @return
	 */
	public PurchaseList getPurchaseList(Integer purchaseListId);
	
	/**
	 * 修改进货单付款状态
	 * @param state 1：已付 2：未付
	 * @param id 付款单ID
	 */
    public void updateState(Integer state ,Integer id);
    
    /**
	 * 根据进货单ID条件查询进货商品信息
	 * @param purchaseListId 进货单ID
	 * @param goodsTypeId 商品类别ID
	 * @param codeOrName 商品编码或名称
	 * @return 
	 */
	public List<PurchaseListGoods> getPurchaseListGoodsByPurchaseListId(Integer purchaseListId, Integer goodsTypeId, String codeOrName);

}
