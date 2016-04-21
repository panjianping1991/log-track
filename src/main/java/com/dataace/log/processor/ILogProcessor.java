package com.dataace.log.processor;

import java.util.List;

public interface ILogProcessor<E> {
	
	public List<E> process(String line);

}
