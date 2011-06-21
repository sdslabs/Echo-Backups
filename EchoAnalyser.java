package sdslabs.preecho.analyzer;

import java.util.Collections;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class EchoAnalyzer {

	public static Analyzer getAnalyzer(String fieldName){
	
		if (fieldName.compareTo("description") == 0 || fieldName.compareTo("content") == 0){
			return (Analyzer) new StandardAnalyzer (Version.LUCENE_30);		
		}else{
			return (Analyzer) new StandardAnalyzer (Version.LUCENE_30,Collections.emptySet() );
		}
		
	}
	
}