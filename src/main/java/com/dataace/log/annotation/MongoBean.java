package com.dataace.log.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.dataace.log.persist.MongoCollection;



@Retention(RetentionPolicy.RUNTIME)
public @interface MongoBean {
	
	MongoCollection collection();

}
