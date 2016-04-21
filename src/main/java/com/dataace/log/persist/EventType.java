package com.dataace.log.persist;


/**
 * @author dell
 * 数据操作类型
 */
public enum EventType {

	INSERT("增加"), UPDATE("更新"), DELETE("删除"), SELECT("查询");

	private EventType(String value) {
		this.value = value;
	}

	private String value;

	public String toString() {
		return value;
	}
}
