package com.java1234.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.java1234.entity.Log;
import com.java1234.entity.Menu;
import com.java1234.entity.Role;
import com.java1234.service.LogService;
import com.java1234.service.MenuService;

/**
 * 菜单控制器
 * @author 兰杰 2018.10.23
 *
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private LogService logService;
	
	/**
	 * 查询当前角色的导航菜单
	 * @param session 用户从缓冲中取出当前的登录角色
	 * @return easyui要求的JSON格式字符串
	 */
	@RequestMapping("/loadMenu")
	public String loadMenu(HttpSession session){
		
		//获取登录中的角色
		Role currentRole = (Role) session.getAttribute("currentRole");
		
		return this.getAllMenu(-1,currentRole.getId()).toString(); //根节点默认从-1开始
	}
	
	/**
	 * 递归查询当前角色下的所有菜单
	 * @return
	 */
	public JsonArray getAllMenu(Integer parentId,Integer roleId){
		
		JsonArray array = this.getMenuByParentId(parentId, roleId);
		
		for(int i=0;i<array.size();i++){
			
			JsonObject obj = (JsonObject) array.get(i);
			
			if(obj.get("state").getAsString().equals("open")){//如果是叶子节点，不再递归
				
				continue;
				
			}else{//如果是根节点，继续递归查询
				
				obj.add("children", this.getAllMenu(obj.get("id").getAsInt(),roleId));
				
			}
			
		}
		
		return array;
	}
	
	/**
	 * 根据父菜单ID获取菜单
	 * @return
	 */
	public JsonArray getMenuByParentId(Integer parentId,Integer roleId){
		
		JsonArray array = new JsonArray();
		
		List<Menu> menus = menuService.getMenuByParentId(parentId, roleId);
		
		//遍历菜单
		for(Menu menu : menus){
			
			JsonObject obj = new JsonObject();
			
			obj.addProperty("id", menu.getId());//菜单ID
			
			obj.addProperty("text", menu.getName());//菜单名称
			
			obj.addProperty("iconCls", menu.getIcon());//图标
			
			if(menu.getState()==1){
				
				obj.addProperty("state", "closed"); //根节点
				
			}else{
				
				obj.addProperty("state", "open");//叶子节点
			}
			
			JsonObject attributes = new JsonObject(); //扩展属性
			
			attributes.addProperty("url", menu.getUrl());
			
			obj.add("attributes", attributes);
			
			array.add(obj);
			
				
		}
		
		return array;
	}
	
	/**
	 * 查询所有菜单，并选中当前角色所拥有的菜单
	 * @return easyui要求的JSON格式字符串
	 */
	@RequestMapping("/loadCheckMenu")
	@RequiresPermissions(value="角色管理")
	public String loadCheckMenu(Integer roleId){
		
		List<Menu> menuList = menuService.getMenuByRoleId(roleId);
		
		List<Integer> menuIds = new ArrayList<Integer>();
		
		for(Menu menu : menuList){
			
			menuIds.add(menu.getId());
			
		}
		
		logService.save(new Log(Log.SELECT_ACTION, "查询菜单信息"));
		
		return this.getCheckAllMenu(-1,menuIds).toString(); //根节点默认从-1开始
	}
	
	/**
	 * 递归查询所有菜单
	 * @return
	 */
	public JsonArray getCheckAllMenu(Integer parentId,List<Integer> menuIds){
		
		JsonArray array = this.getCheckMenuByParentId(parentId, menuIds);
		
		for(int i=0;i<array.size();i++){
			
			JsonObject obj = (JsonObject) array.get(i);
			
			if(obj.get("state").getAsString().equals("open")){//如果是叶子节点，不再递归
				
				continue;
				
			}else{//如果是根节点，继续递归查询
				
				obj.add("children", this.getCheckAllMenu(obj.get("id").getAsInt(),menuIds));
				
			}
			
		}
		
		return array;
	}
	
	/**
	 * 根据父菜单ID获取所有子菜单
	 * @return
	 */
	public JsonArray getCheckMenuByParentId(Integer parentId,List<Integer> menuIds){
		
		JsonArray array = new JsonArray();
		
		List<Menu> menuList = menuService.getAllMenuByParentId(parentId);
		
		//遍历菜单
		for(Menu menu : menuList){
			
			JsonObject obj = new JsonObject();
			
			obj.addProperty("id", menu.getId());//菜单ID
			
			obj.addProperty("text", menu.getName());//菜单名称
			
			obj.addProperty("iconCls", menu.getIcon());//图标
			
			if(menu.getState()==1){
				
				obj.addProperty("state", "closed"); //根节点
				
			}else{
				
				obj.addProperty("state", "open");//叶子节点
			}
			
			if(menuIds.contains(menu.getId())){
				
				obj.addProperty("checked", true);
				
			}
			
			JsonObject attributes = new JsonObject(); //扩展属性
			
			attributes.addProperty("url", menu.getUrl());
			
			obj.add("attributes", attributes);
			
			array.add(obj);
			
				
		}
		
		return array;
	}

}
