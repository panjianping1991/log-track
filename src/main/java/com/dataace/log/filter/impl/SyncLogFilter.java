package com.dataace.log.filter.impl;

import com.dataace.log.filter.ILogFilter;

public class SyncLogFilter implements ILogFilter{

	public boolean filter(String line) {
		String regex="[\\s\\S]+(\\{\"model\":\"\\w+\"\\,\"resultStatus\":\"\\w+\"\\,[\\s\\S]+})$";
		if(null!=line&&line.matches(regex)){
			return true;
		}
		return false;
	}

}
