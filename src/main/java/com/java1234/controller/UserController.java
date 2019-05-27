package com.java1234.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java1234.entity.Log;
import com.java1234.entity.Role;
import com.java1234.entity.User;
import com.java1234.entity.UserRole;
import com.java1234.resultConfig.ResultCode;
import com.java1234.service.LogService;
import com.java1234.service.RoleService;
import com.java1234.service.UserRoleService;
import com.java1234.service.UserService;
import com.java1234.util.StringUtil;

/**
 * 用户信息Controller控制器
 * @author 兰杰 2018.10.18
 * @since 1.0
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private LogService logService;
	
	/**
	 * 系统登录
	 * @param imageCode 用户传入的验证码
	 * @param userName 用户名
	 * @param password 密码
	 * @param session 用于取出系统生成的验证码
	 * @return 登录结果JSON
	 */
	@RequestMapping("/login")
	@ResponseBody
	public Map<String,Object> login(String imageCode,String userName,String password,HttpSession session){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			if(StringUtil.isEmpty(userName)){
				
				map.put("resultCode",ResultCode.FAIL);
				
				map.put("resultContent", "请输入用户名");
				
				return map;
				
			}
			
			if(StringUtil.isEmpty(password)){
				
				map.put("resultCode",ResultCode.FAIL);
				
				map.put("resultContent", "请输入密码");
				
				return map;
				
			}
	
			if(StringUtil.isEmpty(imageCode)){
				
				map.put("resultCode", ResultCode.FAIL);
				
				map.put("resultContent", "请输入验证码");
				
				return map;
				
			}
			
			if(!imageCode.toUpperCase().equals(session.getAttribute("checkcode"))){
				
				map.put("resultCode",ResultCode.FAIL);
				
				map.put("resultContent", "验证码输入有误");
				
				return map;
			}
			
			
			//开始进行登录效验
			Subject subject = SecurityUtils.getSubject();
			
			UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
			
			subject.login(token);
			
			//登录成功后，开始查询用户的角色
			User user = userService.findUserByName(userName);
			
			List<Role> roles = roleService.getRoleByUserId(user.getId());
			
			session.setAttribute("currentUser", user);
			
			logService.save(new Log(Log.LOGIN_ACTION,"登录系统"));
			
			map.put("roleList", roles);
			
			map.put("resultCode",ResultCode.SUCCESS);
			
			map.put("resultContent", "登录成功");
			
			return map;
			
		} catch (AuthenticationException e) {//如果抛出AuthenticationException异常，说明登录效验未通过
			
			e.printStackTrace();
			
			map.put("resultCode",ResultCode.FAIL);
			
			map.put("resultContent", "用户名或密码错误");
			
			return map;
			
		} catch(Exception e){
			
			e.printStackTrace();
			
			map.put("resultCode",ResultCode.ERROR);
			
			map.put("resultContent", "系统正忙，请稍后重试");
			
			return map;
			
		}
		
	}
	
	/**
	 * 从缓存中获取当前登录的用户相关信息
	 * @param seesion
	 * @return
	 */
	@RequestMapping("/loadUserInfo")
	@ResponseBody
	public Map<String,Object> loadUserInfo(HttpSession session){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		User user = (User)session.getAttribute("currentUser");
		
		Role role = (Role) session.getAttribute("currentRole");
		
		map.put("userName", user.getTrueName());
		
		map.put("roleName", role.getName());
		
		return map;
	}
	
	/**
	 * 分页查询用户信息
	 * @param page 当前页数
	 * @param rows 每页显示的记录数
	 * @param userName 用户名
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	@RequiresPermissions(value="用户管理")//有用户管理菜单权限的才给予调用
	public Map<String,Object> list(Integer page,Integer rows,String userName){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<User> users = userService.getUserList(page, rows, userName);
		
		for(User user : users){
			
			List<Role> roles = roleService.getRoleByUserId(user.getId());
			
			StringBuffer sb = new StringBuffer();
			
			for(Role role : roles){
				
				sb.append(","+role.getName());
				
			}
			
			user.setRoles(sb.toString().replaceFirst(",", ""));
			
		}
		
		logService.save(new Log(Log.SELECT_ACTION,"分页查询用户信息"));
		
		map.put("total", userService.getUserCount(userName));
		
		map.put("rows", users);
		
		return map;
	}
	
	/**
	 * 添加或修改用户信息
	 * @param user 用户信息实体
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	@RequiresPermissions(value="用户管理")
	public Map<String,Object> save(User user){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		//用户ID为空时，说明是新增操作，需要先判断用户名是否存在
		if(user.getId()==null){
			
			User exUser = userService.findUserByName(user.getUserName());
			
			if(exUser!=null){
				
				map.put("resultCode", ResultCode.FAIL);
				
				map.put("resultContent", "用户名已存在");
				
				return map;
				
			}
			
			logService.save(new Log(Log.INSERT_ACTION,"添加用户:"+user.getUserName()));
			
		}else{
			
			logService.save(new Log(Log.UPDATE_ACTION,"修改用户:"+user.getUserName()));
			
		}
		
		userService.saveUser(user);
		
		map.put("resultCode", ResultCode.SUCCESS);
		
		map.put("resultContent", "保存用户信息成功");
		
		return map;
	}
	
	/**
	 * 删除用户信息
	 * @param userId 用户ID
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions(value="用户管理")
	public Map<String,Object> delete(Integer userId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			logService.save(new Log(Log.DELETE_ACTION,"删除用户:"+userService.getUserById(userId).getUserName()));
			
			userRoleService.deleteUserRoleByUserId(userId);
			
			userService.deleteUser(userId);
			
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
	 * 设置用户角色
	 * @param userId 用户ID
	 * @param roles 角色ID数组字符串，用逗号号分割
	 * @return
	 */
	@RequestMapping("/setRole")
	@ResponseBody
	@RequiresPermissions(value="用户管理")
	public Map<String,Object> setRole(Integer userId,String roles){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			//先删除当前用户的所有角色
			userRoleService.deleteUserRoleByUserId(userId);
			
			//再赋予当前用户新的角色
			String[] roleArray = roles.split(",");
			
			for(String str : roleArray){
				
				UserRole ur = new UserRole();
				
				ur.setRole(roleService.getRoleById(Integer.parseInt(str)));
				
				ur.setUser(userService.getUserById(userId));
				
				userRoleService.addUserRole(ur);
				
			}
			
			logService.save(new Log(Log.UPDATE_ACTION,"设置用户"+userService.getUserById(userId).getUserName()+"的角色权限"));
			
			map.put("resultCode", ResultCode.SUCCESS);
			
			map.put("resultContent", "设置角色成功");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			map.put("resultCode", ResultCode.FAIL);
			
			map.put("resultContent", "设置角色失败");
		}
		
		return map;
	}
	
	/**
	 * 修改密码
	 * @param newPassword
	 * @param session
	 * @return
	 */
	@RequestMapping("/updatePassword")
	@ResponseBody
	@RequiresPermissions(value="修改密码")
	public Map<String,Object> updatePassword(String newPassword,HttpSession session){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		User user = (User) session.getAttribute("currentUser");
		
		user.setPassword(newPassword);
		
		userService.saveUser(user);
		
		logService.save(new Log(Log.UPDATE_ACTION,"修改密码"));
		
		map.put("resultCode", ResultCode.SUCCESS);
		
		map.put("resultContent", "修改密码成功，下次登录时生效");
		
		return map;
		
	}
	
	/**
	 * 安全退出
	 * @return
	 */
	@RequestMapping("/logOut")
	@RequiresPermissions(value="安全退出")
	public String logOut(HttpSession session){
		
		logService.save(new Log(Log.LOOUT_ACTION,"用户注销"));
		
		//清除shiro用户信息
		SecurityUtils.getSubject().logout();
		
		return "redirect:login.html";
	}
	
}
