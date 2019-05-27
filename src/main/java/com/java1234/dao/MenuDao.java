package com.java1234.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.Menu;

/**
 * 菜单Dao层
 * @author 兰杰 2018.10.23
 *
 */
public interface MenuDao extends JpaRepository<Menu, Integer>{
	
	/**
	 * 根据角色ID查询菜单信息
	 * @param roleId 角色ID
	 * @return 菜单集合
	 */
	@Query(value = "SELECT m.* FROM t_role r,t_menu m,t_role_menu rm WHERE r.id=rm.role_id AND m.id=rm.menu_id AND r.id=?1", nativeQuery = true)
	public List<Menu> getMenuByRoleId(Integer roleId);
	
	/**
	 * 根据菜单父ID获取当前角色下的菜单信息
	 * @param parentId 菜单父ID
	 * @param roleId 角色ID
	 * @return 菜单信息集合
	 */
	@Query(value="SELECT * FROM t_menu WHERE p_id=?1 AND id IN (SELECT menu_id FROM t_role_menu WHERE role_id=?2)",nativeQuery=true)
	public List<Menu> getMenuByParentId(Integer parentId,Integer roleId);
	
	/**
	 * 根据父菜单ID查询所有子菜单
	 * @param parentId 菜单父ID
	 * @return
	 */
	@Query(value="SELECT * FROM t_menu WHERE p_id=?1",nativeQuery=true)
	public List<Menu> getAllMenuByParentId(Integer parentId);
	
}
