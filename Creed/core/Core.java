package creed.sdslabs.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Core {
	
	public static void sendToMachine(String machineID, String add) {
		
	}
	
	// this function will store the cluster on Cassandra and return the address of cassandra
	public static String storeData(List<String> data) {
		return "add";
	}
	
	public static String storeData(Set<String> likes, Set<String> bookmarks, Set<String> skips, Map<String,Integer> rating) {
		return "add";
	}
}
