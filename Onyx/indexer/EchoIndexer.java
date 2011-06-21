package sdslabs.preecho.indexer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

public class EchoIndexer
{
	private IndexWriter writer;
	private String directory;
	public EchoIndexer(String directory)
	{
		this.directory = directory;
	}
	private void index(Document doc, Analyzer analyzer) throws CorruptIndexException, LockObtainFailedException, IOException{

		writer= new IndexWriter(openDirectory(getFieldName(doc)), analyzer,true,IndexWriter.MaxFieldLength.UNLIMITED);
		writer.addDocument(doc);
		writer.close();

	}

	//Extracting field name
	private String getFieldName(Document doc){
		String str;
		str=doc.getFields().get(1).name(); // Assuming second field as name
		return(str);

	}

	private Directory openDirectory(String fieldName) throws IOException{
		File f=new File(this.directory+fieldName);// index directory path
		Directory indexDir= FSDirectory.open(f);
		return indexDir;

	}
	
	public void docIndexer(HashMap<Document,Analyzer> documents) throws CorruptIndexException, LockObtainFailedException, IOException{
		Set<Entry<Document,Analyzer>> set= documents.entrySet();	
		Iterator<Entry<Document,Analyzer>> i=set.iterator();
		while(i.hasNext())		
		{			
			Map.Entry<Document,Analyzer> m = (Map.Entry<Document,Analyzer>)i.next();
			Document doc=m.getKey();
			Analyzer analyzer=m.getValue();
			index(doc,analyzer);
		}
	}
	
}


