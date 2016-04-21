package com.dataace.log.pipeline;

import java.util.List;
import java.util.Map;

public interface Pipeline {
	
	public void process(List<Map<String,Object>> result);

}
