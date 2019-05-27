package com.java1234.service;

import java.util.List;

import com.java1234.entity.ReturnList;
import com.java1234.entity.ReturnListGoods;

/**
 * 退货商品service接口
 * @author 兰杰
 *
 */
public interface ReturnListGoodsService {
	
	/**
	 * 保存商品退货清单，商品退货列表，修改商品属性
	 * @param returnList 商品退货单
	 * @param returnListGoodsList 商品退货列表
	 */
	public void save(ReturnList returnList, List<ReturnListGoods> returnListGoodsList);
	
	/**
	 * 退货单查询
	 * @param returnNumber 单号
	 * @param supplierId 供应商ID
	 * @param state 付款状态
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	public List<ReturnList> getReturnlist(String returnNumber, String supplierId, String state, String sTime,
			String eTime);
	
	/**
	 * 根据退货单ID查询退货商品信息
	 * @param returnListId 退货单ID
	 * @return
	 */
	public List<ReturnListGoods> getReturnListGoodsByReturnListId(Integer returnListId);
	
	/**
	 * 删除退货单及退货单商品信息
	 * @param returnListId 退货单ID
	 */
	public void delete(Integer returnListId);
	
	/**
	 * 根据退货单ID查询退货单信息
	 * @param returnListId 退货单ID
	 * @return
	 */
	public ReturnList getReturnList(Integer returnListId);
	
	/**
	 * 修改进货单付款状态
	 * @param state 1：已退 2：未退
	 * @param id 付款单ID
	 */
    public void updateState(Integer state ,Integer id);
    
    /**
   	 * 根据退货单ID条件查询进货商品信息
   	 * @param returnListId 退货单ID
   	 * @param goodsTypeId 商品类别ID
   	 * @param codeOrName 商品编码或名称
   	 * @return 
   	 */
   	public List<ReturnListGoods> getReturnListGoodsByReturnListId(Integer returnListId, Integer goodsTypeId, String codeOrName);

}
