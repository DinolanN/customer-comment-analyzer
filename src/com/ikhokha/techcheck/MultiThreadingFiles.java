package com.ikhokha.techcheck;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class MultiThreadingFiles implements Callable<Map<String, Integer>> {
	
	private File commentFile;
	public Map<String, Integer> results = new HashMap<>();
	
	public MultiThreadingFiles (File commentFile) {
		
		this.commentFile = commentFile;
		
	}

	//Override call funstion to return the results map
	@Override
	public Map<String, Integer> call() throws Exception {
		
		CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile);
		Map<String, Integer> fileResults = commentAnalyzer.analyze();
		addReportResults(fileResults, results);
		
		return results;
		
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
