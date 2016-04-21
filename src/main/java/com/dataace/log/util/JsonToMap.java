package com.dataace.log.util;


import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Map.Entry;  
import java.util.Set;  

import com.google.gson.JsonArray;  
import com.google.gson.JsonElement;  
import com.google.gson.JsonObject;  
import com.google.gson.JsonParser;  


public class JsonToMap {  
    
  /** 
   * 获取JsonObject 
   * @param json 
   * @return 
   */  
  public static JsonObject parseJson(String json){  
      JsonParser parser = new JsonParser();  
      JsonObject jsonObj = parser.parse(json).getAsJsonObject();  
      return jsonObj;  
  }  
    
  /** 
   * 根据json字符串返回Map对象 
   * @param json 
   * @return 
   */  
  public static Map<String,Object> toMap(String json){  
      return JsonToMap.toMap(JsonToMap.parseJson(json));  
  }  
    
  /** 
   * 将JSONObjec对象转换成Map-List集合 
   * @param json 
   * @return 
   */  
  public static Map<String, Object> toMap(JsonObject json){  
      Map<String, Object> map = new HashMap<String, Object>();  
      Set<Entry<String, JsonElement>> entrySet = json.entrySet();  
      for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ){  
          Entry<String, JsonElement> entry = iter.next();  
          String key = entry.getKey();  
          Object value = entry.getValue();  
          if(value instanceof JsonArray)  
              map.put((String) key, toList((JsonArray) value));  
          else if(value instanceof JsonObject)  
              map.put((String) key, toMap((JsonObject) value));  
          else  {
        	  if(value.getClass()==com.google.gson.JsonPrimitive.class){
        		  if(null!=value){
        			  if(value.toString().matches("\\d{1,9}")){
        				  value = Integer.parseInt(value.toString());
        			  }else if(value.toString().matches("\\d+\\.\\d+")){
        				  value = Double.parseDouble(value.toString());
        			  }else{
        				  value=value.toString();
        				  if(value.toString().startsWith("\"")){
        					  value=value.toString().substring(1);
        				  }
        				  if(value.toString().endsWith("\"")){
        					  value=value.toString().substring(0,value.toString().length()-1);
        				  }
        				  
        			  }
        		  }
        		 map.put((String) key, value);  
        	  }
        	 
          }
            
      }  
      return map;  
  }  
    
  /** 
   * 将JSONArray对象转换成List集合 
   * @param json 
   * @return 
   */  
  public static List<Object> toList(JsonArray json){  
      List<Object> list = new ArrayList<Object>();  
      for (int i=0; i<json.size(); i++){  
          Object value = json.get(i);  
          if(value instanceof JsonArray){  
              list.add(toList((JsonArray) value));  
          }  
          else if(value instanceof JsonObject){  
              list.add(toMap((JsonObject) value));  
          }  
          else{  
              list.add(value);  
          }  
      }  
      return list;  
  }  

}  



