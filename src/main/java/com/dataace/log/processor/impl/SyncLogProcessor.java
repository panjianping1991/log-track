package com.dataace.log.processor.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.dataace.log.model.SyncLog;
import com.dataace.log.processor.ILogProcessor;

public class SyncLogProcessor implements ILogProcessor<SyncLog>{
	
	 private static final Logger logger =  LogManager.getLogger(SyncLogProcessor.class);

	public List<SyncLog> process(String line) {
		try{
			String regex="[\\s\\S]+(\\{\"model\":\"\\w+\"\\,\"resultStatus\":\"\\w+\"\\,[\\s\\S]+})$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(line);
			if(matcher.find()){
				String data = matcher.group(1);
				List<SyncLog> syncLogs = new ArrayList<SyncLog>();
				JSONObject jsonObject = new JSONObject(data);
				Long eventTime = jsonObject.getLong("eventTime");
				String dataSource = jsonObject.getString("dataSource");
				String model = jsonObject.getString("model");
				String resultStatus= jsonObject.getString("resultStatus");
				String city = jsonObject.getString("city");
				String eventId = jsonObject.getString("eventId");
				String bizId = jsonObject.getString("bizId");
				String errorCause = null;
				if(jsonObject.has("errorCause")&&null!=jsonObject.get("errorCause")){
					errorCause=jsonObject.get("errorCause").toString();
			    }
				
				String extraInfo = null;
			    if(jsonObject.has("extraInfo")&&null!=jsonObject.get("extraInfo")){
			    	extraInfo=jsonObject.get("extraInfo").toString();
			    }
				
				Calendar c=Calendar.getInstance();
				c.setTimeInMillis(eventTime);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String eventTimeStr = format.format(c.getTime());
				SyncLog syncLog = new SyncLog();
				syncLog.setEventTime(eventTime);
				syncLog.setEventTimeStr(eventTimeStr);
				format = new SimpleDateFormat("yyyy-MM-dd");
				String eventDate = format.format(c.getTime());
				syncLog.setEventDate(eventDate);
				syncLog.setEventId(eventId);
				syncLog.setDataSource(dataSource);
				syncLog.setErrorCause(errorCause);
				syncLog.setModel(model);
				syncLog.setResultStatus(resultStatus);
				syncLog.setExtraInfo(extraInfo);
				syncLog.setCity(city);
				syncLog.setBizId(bizId);
				syncLogs.add(syncLog);
				return syncLogs;
			}
			
		}catch(Exception e){
			logger.error("failed to process line={},e={}", line,e);
		}
		
		
		
		return null;
	}
	
	

}
