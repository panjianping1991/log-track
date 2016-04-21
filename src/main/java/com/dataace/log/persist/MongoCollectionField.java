package com.dataace.log.persist;


public enum MongoCollectionField {
	
	
	
	ID("_id","Mongo Collection 主键"),
    COLLECTION("collection","集合"),
	CREATE_TIME("createTime","创建时间"),
	UPDATE_TIME("updateTime","更新时间");
	
	
	
	
	private String name;
	private String description;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	MongoCollectionField(String name,String description){
		this.name=name;
		this.description=description;
	}

}
