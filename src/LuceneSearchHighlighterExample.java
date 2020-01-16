import java.nio.file.Paths;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneSearchHighlighterExample {
	public static void main(String[] args) throws Exception
	{
		final String INDEX_DIR = "index";
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));	
		System.out.println(dir);
	}
	

}
