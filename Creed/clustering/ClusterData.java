package creed.sdslabs.clustering;

import java.util.List;
import java.util.Map;

public class ClusterData {
	
	String clusterID;
	
	ClusterData(String ID) {
		clusterID = ID;
	}
	

	public List<String> getDocIDs() {
		return null;		
	}
	
	// return Map of User-Rating for a document
	public Map<String, Integer> getRating(String str) {
		return null;
	}
	
	public List<String> getLikes(String str) {
		return null;
	}
	
	public List<String> getBookmarks(String str) {
		return null;
	}
	
	public List<String> getSkips(String str) {
		return null;
	}
	
	public Map<String, List<Integer>> getDetails(String str) {
		return null;		
	}
	
	public List<String> getRandomCentroids() {
		return null;
	}
	
	

}
