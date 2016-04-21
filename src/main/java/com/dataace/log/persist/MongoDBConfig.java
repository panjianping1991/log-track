package com.dataace.log.persist;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class MongoDBConfig {
	
    private static final Logger logger =  LogManager.getLogger(MongoDBConfig.class);

	/** 服务地址 */
	public  static String MONGO_DB_ADDRESS = "10.50.40.173:27019,10.50.40.174:27019,10.50.40.173:27019";

	/** 库名 */
	public  static String MONGO_DB_DBNAME = "webant";

	/** 用户名 */
	public  static String MONGO_DB_USERNAME = "webant";

	/** 密码 */
	public  static String MONGO_DB_PASSWORD = "Admin123";
	
	
	static{
		InputStream in;
		
			
		
		Properties properties =  new Properties();
		try {
			in = MongoDBConfig.class.getResourceAsStream("/db.properties");
			properties.load(in);
			MONGO_DB_ADDRESS=properties.getProperty("mongo.db.location");
			MONGO_DB_DBNAME=properties.getProperty("mongo.db.dbname");
			MONGO_DB_USERNAME=properties.getProperty("mongo.db.username");
			MONGO_DB_PASSWORD=properties.getProperty("mongo.db.password");
		} catch (Exception e) {
			logger.error("failed to read db.properties",e);
		}
	}
	
	
	
	
}
