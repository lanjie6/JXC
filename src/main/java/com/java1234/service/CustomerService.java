package com.java1234.service;

import java.util.List;

import com.java1234.entity.Customer;

public interface CustomerService {
	
	/**
	 * 根据客户ID查询客户信息
	 * @param customerId 客户ID
	 * @return 客户信息实体
	 */
	public Customer getCustomerById(Integer customerId);
	
	/**
	 * 分页查询客户信息
	 * @param page 当前页数
	 * @param pageSiez 每页显示条数
	 * @param customerName 客户名模糊查询条件
	 * @return 客户信息集合
	 */
	public List<Customer> getCustomerList(Integer page,Integer pageSize,String customerName);
	
	/**
	 * 查询客户数量
	 * @param customerName 客户名模糊查询条件
	 * @return 数量
	 */
	public Long getCustomerCount(String customerName);
	
	/**
	 * 保存或修改客户信息
	 * @param customer 客户实体
	 */
	public void saveCustomer(Customer customer);
	
	/**
	 * 删除客户信息
	 * @param id  客户id
	 */
	public void deleteCustomer(Integer id);
	
	/**
	 * 查询供应商下拉列表数据
	 * @param name 供应商名称
	 * @return
	 */
	public List<Customer> getComboboxList(String name);
}
