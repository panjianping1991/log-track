package com.dataace.log.persist;

public enum MongoCollection {
	
	LOG_STATISTICS("log_statistics","");
	
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
	MongoCollection(String name,String description){
		this.name=name;
		this.description=description;
	}

}
