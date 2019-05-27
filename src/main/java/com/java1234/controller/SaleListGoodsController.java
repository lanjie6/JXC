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
import com.java1234.entity.Log;
import com.java1234.entity.SaleList;
import com.java1234.entity.SaleListGoods;
import com.java1234.entity.User;
import com.java1234.resultConfig.ResultCode;
import com.java1234.service.LogService;
import com.java1234.service.SaleListGoodsService;
import com.java1234.service.UserService;
import com.java1234.util.BigDecimalUtil;
import com.java1234.util.DateUtil;

/**
 * 销售商品Controller层
 * @author 兰杰
 *
 */
@RestController
@RequestMapping("/saleListGoods")
public class SaleListGoodsController{
	
	@Autowired
	private SaleListGoodsService saleListGoodsService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 保存销售单信息
	 * @param saleList 销售单信息实体
	 * @param saleListGoodsStr 销售商品信息JSON字符串
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="销售出库")
	public Map<String,Object> save(SaleList saleList,  String saleListGoodsStr){
		
		Map<String,Object> map  = new HashMap<String,Object>();
		
		try {
			// 使用谷歌Gson将JSON字符串数组转换成具体的集合
			Gson gson = new Gson();
			
			List<SaleListGoods> saleListGoodsList = gson.fromJson(saleListGoodsStr,new TypeToken<List<SaleListGoods>>(){}.getType());
			
			// 设置当前操作用户
			User currentUser = userService.findUserByName((String)SecurityUtils.getSubject().getPrincipal());
			
			saleList.setUser(currentUser);
			
			// 保存销售清单
			saleListGoodsService.save(saleList, saleListGoodsList);
			
			// 保存日志
			logService.save(new Log(Log.INSERT_ACTION, "新增销售单："+saleList.getSaleNumber()));
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "销售单保存成功");
			
			
		} catch (Exception e) {
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "销售单保存失败");
			
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 查询销售单
	 * @param saleNumber 单号
	 * @param customerId 供应商ID
	 * @param state 付款状态
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value={"销售单据查询","客户统计"},logical=Logical.OR)
	public Map<String,Object> list(String saleNumber, String customerId, String state, String sTime,
			String eTime){
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			
			List<SaleList> saleListList = saleListGoodsService.getSalelist(saleNumber, customerId, state, sTime, eTime);
			
			logService.save(new Log(Log.SELECT_ACTION, "销售单据查询"));
			
			result.put("rows", saleListList);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return result;
	}
	
	/**
	 * 查询销售单商品信息
	 * @param saleListId 销售单ID
	 * @return
	 */
	@RequestMapping("/goodsList")
	@RequiresPermissions(value={"销售单据查询","客户统计"},logical=Logical.OR)
	public Map<String,Object> goodsList(Integer saleListId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			 List<SaleListGoods> saleListGoodsList = saleListGoodsService.getSaleListGoodsBySaleListId(saleListId);
			
			 logService.save(new Log(Log.SELECT_ACTION, "销售单商品信息查询"));
			 
			 map.put("rows", saleListGoodsList);
			 
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return map;
	}
	
	/**
	 * 删除销售单及商品信息
	 * @param saleListId 销售单ID
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value="销售单据查询")
	public Map<String,Object> delete(Integer saleListId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			logService.save(new Log(Log.DELETE_ACTION, "删除销售单："+saleListGoodsService.getSaleList(saleListId).getSaleNumber()));
			
			saleListGoodsService.delete(saleListId);
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "删除销售单成功");
			
		} catch (Exception e) {
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "删除销售单失败");
			
			e.printStackTrace();
			
		}
		
		return map;
		
	}
	
	/**
	 * 修改销售单付款状态
	 * @param id 销售单ID
	 * @return
	 */
	@RequestMapping("/updateState")
	@RequiresPermissions(value="供应商统计")
	public Map<String,Object> updateState(Integer id){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			saleListGoodsService.updateState(1, id);
			
			logService.save(new Log(Log.DELETE_ACTION, "支付结算销售单："+saleListGoodsService.getSaleList(id).getSaleNumber()));
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "销售单结算成功");
			
		} catch (Exception e) {
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "销售单结算失败");
			
			e.printStackTrace();
			
		}
		
		return map;
		
	}
	
	/**
	 * 销售商品统计
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
			
			List<SaleList> saleListList = saleListGoodsService.getSalelist(null, null, null, sTime, eTime);
			
			for(SaleList pl : saleListList){
				
				List<SaleListGoods> saleListGoodsList = saleListGoodsService
						.getSaleListGoodsBySaleListId(pl.getId(), goodsTypeId, codeOrName);
				
				for(SaleListGoods pg : saleListGoodsList){
					
					JsonObject obj = new JsonObject();
					
					obj.addProperty("number", pl.getSaleNumber());
					
					obj.addProperty("date", pl.getSaleDate());
					
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
			
			logService.save(new Log(Log.SELECT_ACTION, "销售商品统计查询"));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return result.toString();
		
	}
	
	/**
	 * 按日统计销售情况
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	@RequestMapping("/getSaleDataByDay")
	@RequiresPermissions(value="按日统计分析")
	public String getSaleDataByDay(String sTime, String eTime){
		
		JsonArray result = new JsonArray();
		
		try {
			
			// 获取所有的时间段日期
			List<String> dateList = DateUtil.getTimeSlotByDay(sTime, eTime);
			
			// 查询按日统计的数据
			List<Object> obList = saleListGoodsService.getSaleDataByDay(sTime, eTime);
			
			// 按日统计的数据，如果该日期没有数据的话，则不会有显示，我们的需求是，如果该日期没有销售数据的话，也应该显示为0，所以需要进行特殊处理
			for(String date : dateList){
				
				JsonObject obj = new JsonObject();
				
				boolean flag = false;
				
				for(Object o : obList){ //jpa默认会按sql语法查询的字段的先后顺序来将结果集进行封装成一个数组集合
					
					Object[] os = (Object[]) o;
					
					if(os[0].equals(date)){
						
						obj.addProperty("date", os[0].toString()); //日期
						
						obj.addProperty("saleTotal", BigDecimalUtil.keepTwoDecimalPlaces((Double)os[1])); //销售总额
						
						obj.addProperty("purchasingTotal", BigDecimalUtil.keepTwoDecimalPlaces((Double)os[2])); //成本总额
						
						obj.addProperty("profit",BigDecimalUtil.keepTwoDecimalPlaces((Double)os[1]-(Double)os[2])); //利润
						
						flag = true;
						
					}
					
				}
				
				if(!flag){// 如果没有销售数据，那么也需要设置该日的销售数据默认为0
					
					obj.addProperty("date", date); //日期
					
					obj.addProperty("saleTotal", 0); //销售总额
					
					obj.addProperty("purchasingTotal", 0); //成本总额
					
					obj.addProperty("profit",0); //利润
					
				}
				
				result.add(obj);
				
			}
			
			logService.save(new Log(Log.SELECT_ACTION, "查询按日统计分析数据"));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return result.toString();
	}
	
	/**
	 * 按月统计销售情况
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	@RequestMapping("/getSaleDataByMonth")
	@RequiresPermissions(value="按日统计分析")
	public String getSaleDataByMonth(String sTime, String eTime){
		
		JsonArray result = new JsonArray();
		
		try {
			
			// 获取所有的时间段日期
			List<String> dateList = DateUtil.getTimeSlotByMonth(sTime, eTime);
			
			// 查询按日统计的数据
			List<Object> obList = saleListGoodsService.getSaleDataByMonth(sTime, eTime);
			
			// 按日统计的数据，如果该日期没有数据的话，则不会有显示，我们的需求是，如果该日期没有销售数据的话，也应该显示为0，所以需要进行特殊处理
			for(String date : dateList){
				
				JsonObject obj = new JsonObject();
				
				boolean flag = false;
				
				for(Object o : obList){ //jpa默认会按sql语法查询的字段的先后顺序来将结果集进行封装成一个数组集合
					
					Object[] os = (Object[]) o;
					
					if(os[0].equals(date)){
						
						obj.addProperty("date", os[0].toString()); //日期
						
						obj.addProperty("saleTotal", BigDecimalUtil.keepTwoDecimalPlaces((Double)os[1])); //销售总额
						
						obj.addProperty("purchasingTotal", BigDecimalUtil.keepTwoDecimalPlaces((Double)os[2])); //成本总额
						
						obj.addProperty("profit",BigDecimalUtil.keepTwoDecimalPlaces((Double)os[1]-(Double)os[2])); //利润
						
						flag = true;
						
					}
					
				}
				
				if(!flag){// 如果没有销售数据，那么也需要设置该日的销售数据默认为0
					
					obj.addProperty("date", date); //日期
					
					obj.addProperty("saleTotal", 0); //销售总额
					
					obj.addProperty("purchasingTotal", 0); //成本总额
					
					obj.addProperty("profit",0); //利润
					
				}
				
				result.add(obj);
				
			}
			
			logService.save(new Log(Log.SELECT_ACTION, "查询按月统计分析数据"));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return result.toString();
	}

}
