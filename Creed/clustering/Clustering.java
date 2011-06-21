package creed.sdslabs.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.Math;
import creed.sdslabs.core.*;

public class Clustering {
	
	public void clustering() {
		
	}
	
	
	// returns the ID of one cluster and sends the ID of second one to other machine.
	public List<String> createCluster(String clusterID) {
		ClusterData C = new ClusterData(clusterID);
		return split(C);
	}
	
	// splits the cluster in two clusters and returns the IDs of new clusters as List<String>
	public List<String> split(ClusterData c) {
		
		List<String> centroids = c.getRandomCentroids();
		List<String> listIDs = c.getDocIDs();
		String prevCentroid1 = centroids.get(0);
		String prevCentroid2 = centroids.get(1);		
		List<String> child1 = new ArrayList<String>();
		List<String> child2 = new ArrayList<String>();
		
		
		Iterator<String> iter = listIDs.iterator();
		
		do { 
			Map<String, Integer> ratingCount1 = new HashMap<String, Integer>();
			Map<String, Integer> ratingCount2 = new HashMap<String, Integer>();
			
			Set<String> likesSet1 = new HashSet<String>();
			Set<String> bookmarkSet1 = new HashSet<String>();
			Set<String> skipsSet1 = new HashSet<String>();
			
			Set<String> likesSet2 = new HashSet<String>();
			Set<String> bookmarkSet2 = new HashSet<String>();
			Set<String> skipsSet2 = new HashSet<String>();
			
			Map<String, Integer> ratingMap1 = new HashMap<String, Integer>();
			Map<String, Integer> ratingMap2 = new HashMap<String, Integer>();
			
			while(iter.hasNext()) {
				String tempDoc = iter.next();      // document ID
				
				if( similarity(centroids.get(0), tempDoc, c) > similarity(centroids.get(1), tempDoc, c) ) {
				child1.add(tempDoc);
				
				// --- for ratings
				Map<String, Integer> tempRating1 = c.getRating(tempDoc);          // for centroid1
				Iterator<String> iter_r = tempRating1.keySet().iterator();        // iterator through all users rated the doc
								
				while( iter_r.hasNext() ) {
					String tempUser = iter_r.next();							  // any user 
					if( ratingMap1.containsKey(tempUser)) {
						int newRating = tempRating1.get(tempUser) + ratingMap1.get(tempUser);
						ratingMap1.put(tempUser, newRating);
						int newCount = ratingCount1.get(tempUser) + 1;
						ratingCount1.put(tempUser, newCount);
					}
					else {
						int newRating = tempRating1.get(tempUser);
						ratingMap1.put(tempUser, newRating);
						ratingCount1.put(tempUser, 1);
					}
				}
				
				//---- for likes
				
				List<String> tempLikes = c.getLikes(tempDoc);
				likesSet1.addAll(tempLikes);
				
				//---- for bookmarks
				List<String> tempBookmarks = c.getBookmarks(tempDoc);
				bookmarkSet1.addAll(tempBookmarks);
				
				//---- for skips
				List<String> tempSkips = c.getSkips(tempDoc);
				skipsSet1.addAll(tempSkips);
				
				}
				
				else {
					child2.add(tempDoc);
					
					// --- for ratings
					Map<String, Integer> tempRating2 = c.getRating(tempDoc);          // for centroid1
					Iterator<String> iter_r = tempRating2.keySet().iterator();        // iterator through all users rated the doc
									
					while( iter_r.hasNext() ) {
						String tempUser = iter_r.next();							  // any user 
						if( ratingMap2.containsKey(tempUser)) {
							int newRating = tempRating2.get(tempUser) + ratingMap2.get(tempUser);
							ratingMap2.put(tempUser, newRating);
							int newCount = ratingCount2.get(tempUser) + 1;
							ratingCount2.put(tempUser, newCount);
						}
						else {
							int newRating = tempRating2.get(tempUser);
							ratingMap2.put(tempUser, newRating);
							ratingCount2.put(tempUser, 1);
						}
					}
					
					//---- for likes					
					List<String> tempLikes = c.getLikes(tempDoc);
					likesSet2.addAll(tempLikes);
					
					//---- for bookmarks
					List<String> tempBookmarks = c.getBookmarks(tempDoc);
					bookmarkSet2.addAll(tempBookmarks);
					
					//---- for skips
					List<String> tempSkips = c.getSkips(tempDoc);
					skipsSet2.addAll(tempSkips);
				}
			}
			
			//--- calculation of new centroid
			Iterator<String> iter_c1 = ratingCount1.keySet().iterator();
			while(iter_c1.hasNext()) {
				String tempUser_c1 = iter_c1.next();
				Integer newRating_c1 = ratingMap1.get(tempUser_c1)/ ratingCount1.get(tempUser_c1) ;
				ratingMap1.put(tempUser_c1, newRating_c1);
			}
			
			Iterator<String> iter_c2 = ratingCount1.keySet().iterator();
			while(iter_c2.hasNext()) {
				String tempUser_c2 = iter_c2.next();
				Integer newRating_c2 = ratingMap2.get(tempUser_c2)/ ratingCount2.get(tempUser_c2) ;
				ratingMap1.put(tempUser_c2, newRating_c2);
			}
			
			prevCentroid1 = centroids.get(0);
			prevCentroid2 = centroids.get(1);
			
			String centroid1 = Core.storeData(likesSet1, bookmarkSet1, skipsSet1, ratingMap1);
			String centroid2 = Core.storeData(likesSet2, bookmarkSet2, skipsSet2, ratingMap2);
			centroids.clear();
			centroids.add(centroid1);
			centroids.add(centroid2);			
		} while( similarity(centroids.get(0), prevCentroid1, c) !=0 && similarity(centroids.get(1), prevCentroid2, c) !=0 );
		return null;
	}
	
	public double similarity(String ID1, String ID2, ClusterData c) {
		double distance1 = calculateCorrelation(ID1, ID2, c);
		double distance2 = calculateDistance(ID1, ID2, c);
		return (distance1 + distance2)/2;
	}
	
	// user , likes - suggestion - skips correlation coefficient and 
	public double calculateCorrelation(String ID1, String ID2, ClusterData C) {
		
		int inter_size = 0;
		int union_size = 0;
		// -- for ratings
		Map<String, Integer> ratingDoc1 = C.getRating(ID1);
		Map<String, Integer> ratingDoc2 = C.getRating(ID2);
		
		Set<String> s1_r = ratingDoc1.keySet();
		Set<String> s2_r = ratingDoc2.keySet();
		Set<String> s_r = new HashSet<String>(s1_r);
		s_r.retainAll(s2_r);								// now 's' will contain users which have rated both documents
		
		int d1 = 0;
		int d2 = 0;
		int d12 = 0;
		int d22 = 0;
		int d1d2 = 0;
		int count = s_r.size();
		
		Iterator<String> iter = s_r.iterator();
		
		while( iter.hasNext() ) {
			String tempUser = iter.next();
			d1 += ratingDoc1.get(tempUser);
			d2 += ratingDoc2.get(tempUser);
			d12 += ratingDoc1.get(tempUser)*ratingDoc1.get(tempUser);
			d22 += ratingDoc2.get(tempUser)*ratingDoc2.get(tempUser);
			d1d2 += ratingDoc1.get(tempUser)*ratingDoc2.get(tempUser);
		}
		
		double correlation_rating = (count*d1d2 - ( d1*d2 )) / Math.sqrt((count*d12 - (d1*d1))*(count*d22 - (d2*d2)));
		
		// --for likes
		List<String> likeDoc1 = C.getLikes(ID1);
		List<String> likeDoc2 = C.getLikes(ID2);
		Set<String> s1_l = new HashSet<String>(likeDoc1);
		Set<String> s2_l = new HashSet<String>(likeDoc2);
		
		Set<String> inter_l = new HashSet<String>(s1_l);
		inter_l.retainAll(s2_l);
		inter_size = inter_l.size();
		
		Set<String> union_l = new HashSet<String>(s1_l);
		union_l.addAll(s2_l);
		union_size = union_l.size();
		
		double correlation_likes = inter_size / union_size;
		
		//-- for bookmarks
		List<String> bookmarkDoc1 = C.getBookmarks(ID1);
		List<String> bookmarkDoc2 = C.getBookmarks(ID2);
		Set<String> s1_b = new HashSet<String>(bookmarkDoc1);
		Set<String> s2_b = new HashSet<String>(bookmarkDoc2);
		
		Set<String> inter_b = new HashSet<String>(s1_b);
		inter_b.retainAll(s2_b);
		inter_size = inter_b.size();
		
		Set<String> union_b = new HashSet<String>(s1_b);
		union_b.addAll(s2_b);
		union_size = union_b.size();
		
		double correlation_bookmarks = inter_size / union_size;
				
		//-- for skips
		List<String> skipDoc1 = C.getBookmarks(ID1);
		List<String> skipDoc2 = C.getBookmarks(ID2);
		Set<String> s1_s = new HashSet<String>(skipDoc1);
		Set<String> s2_s = new HashSet<String>(skipDoc2);
		
		Set<String> inter_s = new HashSet<String>(s1_s);
		inter_s.retainAll(s2_s);
		inter_size = inter_s.size();
		
		Set<String> union_s = new HashSet<String>(s1_s);
		union_s.addAll(s2_s);
		union_size = union_s.size();
		
		double correlation_skips = inter_size / union_size;
		
		//--overall correlation coefficient
		double correlation = (correlation_rating + correlation_likes + correlation_bookmarks + correlation_skips) / 4;		
		return correlation;		
	}
	
	// only for rating --
	public double calculateDistance(String ID1, String ID2, ClusterData D) {
		Map<String, Integer> ratingDoc1 = D.getRating(ID1);
		Map<String, Integer> ratingDoc2 = D.getRating(ID2);
		
		Set<String> s1 = ratingDoc1.keySet();
		Set<String> s2 = ratingDoc2.keySet();
		Set<String> s = new HashSet<String>(s1);
		s.retainAll(s2);
		double distance = 0;
		
		Iterator<String> iter = s.iterator();
		while(iter.hasNext()) {
			String tempUser = iter.next();
			distance += (ratingDoc1.get(tempUser)*ratingDoc1.get(tempUser)) - (ratingDoc2.get(tempUser)*ratingDoc2.get(tempUser));
		}
		
		distance = Math.sqrt(distance);
		return distance;		
	}
}
