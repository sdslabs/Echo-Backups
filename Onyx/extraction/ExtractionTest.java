package sdslabs.preecho.extraction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

public class ExtractionTest {

		public static void main(String[] args) throws FileNotFoundException, IOException, SAXException, TikaException, ParserConfigurationException {
			String name = null;
	/*	File f = new File("/media/Media/SDS/algorithms of the intelligent web.pdf");
		
		// title will be stored in name.
		long start = System.currentTimeMillis();

		name = EchoExtraction.getTitle(f);
		try{
			assert name.compareTo("Algorithms of the Intelasdfligent Web".toLowerCase()) == 0 : name;
		}catch(AssertionError error){
			System.out.println(error.getMessage());
		}
		
		System.out.println(name);
		long end = System.currentTimeMillis();
		System.out.println("Time taken in extraction = " + (end - start) ); */
		Map<Integer,List<String>> map = new HashMap<Integer,List<String>>();
        
		// if name is empty, ask user to enter the name of the book
	/*	if(name.isEmpty() == true){
			System.out.println("Please Enter book name = ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				name = br.readLine();
			} catch (IOException e) {
				System.out.println("Error!");
				System.exit(1);
			}
		}    */
		
		//search value stored in name using google api
		//it will return name of books matching your search
		
		System.out.println("Please Enter book name to be searched = ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			name = br.readLine();
		} catch (IOException e) {
			System.out.println("Error!");
			System.exit(1);
		}
		map = EchoExtraction.searchGoogle(name);
		//  assert map.values().toString().compareTo("[[http://www.google.com/books/feeds/volumes/h-E1lVko-skC,  Programming PHP], [http://www.google.com/books/feeds/volumes/e7D-mITABmEC,  Core PHP programming], [http://www.google.com/books/feeds/volumes/2T7F7AslMukC,  Beginning PHP and MySQL: From Novice to Professional, Fourth Edition], [http://www.google.com/books/feeds/volumes/If3TLnM0s3kC,  PHP a beginner's guide], [http://www.google.com/books/feeds/volumes/Ee_22ndP6gIC,  PHP the complete reference]]") == 0;
		
		Map<String,String> final_map = new HashMap<String,String>();
		List<String> id = new ArrayList<String>();
		if(map.size() > 1) {
			Object[] isbnlist = (Object[]) map.keySet().toArray();
			int i=0; 
			for(List<String> s : map.values() ) {
				System.out.println(isbnlist[i].toString() + "--" +s.get(1));
				i++;
				}
			
			System.out.println("Enter the ISBN shown before the book name. Just copy  and paste the ISBN:");
			BufferedReader isbn = new BufferedReader(new InputStreamReader(System.in));
			try {
				name = isbn.readLine();
			} catch (IOException e) {
				System.out.println("Error!");
				System.exit(1);
			}
			int value = Integer.parseInt(name);
			
			id = map.get(value);
			System.out.println(id);
			String s2 = id.get(0);
			System.out.println(s2);
		    final_map = EchoExtraction.extractMetadata(s2);
		}
		
		else {
			
			for(List<String> s1 : map.values()) {
				final_map = EchoExtraction.extractMetadata(s1.get(0));
			}
			
			
		}
		System.out.println(final_map.toString());
		}
		}
			
		
	

