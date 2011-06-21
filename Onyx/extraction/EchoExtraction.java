package sdslabs.preecho.extraction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gdata.client.books.*;
import com.google.gdata.data.books.*;
import com.google.gdata.data.dublincore.Title;
import java.io.DataInputStream;
import java.net.URLConnection;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

public class EchoExtraction {

	// -----function to return name of the book which will be used to search books on google-----
	public static String getTitle(File f)  throws FileNotFoundException,IOException,SAXException,TikaException  {		

		String title_field = Metadata.TITLE;
		InputStream stream = new FileInputStream(f);            // converting from file to InputStream
		Parser parser = new AutoDetectParser();                // to auto detect parser
		ContentHandler handler = new BodyContentHandler(-1);      // in argument, maximam limit has to be specified
		ParseContext context = new ParseContext();               
		context.set(Parser.class, parser);
		Metadata metadata = new Metadata();
     
	// try to put the data from stream to metadata and handler
		try {
			parser.parse(stream, handler, metadata, context);
		} 
		finally {
			stream.close();
		}

		String title = new String("");
		for(String name : metadata.names())   {
			if(name.compareTo(title_field) == 0 )    {
				title = metadata.get(name);
			}
		}

		return title.toLowerCase();

	}
	//---------end of the function getTitle--------------------



	public static Map<Integer,List<String>> searchGoogle(String str) {

		Map<Integer, List<String>> hm = null;

		try {
			BooksService service = new BooksService("Document List Demo");
			VolumeQuery query = new VolumeQuery(new URL("http://www.google.com/books/feeds/volumes"));

			query.setFullTextQuery(str);
			hm = new HashMap<Integer,List<String>>();
			VolumeFeed volumeFeed = service.query(query, VolumeFeed.class);

			for(VolumeEntry entry : volumeFeed.getEntries() ) {
				int isbnNumber = Integer.parseInt(entry.getIdentifiers().get(1).getValue().substring(5));
				List<String> list = new ArrayList<String>();
				String id = entry.getId();
				list.add(id);
				String t1 = new String("");
				for(Title t : entry.getTitles())   {
					t1 = t1+" "+ t.getValue();
				}
				list.add(t1);
				hm.put(isbnNumber, list);

			}


		}
		catch(Exception ex) {
			System.err.println("Exception : " + ex.getMessage() );
		}

		return hm;
	}


	public static Map<String,String> extractMetadata(String str) throws ParserConfigurationException, IOException {

		Map<String,String> list=null;

		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			URLConnection urlConn;
			URL url = new URL(str);
			urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setUseCaches(true);

			DataInputStream dis = new DataInputStream(urlConn.getInputStream());
			Document doc = docBuilder.parse (dis);
			String[] fields_arr = {"dc:title","dc:creator","dc:date","dc:description","dc:format","dc:identifier","dc:language","dc:publisher","dc:subject"};
			list = new HashMap<String,String>();

			for(int i=0;i<9;i++) {
				String ls = new String("");
				NodeList listOfNodes = doc.getElementsByTagName(fields_arr[i]);
				int totalNodes = listOfNodes.getLength();
				Element firstNodeElement;

				for(int j=0;j<totalNodes;j++) {
					firstNodeElement = (Element)listOfNodes.item(j);
					ls = ls + "  " + firstNodeElement.getTextContent();
				}
				/*if(i == 0){
					list.put(fields_arr[i], ls);
				}else{*/
					list.put(fields_arr[i].substring(3), ls.trim());
				//}
			}
		}
		catch (SAXException e) {
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();
		}

		return list;

	}
}