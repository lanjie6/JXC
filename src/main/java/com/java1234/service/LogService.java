package com.java1234.service;

import java.util.Date;
import java.util.List;

import com.java1234.entity.Log;

/**
 * 日志Service接口
 * @author 兰杰 2018.11.1
 * @since 1.0
 *
 */
public interface LogService {
	
	/**
	 * 保存或修改日志
	 * @param log 日志实体
	 */
	public void save(Log log);
	
	/**
	 * 分页按条件查询日志信息
	 * @param type 日志类型
	 * @param trueName 操作人员真实姓名
	 * @param sTime 日志开始时间
	 * @param eTime 日志结束时间
	 * @param page 当前页数
	 * @param rows 每页显示条数
	 * @return 日志信息集合
	 */
	public List<Log> getLogList(String type,String trueName,Date sTime,Date eTime,Integer page,Integer rows);
	
	/**
	 * 查询日志数量
	 * @param type 日志类型
	 * @param trueName 操作人员真实姓名
	 * @param sTime 日志开始时间
	 * @param eTime 日志结束时间
	 * @return 记录数
	 */
	public Long getLogCount(String type,String trueName,Date sTime,Date eTime);

}
