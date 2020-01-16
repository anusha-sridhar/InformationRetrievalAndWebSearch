
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;


public class SearchIndex {

    public static void main(String[] args) {
    	
    	//Accept query input from the user
    	System.out.println("Enter query");
    	Scanner scan=new Scanner(System.in);
    	String userinput=scan.next();
    	
    	//calling the function to perform an Index search
    	try {
    		IndexSearch(userinput);
    	}catch (Exception e ) {
    		e.printStackTrace();
    	}
        
    }
    
    //IndexSearch() accepts the query obtained from the user as an argument
    public static void IndexSearch(String userinput1){
 
    	String ab="content:"+userinput1;
    	//System.out.println(ab);
        Directory indexdir= null;
        try {
            indexdir = FSDirectory.open(Paths.get("index"));
        } catch (IOException e) {
            System.out.println("FileNotFoundException"+e.getMessage());
            e.printStackTrace();
        }
        
        IndexReader reader= null;
        try {
            reader = DirectoryReader.open(indexdir);
        } catch (IOException e) {
            System.out.println("Index reader Exception"+e.getMessage());
            e.printStackTrace();
        }

        //Maximum number of documents are
        System.out.println("Maximum docs:\t\t"+reader.maxDoc());
      
        //create an object to search the solr index
        IndexSearcher searcher=new IndexSearcher(reader);
        
        //create a standard analyzer
        Analyzer analyzer=new StandardAnalyzer();
        
        //create query parser object
        QueryParser qp=new QueryParser("DOC_ID", analyzer);

        //create query object
        Query query= null;
        try {
            query = qp.parse(ab);
        } catch (ParseException e) {
            System.out.println("inside parse exception"+e.getMessage());
            e.printStackTrace();
        }
        
        //Find th etop documents
        TopDocs hits= null;
        try {
            hits = searcher.search(query, 3);
        } catch (IOException e) {
            System.out.println("inside io exception"+e.getMessage());
        }
       
        for(ScoreDoc scoreDoc:hits.scoreDocs)
        {
            Document doc= null;
            try {
                doc = searcher.doc(scoreDoc.doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(doc);
            List<IndexableField> list= doc.getFields();
            for (IndexableField indexableField : list) {
                System.out.println(indexableField.name()+" : "+doc.get(indexableField.name()));
            }
            System.out.println("------------------");
        }
              try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
