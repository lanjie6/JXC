package com.java1234.service;

import java.util.List;

import com.java1234.entity.Supplier;

public interface SupplierService {
	
	/**
	 * 根据供应商ID查询供应商信息
	 * @param supplierId 供应商ID
	 * @return 供应商信息实体
	 */
	public Supplier getSupplierById(Integer supplierId);
	
	/**
	 * 分页查询供应商信息
	 * @param page 当前页数
	 * @param pageSiez 每页显示条数
	 * @param supplierName 供应商名模糊查询条件
	 * @return 供应商信息集合
	 */
	public List<Supplier> getSupplierList(Integer page,Integer pageSize,String supplierName);
	
	/**
	 * 查询供应商数量
	 * @param supplierName 供应商名模糊查询条件
	 * @return 数量
	 */
	public Long getSupplierCount(String supplierName);
	
	/**
	 * 保存或修改供应商信息
	 * @param supplier 供应商实体
	 */
	public void saveSupplier(Supplier supplier);
	
	/**
	 * 删除供应商信息
	 * @param id  供应商id
	 */
	public void deleteSupplier(Integer id);
	
	/**
	 * 查询供应商下拉列表数据
	 * @param name 供应商名称
	 * @return
	 */
	public List<Supplier> getComboboxList(String name);
}
