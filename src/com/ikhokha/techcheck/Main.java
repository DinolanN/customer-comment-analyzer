package com.ikhokha.techcheck;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		Map<String, Integer> totalResults = new HashMap<>();
				
		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));
		
		//five threads at a time, can be changed later on
		int numberOfThreads = commentFiles.length <= 5 ? commentFiles.length : 5;
		
		ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
		
		for (File commentFile: commentFiles) {
			
			//Callback for getting the resultsMap from the thread
			Future<Map<String, Integer>> resultsMap = service.submit(new MultiThreadingFiles(commentFile));
			addReportResults(resultsMap.get(), totalResults);
			
		}
		
		service.shutdown(); 
		
		System.out.println("RESULTS\n=======");
		totalResults.forEach((k,v) -> System.out.println(k + " : " + v));
	}
	
	/**
	 * This method adds the result counts from a source map to the target map 
	 * @param source the source map
	 * @param target the target map
	 */
	private static void addReportResults(Map<String, Integer> source, Map<String, Integer> target) {
		
		if (target.isEmpty()) {
			
			for (Map.Entry<String, Integer> entry : source.entrySet()) {
				
				target.put(entry.getKey(), entry.getValue());
				
			}
			
		} else {
			
			for (Map.Entry<String, Integer> entry : source.entrySet()) {
				
				target.replace(entry.getKey(), target.get(entry.getKey()) + entry.getValue());

			}
		}
	}
}
