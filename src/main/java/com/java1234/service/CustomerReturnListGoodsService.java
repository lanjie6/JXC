package com.java1234.service;

import java.util.List;

import com.java1234.entity.CustomerReturnList;
import com.java1234.entity.CustomerReturnListGoods;

/**
 * 客户退货商品service接口
 * @author 兰杰
 *
 */
public interface CustomerReturnListGoodsService {
	
	/**
	 * 保存客户退货清单，客户退货列表，修改商品属性
	 * @param customerReturnList 商品客户退货单
	 * @param customerReturnListGoodsList 商品客户退货列表
	 */
	public void save(CustomerReturnList customerReturnList, List<CustomerReturnListGoods> customerReturnListGoodsList);
	
	/**
	 * 客户退货单查询
	 * @param customerReturnNumber 单号
	 * @param customerId 客户ID
	 * @param state 付款状态
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	public List<CustomerReturnList> getCustomerReturnlist(String customerReturnNumber, String customerId, String state, String sTime,
			String eTime);
	
	/**
	 * 根据客户退货单ID查询客户退货商品信息
	 * @param customerReturnListId 客户退货单ID
	 * @return
	 */
	public List<CustomerReturnListGoods> getCustomerReturnListGoodsByCustomerReturnListId(Integer customerReturnListId);
	
	/**
	 * 删除客户退货单及客户退货单商品信息
	 * @param customerReturnListId 客户退货单ID
	 */
	public void delete(Integer customerReturnListId);
	
	/**
	 * 根据客户退货单ID查询客户退货单信息
	 * @param customerReturnListId 客户退货单ID
	 * @return
	 */
	public CustomerReturnList getCustomerReturnList(Integer customerReturnListId);
	
	/**
	 * 根据商品ID查询客户退货商品数量
	 * @param goodsId 商品ID
	 * @return
	 */
	public Integer getCustomerReturnTotalByGoodsId(Integer goodsId);
	
	/**
	 * 修改客户退货单退款状态
	 * @param state 1：已退 2：未退
	 * @param id 退换单ID
	 */
	public void updateState(Integer state ,Integer id);
	
	/**
	 * 根据客户退货单ID条件查询客户退货商品信息
	 * @param customerReturnListId 客户退货单ID
	 * @param goodsTypeId 商品类别ID
	 * @param codeOrName 商品编码或名称
	 * @return 
	 */
	public List<CustomerReturnListGoods> getCustomerReturnListGoodsByCustomerReturnListId(Integer customerReturnListId, Integer goodsTypeId, String codeOrName);

}
