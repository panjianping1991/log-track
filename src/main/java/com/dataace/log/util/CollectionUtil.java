package com.dataace.log.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;







import com.google.gson.Gson;



public class CollectionUtil {
	

	public static Map<String,Object> toMap(Object data) {
		Gson gson = new Gson();
	
		return JsonToMap.toMap(gson.toJson(data));
			  //return gson.fromJson(gson.toJson(data), new TypeToken<Map<String,Object>>(){}.getType());
//			JSONObject jsonObj = new JSONObject(data);
//			for(String key:JSONObject.getNames(jsonObj)){
//				result.put(key, jsonObj.get(key)==null?null:jsonObj.get(key).toString());			
//			}
//			
//		   return result;
	}
	
	
	


}
