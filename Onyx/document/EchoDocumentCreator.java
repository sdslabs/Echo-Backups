package sdslabs.preecho.document;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import sdslabs.preecho.analyzer.EchoAnalyzer;

public class EchoDocumentCreator {

	static Map<Document,Analyzer> docAnalyzerList= new HashMap<Document, Analyzer>();

	public static Map<Document, Analyzer> creator(Map<String,String> mdList, String id, File f) throws IOException, SAXException, TikaException{

		for(Map.Entry<String,String> fcpair: mdList.entrySet())	{

			Document doc=new Document();
			doc.add(new Field(fcpair.getKey(), fcpair.getValue(), Field.Store.NO, Field.Index.ANALYZED));
			doc.add(new Field("id",id,Field.Store.YES,Field.Index.NOT_ANALYZED));
			docAnalyzerList.put(doc, EchoAnalyzer.getAnalyzer(doc.getFields().get(0).toString()));
		}
		Document doc=new Document();
		doc.add(new Field("content",EchoDocumentParser.FileParser(f),Field.Store.NO,Field.Index.ANALYZED));
		docAnalyzerList.put(doc, EchoAnalyzer.getAnalyzer("content"));
		return docAnalyzerList;
	}

}
