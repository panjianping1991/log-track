package com.dataace.log.persist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dataace.log.util.StringUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;

public class MongoDB {
	
	private static final Logger logger =  LogManager.getLogger(MongoDB.class);

	private static DB db;
	
	private static MongoClient mongoClient;
	
	static{
		init();
	}
	
	public static void init() {
		        List<ServerAddress> serverAddresses = getAddress(MongoDBConfig.MONGO_DB_ADDRESS);
		        //认证信息
				MongoCredential credential = MongoCredential.createCredential(MongoDBConfig.MONGO_DB_USERNAME, MongoDBConfig.MONGO_DB_DBNAME, MongoDBConfig.MONGO_DB_PASSWORD.toCharArray());
				MongoClientOptions.Builder builder = new MongoClientOptions.Builder();

				mongoClient = new MongoClient(serverAddresses, Arrays.asList(credential));
				
				
				if (!StringUtil.isEmpty(MongoDBConfig.MONGO_DB_DBNAME)) {
					db = mongoClient.getDB(MongoDBConfig.MONGO_DB_DBNAME);		
				
				}
			
				System.out.println(db);
	}
	
	
	private static List<ServerAddress> getAddress(String addressStr) {
		List<ServerAddress> serverAddress = new ArrayList<ServerAddress>();

		if (!StringUtil.isEmpty(addressStr)) {
			String[] addresses = addressStr.split(",");
			
				for (String address : addresses) {
					String[] curAddresses = address.trim().split(":");
					if (curAddresses.length == 2) {
						int port = Integer.parseInt(curAddresses[1]);
						serverAddress.add(new ServerAddress(curAddresses[0],
								port));
					}
				}
			
		}
		
		return serverAddress;
	}
	
	
	/**
	 * 更新文档，如果文档不存在，则新增，如果文档存在，则新增字段，如若字段也存在，则更新字段的值(如果是sharded collection，则此方法慎用，会引发异常)
	 * @param collectionName collection名字
	 * @param queryMap 查询条件MAP
	 * @param updateDataMap 更新的记录MAP
	 * @param upsert 如果需要更新的文档不存在，是否需要upsert，为true表示需要，为false表示不需要
	 * @param multi 是否需要更新多条记录，为true表示会更新多条，为false表示只更新一条
	 * @return
	 */
	public static WriteResult upsert(MongoCollection collection, Map<String, Object> queryMap , Map<String, Object> updateDataMap, boolean upsert, boolean multi) {
		if(updateDataMap == null) {
			logger.error("BaseMongoDB updateDataMap is null");
			return null;
		}
		/*Map<String, Object> criteriaMap = Maps.newConcurrentMap();
		criteriaMap.put(Constants.DATA_ID_KEY, objMap.get(Constants.DATA_ID_KEY));*/
			
		return getDBCollection(collection.getName()).update(new BasicDBObject(queryMap), new BasicDBObject("$set",updateDataMap), upsert, multi);
	}

	
	
	/**
	 * 插入文档
	 * @param collection
	 * @param objMap
	 * @return
	 */
	public static WriteResult insert(MongoCollection collection, Map<String, Object> objMap) {
	
		String collectionName = collection.getName();
		if(objMap == null) {
			logger.error("objMap is null");
			return null;
		}
		
		return getDBCollection(collectionName).insert(new BasicDBObject(objMap));
	}

	
	
	/**
	 * 统计
	 * @param collection 数据类型
	 * @param queryObj 查询条件
	 * @return 统计数目
	 */
	public static long count(MongoCollection collection, DBObject queryObj) {
		if (queryObj != null) {
			return getDBCollection(collection.getName()).count(queryObj);
		} else {
			return getDBCollection(collection.getName()).count();
		}
	}
	
	/**
	 * 根据查询条件进行查询
	 * @param collection 数据类型
	 * @param obj 查询条件
	 * @return 结果集列表
	 */
	public static List<DBObject> find(MongoCollection collection, DBObject queryObj) {
		DBCursor dbCursor = db.getCollection(collection.getName()).find(queryObj);
		List<DBObject> objs =  DBCursor2List(dbCursor);
		dbCursor.close();
		return objs;
	}
	
	
	
	public static List<DBObject> find(MongoCollection collection, DBObject queryObj, int limit) {
		DBCursor dbCursor = getDBCollection(collection.getName()).find(queryObj).limit(limit);
		List<DBObject> objs = DBCursor2List(dbCursor);
		dbCursor.close();
		return objs;
	}
	
	/**
	 * 根据id和数据类型进行查询
	 * @param collectionName mongodb collection名字
	 * @param id 原始数据id
	 * @return 结果集列表
	 */
	public static DBObject findById(MongoCollection collection, String id) {
		BasicDBObject queryObj = new BasicDBObject();
		queryObj.put(MongoCollectionField.ID.getName(), id);

		return findOne(collection.getName(), queryObj);

	}

	/**
	 * 根据查询条件进行查询
	 * @param collectionName mongodb collection名字
	 * @param queryObj 查询条件
	 * @return 结果集列表
	 */
	public static  DBObject findOne(String collectionName, DBObject queryObj) {
		if(null==db.getCollection(collectionName)){
			return null;
		}
		return db.getCollection(collectionName).findOne(queryObj);
	}
	
	
	/**
	 * 更新文档，会主动从objMap中查询出_id字段，若是根据_id没有查询到文档，本方法不会将updateDataMap当作新的文档插入到mongodb，而且本方法只会更新一条数据
	 * @param collectionName 集合名
	 * @param objMap 待更新的map
	 * @return
	 */
	public static WriteResult updateByQueryIdAndDocumentNoInsert(MongoCollection collection, Map<String, Object> objMap) {
		if(objMap == null) {
			logger.error("objMap is null");
			return null;
		}
		Map<String,Object> notEmptyFields = new HashMap<String,Object>();
		for(String key:objMap.keySet()){
			if(null!=objMap.get(key)&&!"".equals(objMap.get(key).toString())){
				notEmptyFields.put(key, objMap.get(key));
			}
		}
		Map<String, Object> criteriaMap = new HashMap<String,Object>();
		Object pageOriginalIdObject = objMap.get(MongoCollectionField.ID.getName()); 
		if(null != pageOriginalIdObject){
			// 默认的记录就不会增加新的document	
			criteriaMap.put(MongoCollectionField.ID.getName(), pageOriginalIdObject);
			return getDBCollection(collection.getName()).update(new BasicDBObject(criteriaMap), new BasicDBObject("$set",notEmptyFields));
		}
		
		return null;
	}
	
	/**
	 * 将DB游标转换为List
	 * @param cur 游标
	 * @return 数据列表
	 */
	private static List<DBObject> DBCursor2List(DBCursor cur) {
		List<DBObject> list = new ArrayList<DBObject>();
		if (cur != null) {
			list = cur.toArray();
		}
		return list;
	}
	
	public static DBCollection getDBCollection(String collectionName) {
		return db.getCollection(collectionName);
	}
	
	public static void main(String[] args) {
	
		logger.info(MongoDB.findById(MongoCollection.LOG_STATISTICS,"1"));
	}

}
