package sdslabs.preecho.document;

import java.io.File;
import java.io.FileInputStream;
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

public class EchoDocumentParser {

	static String FileParser(File f) throws IOException, SAXException, TikaException{

		InputStream is= new FileInputStream(f);
		Parser parser = new AutoDetectParser();
		ContentHandler handler = new BodyContentHandler(-1);
		ParseContext context = new ParseContext();
		context.set(Parser.class, parser);
		
		try{
			parser.parse(is, handler, new Metadata(), context);
		}finally{
			is.close();
		}
		
		return handler.toString();
	}
	
}
