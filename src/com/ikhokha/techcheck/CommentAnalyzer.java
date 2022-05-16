package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentAnalyzer {
	
	private File file;
	private List<SearchMetrics> searchMetrics;
	
	private Map<String, Integer> resultsMap = new HashMap<>();
	
	public CommentAnalyzer(File file) {
		this.file = file;
	}
	
	public Map<String, Integer> analyze() {
		
		searchMetrics = SetSearchParams();
		
		resultsMap = InitializeMap(resultsMap, searchMetrics);
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				//Loop for check the files according to the search metrics
				for (SearchMetrics metric: searchMetrics) {
					
					//Switch case depending on the seach metric types
					switch(metric.Type.toLowerCase()) {
					
						case "int":
							
							if (metric.Name.toLowerCase().contains("shorter")) {
								
								if (line.length() < Integer.parseInt(metric.Value)) {
									
									incOccurrence(resultsMap, metric.Name);

								}
								
							} else if (metric.Name.toLowerCase().contains("longer")) {
								
								if (line.length() >= Integer.parseInt(metric.Value)) {
									
									incOccurrence(resultsMap, metric.Name);

								}
								
							}
							
							break;
						
						case "string":
							
							if (line.toLowerCase().contains(metric.Value.toLowerCase())) {
								
								incOccurrence(resultsMap, metric.Name);
								
							}
							
							break;
							
						default:
							
							break;
							
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Error processing file: " + file.getAbsolutePath());
			e.printStackTrace();
		}
		
		return resultsMap;
		
	}
	
	/**
	 * This method increments a counter by 1 for a match type on the countMap. Uninitialized keys will be set to 1
	 * @param countMap the map that keeps track of counts
	 * @param key the key for the value to increment
	 */
	private void incOccurrence(Map<String, Integer> countMap, String key) {
		
		countMap.putIfAbsent(key, 0);
		countMap.put(key, countMap.get(key) + 1);
	}
	
	//Initialize the map and set the metrics to 0
	private Map<String, Integer> InitializeMap (Map<String, Integer> countMap, List<SearchMetrics> searchMetrics) {
		
		for (SearchMetrics metric : searchMetrics) {
			countMap.putIfAbsent(metric.Name, 0);
		}
		
		return countMap;
		
	}
	
	//Add custom objects(search parameters) to the list of search metrics
	private List<SearchMetrics> SetSearchParams () {
		
		List<SearchMetrics> searchMetrics = new ArrayList<>();
		
		SearchMetrics searchMetric1 = new SearchMetrics("SHORTER_THAN_15", "int", "15");
		SearchMetrics searchMetric2 = new SearchMetrics("MOVER_MENTIONS", "string", "mover");
		SearchMetrics searchMetric3 = new SearchMetrics("SHAKER_MENTIONS", "string", "shaker");
		SearchMetrics searchMetric4 = new SearchMetrics("QUESTIONS", "string", "?");
		SearchMetrics searchMetric5 = new SearchMetrics("SPAM", "string", "http");
		searchMetrics.add(searchMetric1);
		searchMetrics.add(searchMetric2);
		searchMetrics.add(searchMetric3);
		searchMetrics.add(searchMetric4);
		searchMetrics.add(searchMetric5);
		
		return searchMetrics;
		
	}
}
