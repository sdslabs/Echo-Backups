package creed.sdslabs.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Recommendation {
	
	//----returns list of the docs rated by the user ( cassandra function )
	public Map<String, Integer> docList() {
		return null;
	}
	
	public List<String> docLikes() {
		return null;
	}
	
	public Map<String, Integer> recommendLikes(List<String> results) {
		List<String> docList = docLikes();
		Map<String, List<String>> clusDocMap = new HashMap<String, List<String>>();
		for(String s : docList) {
			List<String> tempDocList;
			String tempClusterID = getClusterID(s);
			if(clusDocMap.keySet().contains(tempClusterID)) {
				tempDocList = clusDocMap.get(tempClusterID);
				tempDocList.add(s);
				clusDocMap.put(tempClusterID, tempDocList);
			}
			else {
				tempDocList = new ArrayList<String>();
				tempDocList.add(s);
				clusDocMap.put(tempClusterID, tempDocList);
			}
		}
		
		Map<String, Integer> finalMap = new HashMap<String, Integer>();
		for(String result : results) {
			String tempClusterID = getClusterID(result);
			if( clusDocMap.keySet().contains(tempClusterID) ) {
				int size = clusDocMap.get(tempClusterID).size();
				finalMap.put(tempClusterID, size);
			}
		}
		return finalMap;
	}
	
	public Map<String, Double> recommendRating(List<String> results) {
		Map<String, Integer> mapDocs = docList();
		Iterator<String> iter = mapDocs.keySet().iterator();
		Map<String, List<String>> map1 = new HashMap<String, List<String>>();
		while(iter.hasNext()) {
			List<String> tempList;
			String tempDoc = iter.next();
			String tempClusterID = getClusterID(tempDoc);
			if(map1.keySet().contains(tempClusterID)) {
				tempList = map1.get(tempClusterID);
				tempList.add(tempDoc);
				map1.put(tempClusterID, tempList);
			}
			else {
				tempList = new ArrayList<String>();
				tempList.add(tempDoc);
				map1.put(tempClusterID, tempList);
			}
		}
		//--map1 will have clusterIDs and related docs & mapDocs has docs and rating for a particular user
		Map<String, Double> finalMap = new HashMap<String, Double>();
		
		for(String s : results) {
			String tempClusterID = getClusterID(s);
			if( map1.keySet().contains(tempClusterID) ) {
				double avgRating = getAvgRating(map1.get(tempClusterID), mapDocs);
				finalMap.put(tempClusterID, avgRating);
			}
		}
		return finalMap;
	}
	
	
	// -- cassandra function --
	public String getClusterID(String doc) {
		return "ID";
	}
	
	public double getAvgRating( List<String> list, Map<String, Integer> map) {
		int avgRating = 0;
		int count = 0;
		for(String s : list) {
			if( map.keySet().contains(s)) {
				avgRating += map.get(s);
				count++;
			}
		}
		
		avgRating /= count;
		return avgRating;
	}
	
	
	
}
