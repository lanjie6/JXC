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
import com.java1234.entity.ReturnList;
import com.java1234.entity.ReturnListGoods;
import com.java1234.entity.User;
import com.java1234.resultConfig.ResultCode;
import com.java1234.service.LogService;
import com.java1234.service.ReturnListGoodsService;
import com.java1234.service.UserService;

/**
 * 退货商品Controller层
 * @author 兰杰
 *
 */
@RestController
@RequestMapping("/returnListGoods")
public class ReturnListGoodsController{
	
	@Autowired
	private ReturnListGoodsService returnListGoodsService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 保存退货单信息
	 * @param returnList 退货单信息实体
	 * @param returnListGoodsStr 退货商品信息JSON字符串
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="退货出库")
	public Map<String,Object> save(ReturnList returnList,  String returnListGoodsStr){
		
		Map<String,Object> map  = new HashMap<String,Object>();
		
		try {
			// 使用谷歌Gson将JSON字符串数组转换成具体的集合
			Gson gson = new Gson();
			
			List<ReturnListGoods> returnListGoodsList = gson.fromJson(returnListGoodsStr,new TypeToken<List<ReturnListGoods>>(){}.getType());
			
			// 设置当前操作用户
			User currentUser = userService.findUserByName((String)SecurityUtils.getSubject().getPrincipal());
			
			returnList.setUser(currentUser);
			
			// 保存退货清单
			returnListGoodsService.save(returnList, returnListGoodsList);
			
			// 保存日志
			logService.save(new Log(Log.INSERT_ACTION, "新增退货单："+returnList.getReturnNumber()));
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "退货单保存成功");
			
			
		} catch (Exception e) {
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "退货单保存失败");
			
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 查询退货单
	 * @param returnNumber 单号
	 * @param supplierId 供应商ID
	 * @param state 付款状态
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value={"退货单据查询","供应商统计"},logical=Logical.OR)
	public Map<String,Object> list(String returnNumber, String supplierId, String state, String sTime,
			String eTime){
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			
			List<ReturnList> returnListList = returnListGoodsService.getReturnlist(returnNumber, supplierId, state, sTime, eTime);
			
			logService.save(new Log(Log.SELECT_ACTION, "退货单据查询"));
			
			result.put("rows", returnListList);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return result;
	}
	
	/**
	 * 查询退货单商品信息
	 * @param returnListId 退货单ID
	 * @return
	 */
	@RequestMapping("/goodsList")
	@RequiresPermissions(value={"退货单据查询","供应商统计"},logical=Logical.OR)
	public Map<String,Object> goodsList(Integer returnListId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			 List<ReturnListGoods> returnListGoodsList = returnListGoodsService.getReturnListGoodsByReturnListId(returnListId);
			
			 logService.save(new Log(Log.SELECT_ACTION, "退货单商品信息查询"));
			 
			 map.put("rows", returnListGoodsList);
			 
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return map;
	}
	
	/**
	 * 删除退货单及商品信息
	 * @param returnListId 退货单ID
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value="退货单据查询")
	public Map<String,Object> delete(Integer returnListId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			logService.save(new Log(Log.DELETE_ACTION, "删除退货单："+returnListGoodsService.getReturnList(returnListId).getReturnNumber()));
			
			returnListGoodsService.delete(returnListId);
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "删除退货单成功");
			
		} catch (Exception e) {
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "删除退货单失败");
			
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
			
			returnListGoodsService.updateState(1, id);
			
			logService.save(new Log(Log.DELETE_ACTION, "支付结算退货单："+returnListGoodsService.getReturnList(id).getReturnNumber()));
			
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
	 * 进货商品统计
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param goodsTypeId 商品类别ID
	 * @param codeOrName 编号或商品名称
	 * @return
	 */
	@RequestMapping("/count")
	@RequiresPermissions(value="商品采购统计")
	public String count(String sTime, String eTime ,Integer goodsTypeId, String codeOrName){
		
		JsonArray result = new JsonArray();
		
		try {
			
			List<ReturnList> returnListList = returnListGoodsService.getReturnlist(null, null, null, sTime, eTime);
			
			for(ReturnList pl : returnListList){
				
				List<ReturnListGoods> returnListGoodsList = returnListGoodsService
						.getReturnListGoodsByReturnListId(pl.getId(), goodsTypeId, codeOrName);
				
				for(ReturnListGoods pg : returnListGoodsList){
					
					JsonObject obj = new JsonObject();
					
					obj.addProperty("number", pl.getReturnNumber());
					
					obj.addProperty("date", pl.getReturnDate());
					
					obj.addProperty("supplierName", pl.getSupplier().getName());
					
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
			
			logService.save(new Log(Log.SELECT_ACTION, "退货商品统计查询"));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return result.toString();
		
	}

}
