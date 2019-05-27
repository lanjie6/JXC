package com.java1234.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java1234.entity.Log;
import com.java1234.entity.Role;
import com.java1234.entity.RoleMenu;
import com.java1234.resultConfig.ResultCode;
import com.java1234.service.LogService;
import com.java1234.service.MenuService;
import com.java1234.service.RoleMenuService;
import com.java1234.service.RoleService;
import com.java1234.service.UserRoleService;

/**
 * 角色Controller控制器
 * @author 兰杰 2018.10.19
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RoleMenuService roleMenuService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private LogService logService;
	
	/**
	 * 保存用户登录时所选择的角色
	 * @param roleId
	 * @param session
	 * @return
	 */
	@RequestMapping("/saveRole")
	public Map<String,Object> saveRole(Integer roleId,HttpSession session){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		Role role = roleService.getRoleById(roleId);
		
		//将用户选择的角色信息放入缓存，以便以后加载权限的时候使用
		session.setAttribute("currentRole", role);
		
		map.put("resultCode", ResultCode.SUCCESS);
		
		map.put("resultContent", "保存当前登录用户所选角色信息成功");
		
		return map;
	}
	
	/**
	 * 查询所有角色信息
	 * @return
	 */
	@RequestMapping("/listAll")
	//这里想表达的意思是当前用户拥有用户管理菜单权限或是角色管理菜单权限就可以访问该方法
	//但hiro的该注解默认会把该逻辑表达成'与'的关系，所以我们需要手动指定逻辑关系为'或'
	@RequiresPermissions(value={"用户管理","角色管理"},logical=Logical.OR)
	public Map<String,Object> listAll(){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<Role> roleList = roleService.findAll();
		
		logService.save(new Log(Log.SELECT_ACTION, "查询所有角色信息"));
		
		map.put("rows", roleList);
		
		return map;
	}
	
	/**
	 * 分页查询角色信息
	 * @param page 当前页数
	 * @param rows 每页显示的记录数
	 * @param roleName 角色名
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value="角色管理")
	public Map<String,Object> list(Integer page,Integer rows,String roleName){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<Role> roles = roleService.getRoleList(page, rows, roleName);
		
		logService.save(new Log(Log.SELECT_ACTION, "分页查询角色信息"));
		
		map.put("total", roleService.getRoleCount(roleName));
		
		map.put("rows", roles);
		
		return map;
	}
	
	/**
	 * 添加或修改角色信息
	 * @param role 角色信息实体
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="角色管理")
	public Map<String,Object> save(Role role){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		//角色ID为空时，说明是新增操作，需要先判断角色名是否存在
		if(role.getId()==null){
			
			Role exRole = roleService.findRoleByName(role.getName());
			
			if(exRole!=null){
				
				map.put("resultCode", ResultCode.FAIL);
				
				map.put("resultContent", "角色名已存在");
				
				return map;
				
			}
			
			logService.save(new Log(Log.INSERT_ACTION, "新增角色:"+role.getName()));
			
		}else{
			
			logService.save(new Log(Log.UPDATE_ACTION, "修改角色:"+role.getName()));
			
		}
	
		roleService.saveRole(role);
		
		map.put("resultCode", ResultCode.SUCCESS);
		
		map.put("resultContent", "保存角色信息成功");
		
		return map;
	}
	
	/**
	 * 删除用户信息
	 * @param userId 用户ID
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value="角色管理")
	public Map<String,Object> delete(Integer roleId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			roleMenuService.deleteRoleMenuByRoleId(roleId);
			
			userRoleService.deleteUserRoleByRoleId(roleId);
			
			logService.save(new Log(Log.DELETE_ACTION,"删除角色:"+roleService.getRoleById(roleId).getName()));
			
			roleService.deleteRole(roleId);
			
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
	 * 设置菜单权限
	 * @param roleId 角色ID
	 * @param menus 菜单数组字符串，用逗号号分割
	 * @return
	 */
	@RequestMapping("/setMenu")
	@RequiresPermissions(value="角色管理")
	public Map<String,Object> setMenu(Integer roleId,String menus){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			//先删除当前角色的所有菜单
			roleMenuService.deleteRoleMenuByRoleId(roleId);
			
			//再赋予当前角色新的菜单
			String[] menuArray = menus.split(",");
			
			for(String str : menuArray){
				
				RoleMenu rm = new RoleMenu();
				
				rm.setRole(roleService.getRoleById(roleId));
				
				rm.setMenu(menuService.getMenuById(Integer.parseInt(str)));
				
				roleMenuService.save(rm);
				
			}
			
			logService.save(new Log(Log.UPDATE_ACTION,"设置"+roleService.getRoleById(roleId).getName()+"角色的菜单权限"));
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "设置菜单权限成功");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "设置菜单权限失败");
		}
		
		return map;
	}

}
