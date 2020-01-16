import java.io.File;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
public class LuceneDemo {
	
	public static String userinput;
	
	//contains the indexed files
	public static final String INDEX_DIR = "index";
	
	/*private static IndexSearcher createSearcher() throws IOException
	{
		
	    Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
	    IndexReader reader = DirectoryReader.open(dir);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    return searcher;
	}*/
	
	public static void main(String[] args) throws Exception 
	{
		//Input the query
        System.out.println("Enter query");
        Scanner scan=new Scanner(System.in);
        userinput=scan.next();
		
		//gets the reference of the directory
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		
		//print path
		System.out.println(dir);
		
		//Index reader - an interface for accessing a point-in-time view of a lucene index
		IndexReader reader = DirectoryReader.open(dir);
		
		//Create lucene searcher. It search over a single IndexReader.
        IndexSearcher searcher = new IndexSearcher(reader);
         
        //analyzer with the default stop words
        Analyzer analyzer = new StandardAnalyzer();
        
        //Query parser to be used for creating TermQuery
        QueryParser qp = new QueryParser("contents",analyzer);
         
        //Create the query
        Query query = qp.parse(userinput);
        System.out.println(query);
        System.out.println(qp);
        
        //Search the lucene documents
        TopDocs hits = searcher.search(query, 10);
        
        //Printing the docs one by one
        ScoreDoc[] score = hits.scoreDocs;
        for (int i = 0; i < score.length; i++) {
            int docId = score[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d.get("filename"));
        }

        System.out.println("Found " + score.length);

    }
        
        
         
	
}