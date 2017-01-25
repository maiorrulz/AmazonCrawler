package com.rest.crawler;

import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceThreadPool {
	
	private Queue<String> urlList;
	ExecutorService executor = Executors.newCachedThreadPool();
	
	public ExecutorServiceThreadPool(Queue<String> list) {
		
		urlList = list;
	}
	
	
	
	public void startProcessing(Vector<Review> reviews) {
		
		while(!urlList.isEmpty()) {
			
			executor.submit(new ReviewCrawler(urlList.poll(), reviews));
		}
		
	}
	
	
    public void finish(){
    	
    	try {
    		executor.shutdown();
			executor.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("Finished all threads");
    }

}
