package com.java1234.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.java1234.entity.CustomerReturnList;
import com.java1234.entity.CustomerReturnListGoods;
import com.java1234.entity.Log;
import com.java1234.entity.User;
import com.java1234.resultConfig.ResultCode;
import com.java1234.service.CustomerReturnListGoodsService;
import com.java1234.service.LogService;
import com.java1234.service.UserService;

/**
 * 客户退货Controller层
 * @author 兰杰
 *
 */
@RestController
@RequestMapping("/customerReturnListGoods")
public class CustomerReturnListGoodsController{
	
	@Autowired
	private CustomerReturnListGoodsService customerReturnListGoodsService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 保存客户退货单信息
	 * @param customerReturnList 客户退货单信息实体
	 * @param customerReturnListGoodsStr 客户退货信息JSON字符串
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="客户退货")
	public Map<String,Object> save(CustomerReturnList customerReturnList,  String customerReturnListGoodsStr){
		
		Map<String,Object> map  = new HashMap<String,Object>();
		
		try {
			// 使用谷歌Gson将JSON字符串数组转换成具体的集合
			Gson gson = new Gson();
			
			List<CustomerReturnListGoods> customerReturnListGoodsList = gson.fromJson(customerReturnListGoodsStr,new TypeToken<List<CustomerReturnListGoods>>(){}.getType());
			
			// 设置当前操作用户
			User currentUser = userService.findUserByName((String)SecurityUtils.getSubject().getPrincipal());
			
			customerReturnList.setUser(currentUser);
			
			// 保存客户退货清单
			customerReturnListGoodsService.save(customerReturnList, customerReturnListGoodsList);
			
			// 保存日志
			logService.save(new Log(Log.INSERT_ACTION, "新增客户退货单："+customerReturnList.getCustomerReturnNumber()));
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "客户退货单保存成功");
			
			
		} catch (Exception e) {
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "客户退货单保存失败");
			
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 查询客户退货单
	 * @param customerReturnNumber 单号
	 * @param customerId 供应商ID
	 * @param state 付款状态
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value={"客户退货查询","客户统计"},logical=Logical.OR)
	public Map<String,Object> list(String customerReturnNumber, String customerId, String state, String sTime,
			String eTime){
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			
			List<CustomerReturnList> customerReturnListList = customerReturnListGoodsService.getCustomerReturnlist(customerReturnNumber, customerId, state, sTime, eTime);
			
			logService.save(new Log(Log.SELECT_ACTION, "客户退货单据查询"));
			
			result.put("rows", customerReturnListList);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return result;
	}
	
	/**
	 * 查询客户退货单商品信息
	 * @param customerReturnListId 客户退货单ID
	 * @return
	 */
	@RequestMapping("/goodsList")
	@RequiresPermissions(value={"客户退货查询","客户统计"},logical=Logical.OR)
	public Map<String,Object> goodsList(Integer customerReturnListId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			 List<CustomerReturnListGoods> customerReturnListGoodsList = customerReturnListGoodsService.getCustomerReturnListGoodsByCustomerReturnListId(customerReturnListId);
			
			 logService.save(new Log(Log.SELECT_ACTION, "客户退货单商品信息查询"));
			 
			 map.put("rows", customerReturnListGoodsList);
			 
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return map;
	}
	
	/**
	 * 删除客户退货单及商品信息
	 * @param customerReturnListId 客户退货单ID
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value="客户退货查询")
	public Map<String,Object> delete(Integer customerReturnListId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			logService.save(new Log(Log.DELETE_ACTION, "删除客户退货单："+customerReturnListGoodsService.getCustomerReturnList(customerReturnListId).getCustomerReturnNumber()));
			
			customerReturnListGoodsService.delete(customerReturnListId);
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "删除客户退货单成功");
			
		} catch (Exception e) {
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "删除客户退货单失败");
			
			e.printStackTrace();
			
		}
		
		return map;
		
	}
	
	/**
	 * 修改进货单付款状态
	 * @param id 进货单ID
	 * @return
	 */
	@RequestMapping("/updateState")
	@RequiresPermissions(value="供应商统计")
	public Map<String,Object> updateState(Integer id){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			customerReturnListGoodsService.updateState(1, id);
			
			logService.save(new Log(Log.DELETE_ACTION, "支付结算客户退货单："+customerReturnListGoodsService.getCustomerReturnList(id).getCustomerReturnNumber()));
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "退货单结算成功");
			
		} catch (Exception e) {
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "退货单结算失败");
			
			e.printStackTrace();
			
		}
		
		return map;
		
	}
	
	/**
	 * 客户退货商品统计
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param goodsTypeId 商品类别ID
	 * @param codeOrName 编号或商品名称
	 * @return
	 */
	@RequestMapping("/count")
	@RequiresPermissions(value="商品销售统计")
	public String count(String sTime, String eTime ,Integer goodsTypeId, String codeOrName){
		
		JsonArray result = new JsonArray();
		
		try {
			
			List<CustomerReturnList> customerReturnListList = customerReturnListGoodsService.getCustomerReturnlist(null, null, null, sTime, eTime);
			
			for(CustomerReturnList pl : customerReturnListList){
				
				List<CustomerReturnListGoods> customerReturnListGoodsList = customerReturnListGoodsService
						.getCustomerReturnListGoodsByCustomerReturnListId(pl.getId(), goodsTypeId, codeOrName);
				
				for(CustomerReturnListGoods pg : customerReturnListGoodsList){
					
					JsonObject obj = new JsonObject();
					
					obj.addProperty("number", pl.getCustomerReturnNumber());
					
					obj.addProperty("date", pl.getCustomerReturnDate());
					
					obj.addProperty("supplierName", pl.getCustomer().getName());
					
					obj.addProperty("code", pg.getCode());
					
					obj.addProperty("name", pg.getName());
					
					obj.addProperty("model", pg.getModel());
					
					obj.addProperty("goodsType", pg.getType().getName());
					
					obj.addProperty("unit", pg.getUnit());
					
					obj.addProperty("price", pg.getPrice());
					
					obj.addProperty("num", pg.getNum());
					
					obj.addProperty("total", pg.getTotal());
					
					result.add(obj);
					
				}
			}
			
			logService.save(new Log(Log.SELECT_ACTION, "客户退货商品统计查询"));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return result.toString();
		
	}

}
