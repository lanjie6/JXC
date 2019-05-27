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
import com.java1234.entity.OverflowList;
import com.java1234.entity.OverflowListGoods;
import com.java1234.entity.User;
import com.java1234.resultConfig.ResultCode;
import com.java1234.service.LogService;
import com.java1234.service.OverflowListGoodsService;
import com.java1234.service.UserService;

/**
 * 商品报溢商品Controller层
 * @author 兰杰
 *
 */
@RestController
@RequestMapping("/overflowListGoods")
public class OverflowListGoodsController{
	
	@Autowired
	private OverflowListGoodsService overflowListGoodsService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 保存商品报溢单信息
	 * @param overflowList 商品报溢单信息实体
	 * @param overflowListGoodsStr 商品报溢商品信息JSON字符串
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="商品报溢")
	public Map<String,Object> save(OverflowList overflowList,  String overflowListGoodsStr){
		
		Map<String,Object> map  = new HashMap<String,Object>();
		
		try {
			// 使用谷歌Gson将JSON字符串数组转换成具体的集合
			Gson gson = new Gson();
			
			List<OverflowListGoods> overflowListGoodsList = gson.fromJson(overflowListGoodsStr,new TypeToken<List<OverflowListGoods>>(){}.getType());
			
			// 设置当前操作用户
			User currentUser = userService.findUserByName((String)SecurityUtils.getSubject().getPrincipal());
			
			overflowList.setUser(currentUser);
			
			// 保存商品报溢清单
			overflowListGoodsService.save(overflowList, overflowListGoodsList);
			
			// 保存日志
			logService.save(new Log(Log.INSERT_ACTION, "新增商品报溢单："+overflowList.getOverflowNumber()));
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "商品报溢单保存成功");
			
			
		} catch (Exception e) {
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "商品报溢单保存失败");
			
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 查询商品报溢单
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value="报损报溢查询")
	public Map<String,Object> list(String sTime,String eTime){
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			
			List<OverflowList> overflowListList = overflowListGoodsService.getOverflowlist(sTime, eTime);
			
			logService.save(new Log(Log.SELECT_ACTION, "商品报溢单据查询"));
			
			result.put("rows", overflowListList);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return result;
	}
	
	/**
	 * 查询商品报溢单商品信息
	 * @param overflowListId 商品报溢单ID
	 * @return
	 */
	@RequestMapping("/goodsList")
	@RequiresPermissions(value="报损报溢查询")
	public Map<String,Object> goodsList(Integer overflowListId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			 List<OverflowListGoods> overflowListGoodsList = overflowListGoodsService.getOverflowListGoodsByOverflowListId(overflowListId);
			
			 logService.save(new Log(Log.SELECT_ACTION, "商品报溢单商品信息查询"));
			 
			 map.put("rows", overflowListGoodsList);
			 
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return map;
	}

}
