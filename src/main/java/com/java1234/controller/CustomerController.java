package com.java1234.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java1234.entity.Customer;
import com.java1234.entity.Log;
import com.java1234.resultConfig.ResultCode;
import com.java1234.service.CustomerService;
import com.java1234.service.LogService;

/**
 * 客户Controller控制器
 * @author 兰杰 2018.11.06
 * @since 1.0
 *
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private LogService logService;
	
	/**
	 * 查询下拉框客户信息
	 * @param q 客户名称
	 * @return
	 */
	@RequestMapping("/getComboboxList")
	@RequiresPermissions(value={"销售出库","客户退货","销售单据查询","客户退货查询","客户统计"},logical=Logical.OR)
	public List<Customer> getComboboxList(String q){
		
		return customerService.getComboboxList(q);
	
	}
	
	/**
	 * 分页查询客户
	 * @param page 当前页数
	 * @param rows 每页显示的记录数
	 * @param name 客户名
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value="客户管理")//有客户管理菜单权限的才给予调用
	public Map<String,Object> list(Integer page,Integer rows,String name){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<Customer> customers = customerService.getCustomerList(page, rows, name);
		
		logService.save(new Log(Log.SELECT_ACTION,"分页查询客户"));
		
		map.put("total", customerService.getCustomerCount(name));
		
		map.put("rows", customers);
		
		return map;
	}
	
	/**
	 * 添加或修改客户
	 * @param customer 客户实体
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="客户管理")
	public Map<String,Object> save(Customer customer){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(customer.getId()==null){
		
			logService.save(new Log(Log.INSERT_ACTION,"添加客户:"+customer.getName()));
			
		}else{
			
			logService.save(new Log(Log.UPDATE_ACTION,"修改客户:"+customer.getName()));
			
		}
		
		customerService.saveCustomer(customer);
		
		map.put("resultCode", ResultCode.SUCCESS);
		
		map.put("resultContent", "保存客户成功");
		
		return map;
	}
	
	/**
	 * 删除客户
	 * @param ids 客户ids字符串，用逗号分隔
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value="客户管理")
	public Map<String,Object> delete(String ids){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			String[] idArray = ids.split(",");
			
			for(String id : idArray){
				
				logService.save(new Log(Log.DELETE_ACTION,
						"删除客户:" + customerService.getCustomerById(Integer.parseInt(id)).getName()));

				customerService.deleteCustomer(Integer.parseInt(id));
				
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
