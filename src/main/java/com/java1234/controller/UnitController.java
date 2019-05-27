package com.java1234.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.java1234.entity.Log;
import com.java1234.entity.Unit;
import com.java1234.resultConfig.ResultCode;
import com.java1234.service.LogService;
import com.java1234.service.UnitService;

/**
 * 商品单位Controller层
 * @author 兰杰
 *
 */
@RestController
@RequestMapping("/unit")
public class UnitController {
	
	@Autowired
	private UnitService unitService;
	
	@Autowired
	private LogService logService;

	/**
	 * 添加或修改商品单位
	 * @param unit 商品单位实体
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	@RequiresPermissions(value="商品管理")
	public Map<String,Object> save(Unit unit){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		logService.save(new Log(Log.INSERT_ACTION,"添加商品单位:"+unit.getName()));
		
		unitService.saveUnit(unit);
		
		map.put("resultCode", ResultCode.SUCCESS);
		
		map.put("resultContent", "保存商品单位成功");
		
		return map;
	}
	
	/**
	 * 删除商品单位
	 * @param unitId 商品单位ID
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions(value="商品管理")
	public Map<String,Object> delete(Integer unitId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			logService.save(new Log(Log.DELETE_ACTION,"删除商品单位:"+unitService.getOne(unitId).getName()));
			
			unitService.delete(unitId);
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "数据删除成功");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "数据删除失败");
			
		}

		return map;
		
	}
	
	/**
	 * 查询所有商品单位
	 * @param unitId 商品单位ID
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	@RequiresPermissions(value="商品管理")
	public Map<String,Object> list(){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<Unit> unitList = unitService.listAll();
		
		map.put("rows", unitList);

		return map;
		
	}
}
