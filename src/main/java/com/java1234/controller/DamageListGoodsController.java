package com.java1234.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.java1234.entity.Log;
import com.java1234.entity.DamageList;
import com.java1234.entity.DamageListGoods;
import com.java1234.entity.User;
import com.java1234.resultConfig.ResultCode;
import com.java1234.service.LogService;
import com.java1234.service.DamageListGoodsService;
import com.java1234.service.UserService;

/**
 * 商品报损商品Controller层
 * @author 兰杰
 *
 */
@RestController
@RequestMapping("/damageListGoods")
public class DamageListGoodsController{
	
	@Autowired
	private DamageListGoodsService damageListGoodsService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 保存商品报损单信息
	 * @param damageList 商品报损单信息实体
	 * @param damageListGoodsStr 商品报损商品信息JSON字符串
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="商品报损")
	public Map<String,Object> save(DamageList damageList,  String damageListGoodsStr){
		
		Map<String,Object> map  = new HashMap<String,Object>();
		
		try {
			// 使用谷歌Gson将JSON字符串数组转换成具体的集合
			Gson gson = new Gson();
			
			List<DamageListGoods> damageListGoodsList = gson.fromJson(damageListGoodsStr,new TypeToken<List<DamageListGoods>>(){}.getType());
			
			// 设置当前操作用户
			User currentUser = userService.findUserByName((String)SecurityUtils.getSubject().getPrincipal());
			
			damageList.setUser(currentUser);
			
			// 保存商品报损清单
			damageListGoodsService.save(damageList, damageListGoodsList);
			
			// 保存日志
			logService.save(new Log(Log.INSERT_ACTION, "新增商品报损单："+damageList.getDamageNumber()));
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "商品报损单保存成功");
			
			
		} catch (Exception e) {
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "商品报损单保存失败");
			
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 查询商品报损单
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value="报损报溢查询")
	public Map<String,Object> list(String sTime,String eTime){
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			
			List<DamageList> damageListList = damageListGoodsService.getDamagelist(sTime, eTime);
			
			logService.save(new Log(Log.SELECT_ACTION, "商品报损单据查询"));
			
			result.put("rows", damageListList);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return result;
	}
	
	/**
	 * 查询商品报损单商品信息
	 * @param damageListId 商品报损单ID
	 * @return
	 */
	@RequestMapping("/goodsList")
	@RequiresPermissions(value="报损报溢查询")
	public Map<String,Object> goodsList(Integer damageListId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			 List<DamageListGoods> damageListGoodsList = damageListGoodsService.getDamageListGoodsByDamageListId(damageListId);
			
			 logService.save(new Log(Log.SELECT_ACTION, "商品报损单商品信息查询"));
			 
			 map.put("rows", damageListGoodsList);
			 
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return map;
	}

}
