package com.dataace.log.pipeline.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;








import com.dataace.log.persist.EventType;
import com.dataace.log.persist.MongoCollection;
import com.dataace.log.persist.MongoCollectionField;
import com.dataace.log.persist.MongoDB;
import com.dataace.log.pipeline.Pipeline;
import com.mongodb.DBObject;

public class MongoPipeline implements Pipeline{

	private static final Logger logger =  LogManager.getLogger(MongoPipeline.class);
	
	public void process(List<Map<String, Object>> result) {
			
		if(null!=result){
			for(Map<String,Object> model:result){
				Map<String, Object> qr = null;
				DBObject dbObject = null;
				
			
				String _id = null;
				if(null!=model.get(MongoCollectionField.ID.getName())){
					_id = model.get(MongoCollectionField.ID.getName()).toString();
				}
				
				MongoCollection collection = (MongoCollection) model.remove(MongoCollectionField.COLLECTION.getName());
				
				if(_id!=null){
					
					
					// 先查询
					dbObject = MongoDB.findById(collection,(String)_id);
					
					if(dbObject != null) {
						//更新操作
						//根据_id获取原始数据，如果必要属性都已经爬取到了，则进行mq推送
						logger.debug(" {} one data, _id={}, collectionName={}", EventType.UPDATE, _id,collection.getName());
					
						qr = new HashMap<String,Object>();
						
						qr.putAll(model);
						qr.put(MongoCollectionField.CREATE_TIME.getName(),Calendar.getInstance().getTimeInMillis());
						qr.put(MongoCollectionField.UPDATE_TIME.getName(),Calendar.getInstance().getTimeInMillis());
						MongoDB.updateByQueryIdAndDocumentNoInsert(collection, qr);
						
					}else {
						// 增加操作
						// 如果原始数据已经包含必要属性，则进行mq推送，否则只是新增记录
						logger.debug("{} one data, _id={}, collectionName={}", EventType.INSERT, _id,collection.getName());
						model.put(MongoCollectionField.CREATE_TIME.getName(),Calendar.getInstance().getTimeInMillis());
						model.put(MongoCollectionField.UPDATE_TIME.getName(),Calendar.getInstance().getTimeInMillis());
				
						MongoDB.insert(collection, model);

					}
				}else{
					// 增加操作
					// 如果原始数据已经包含必要属性，则进行mq推送，否则只是新增记录
					logger.debug("{} one data, _id={}, collectionName={}", EventType.INSERT, _id,collection.getName());
					model.put(MongoCollectionField.CREATE_TIME.getName(),Calendar.getInstance().getTimeInMillis());
					model.put(MongoCollectionField.UPDATE_TIME.getName(),Calendar.getInstance().getTimeInMillis());
			
					MongoDB.insert(collection, model);
				}
				
			}
		}else{
			logger.info("empty result");
		}
		
	}

}
