package com.dataace.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dataace.log.annotation.MongoBean;
import com.dataace.log.filter.ILogFilter;
import com.dataace.log.filter.impl.SyncLogFilter;
import com.dataace.log.persist.MongoCollectionField;
import com.dataace.log.pipeline.Pipeline;
import com.dataace.log.pipeline.PipelineChain;
import com.dataace.log.pipeline.impl.MongoPipeline;
import com.dataace.log.processor.ILogProcessor;
import com.dataace.log.processor.impl.SyncLogProcessor;
import com.dataace.log.util.CollectionUtil;

public class LogMonitor {
    private static final Logger logger =  LogManager.getLogger(LogMonitor.class);
	
	private static BlockingTaskQueue<List<Map<String,Object>>> dataQueue = new BlockingTaskQueue<List<Map<String,Object>>>(1<<13); 
	private static PipelineChain pipelineChain = new PipelineChain();
	  
	
    public static void addPipeline(Pipeline pipeline){
    	pipelineChain.addPipeline(pipeline);
    }
    
	public static void monitor(String filepath,ILogFilter logFilter,ILogProcessor<?> logProcessor){
		logger.info("monitor start");
		processResult();
		Runtime runtime  =  Runtime.getRuntime();
	        Process process  =   null ;
	        String line  =   null ;
	        InputStream is  =   null ;
	        InputStreamReader isr  =   null ;
	        BufferedReader br  =   null ;
	        for(;;){
	        	 try   {
	        		 runtime  =  Runtime.getRuntime();
	 	            process  =  runtime.exec( " tail -F "+filepath );  
	 	        }   catch  (IOException e)  {
	 	            System.out.println(e);
	 	            break;
	 	        }
	 	        is  =  process.getInputStream();
	 	        isr  =   new  InputStreamReader(is);
	 	        br  =   new  BufferedReader(isr);
	 	         try   {
	 	             while  ((line  =  br.readLine())  !=   null )  {
	 	                if(logFilter.filter(line)){
	 	                	logger.info("matching:"+line);
	 	                	List<?> datas= logProcessor.process(line);
	 	                	List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
	 						if(null!=datas)
	 						for(Object data:datas){
	 							Map<String,Object> result = CollectionUtil.toMap(data);	
	 							MongoBean mongoBean = data.getClass().getAnnotation(MongoBean.class);

	 							if(null==mongoBean){				
	 								logger.error("please set the mongo collection for data,data="+data);
	 								continue;
	 							}

	 							result.put(MongoCollectionField.COLLECTION.getName(),mongoBean.collection());													
	 							results.add(result);
	 							try {
	 								dataQueue.put(results);
	 							} catch (Exception e) {
	 								logger.error("failed to put the result to dataQueue", e);
	 							} 
	 						}
	 	                }else{
	 	                	//logger.info("skip line");
	 	                }
	 	            }
	 	        }   catch  (IOException e)  {
	 	            logger.error("error occured in monitor", e);
	 	        } finally{
	 	        	if(null!=br){
	 	        		try {
	 						br.close();
	 					} catch (IOException e) {				
	 						e.printStackTrace();
	 					}
	 	        	}
	 	        }
	 	        logger.error("stream is closed!!!");
	        }
	        
	}
	
	
	 public static void processResult(){
	    	new Thread(){

				@Override
				public void run() {
				    int success=0;
				    int emptyTime=0;
					for(;;){
						List<Map<String,Object>> result = dataQueue.poll();
						if(null!=result){
							    success++;					
								if(null!=pipelineChain){
									pipelineChain.process(result);
								}
								if(success%10==0){
									logger.info("persist success:"+success+",dataQueue size:"+dataQueue.size());
								}
							
						}else{
							
							try {
								Thread.sleep(5000);
								emptyTime++;
								if(emptyTime%6==0){
									logger.info("dataQueue is empty,listerning for logs!!!");
								}
							} catch (InterruptedException e) {
								logger.error(e.getMessage(),e);
							}
						}
						
						
					}
				}
	    		
	    	}.start();
	    }
	
	
	public static void main(String[] args) {
		LogMonitor.addPipeline(new MongoPipeline());
		LogMonitor.monitor(args[0],new SyncLogFilter(),new SyncLogProcessor());
	}
}
