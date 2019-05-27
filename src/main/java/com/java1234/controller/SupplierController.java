package com.java1234.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java1234.entity.Log;
import com.java1234.entity.Supplier;
import com.java1234.resultConfig.ResultCode;
import com.java1234.service.LogService;
import com.java1234.service.SupplierService;

/**
 * 供应商Controller控制器
 * @author 兰杰 2018.11.06
 * @since 1.0
 *
 */
@RestController
@RequestMapping("/supplier")
public class SupplierController {
	
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private LogService logService;
	
	/**
	 * 查询下拉框供应商信息
	 * @param q 供应商名称
	 * @return
	 */
	@RequestMapping("/getComboboxList")
	@RequiresPermissions(value={"进货入库","退货出库","进货单据查询","退货单据查询","供应商统计"},logical=Logical.OR)
	public List<Supplier> getComboboxList(String q){
		
		return supplierService.getComboboxList(q);
	
	}
	
	/**
	 * 分页查询供应商
	 * @param page 当前页数
	 * @param rows 每页显示的记录数
	 * @param name 供应商名
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value="供应商管理")//有供应商管理菜单权限的才给予调用
	public Map<String,Object> list(Integer page,Integer rows,String name){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<Supplier> suppliers = supplierService.getSupplierList(page, rows, name);
		
		logService.save(new Log(Log.SELECT_ACTION,"分页查询供应商"));
		
		map.put("total", supplierService.getSupplierCount(name));
		
		map.put("rows", suppliers);
		
		return map;
	}
	
	/**
	 * 添加或修改供应商
	 * @param supplier 供应商实体
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="供应商管理")
	public Map<String,Object> save(Supplier supplier){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(supplier.getId()==null){
		
			logService.save(new Log(Log.INSERT_ACTION,"添加供应商:"+supplier.getName()));
			
		}else{
			
			logService.save(new Log(Log.UPDATE_ACTION,"修改供应商:"+supplier.getName()));
			
		}
		
		supplierService.saveSupplier(supplier);
		
		map.put("resultCode", ResultCode.SUCCESS);
		
		map.put("resultContent", "保存供应商成功");
		
		return map;
	}
	
	/**
	 * 删除供应商
	 * @param ids 供应商ids字符串，用逗号分隔
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value="供应商管理")
	public Map<String,Object> delete(String ids){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			String[] idArray = ids.split(",");
			
			for(String id : idArray){
				
				logService.save(new Log(Log.DELETE_ACTION,
						"删除供应商:" + supplierService.getSupplierById(Integer.parseInt(id)).getName()));

				supplierService.deleteSupplier(Integer.parseInt(id));
				
			}
		
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "成功删除"+idArray.length+"条数据");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "数据删除失败");
			
		}

		return map;
		
	}

}
