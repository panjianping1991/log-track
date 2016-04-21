package com.dataace.log;


import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.dataace.log.exception.InvalidOperationException;






public class BlockingTaskQueue<T> {
	
	private BlockingQueue<T> tasks = null;
	private long lastInsertTime =0;
	private boolean disable=false;
	
	public BlockingTaskQueue(int capacity){
		this.tasks = new ArrayBlockingQueue<T>(capacity);
		lastInsertTime= Calendar.getInstance().getTimeInMillis();
	}
	

	public void put(T task) throws InvalidOperationException, InterruptedException{
		if(disable){
			throw new InvalidOperationException("the queue has been disabled!");
		}
		this.tasks.put(task);
		lastInsertTime = Calendar.getInstance().getTimeInMillis();
	}
	
	public T poll(){
		if(disable){
			return null;
		}
		return tasks.poll();
	}
	
	public int size(){
		if(disable){
			return 0;
		}
		return tasks.size();
	}
	
	public boolean isEmpty(){
		if(disable){
			return true;
		}
		return tasks.isEmpty();
	}
	
	public boolean isDisable(){
		return false;
		
	}
	
    public  void setDisable(){
		disable=true;
	}

	public long getLastInsertTime() {
		return lastInsertTime;
	}


	public void setLastInsertTime(long lastInsertTime) {
		this.lastInsertTime = lastInsertTime;
	}
	
	
	
}

