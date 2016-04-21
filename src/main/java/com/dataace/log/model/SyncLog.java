package com.dataace.log.model;

import com.dataace.log.annotation.MongoBean;
import com.dataace.log.persist.MongoCollection;


@MongoBean(collection = MongoCollection.LOG_STATISTICS)
public class SyncLog {
	
	private String id;
	private String bizId;
	private String city;
	private Long eventTime;
	private String eventId;
	private String model;
	private String resultStatus;
	private String dataSource;
	private String eventTimeStr;
	private String eventDate;
	private String errorCause;
	
	private String extraInfo;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getEventTime() {
		return eventTime;
	}

	public void setEventTime(Long eventTime) {
		this.eventTime = eventTime;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getEventTimeStr() {
		return eventTimeStr;
	}

	public void setEventTimeStr(String eventTimeStr) {
		this.eventTimeStr = eventTimeStr;
	}

	public String getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	
	
	

}
