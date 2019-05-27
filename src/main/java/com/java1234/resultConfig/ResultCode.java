package com.java1234.resultConfig;

public interface ResultCode {
	
	/**
	 * 返回状态码：成功
	 */
	public static final String SUCCESS = "001";
	
	/**
	 * 返回状态码：失败
	 */
	public static final String FAIL = "002";
	
	/**
	 * 返回状态码：异常
	 */
	public static final String ERROR = "003";
	
	/**
	 * 返回状态码：登录超时
	 */
	public static final String OVERTIME = "004";

}
