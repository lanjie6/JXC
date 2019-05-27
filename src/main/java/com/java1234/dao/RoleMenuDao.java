package com.java1234.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.RoleMenu;

/**
 * 角色和菜单关联Dao接口
 * @author 兰杰 2018.10.29
 *
 */
public interface RoleMenuDao extends JpaRepository<RoleMenu, Integer>,JpaSpecificationExecutor<RoleMenu>{
	
	/**
	 * 根据角色ID删除关联关系
	 * @param roleId 角色ID
	 */
	@Query(value="DELETE FROM t_role_menu WHERE role_id=?1",nativeQuery=true)
	@Modifying//如果需要删除或修改数据，需要添加@Modifying注解来配合使用，否则Jpa会报错
	public void deleteRoleMenuByRoleId(Integer roleId);

}
