package com.dataace.log.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PipelineChain implements Pipeline{
	
	private List<Pipeline> pipelines = new ArrayList<Pipeline>();

	
	public void addPipeline(Pipeline pipeline) {
		if(null != pipeline)
			this.pipelines.add(pipeline);
	}
	
	public List<Pipeline> getPipelines() {
		return this.pipelines;
	}
	
	public boolean isEmpty() {
		return pipelines.isEmpty();
	}
	
	public void clearPipeline() {
		pipelines.clear();
	}

	public void process(List<Map<String, Object>> result) {
		for(Pipeline pipeline: pipelines) {
        	if(null != pipeline)
        		pipeline.process(result);
        }
		
	}

}
