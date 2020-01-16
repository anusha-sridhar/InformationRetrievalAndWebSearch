import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssignmentGUI {
	
	private JFrame frame;
	private JTextField QueryField;
	private JTextArea ResultField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AssignmentGUI window = new AssignmentGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AssignmentGUI() {
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		QueryField = new JTextField();
		QueryField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		QueryField.setBounds(97, 41, 798, 36);
		frame.getContentPane().add(QueryField);
		QueryField.setColumns(10);
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//button click action here
				
				String userinput=QueryField.getText();
				String ab="content:"+userinput;
				
				Directory indexdir= null;
			        try {
			            indexdir = FSDirectory.open(Paths.get("index"));
			        } catch (IOException ex) {
			        	ResultField.setText("FileNotFoundException"+ex.getMessage());
			            ex.printStackTrace();
			        }
			     
			    IndexReader reader= null;
			        try {
			            reader = DirectoryReader.open(indexdir);
			        } catch (IOException ex) {
			        	ResultField.setText("Index reader Exception"+ex.getMessage());
			            ex.printStackTrace();
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
			        } catch (ParseException ex) {
			        	ResultField.setText("inside parse exception"+ex.getMessage());
			            ex.printStackTrace();
			        }
			        
			        //Find th etop documents
			        TopDocs hits= null;
			        try {
			            hits = searcher.search(query, 5);
			        } catch (IOException ex) {
			        	ResultField.setText("inside io exception"+ex.getMessage());
			        }
			        ArrayList<String> ar = new ArrayList<String>();
			        
			        for(ScoreDoc scoreDoc:hits.scoreDocs)
			        {
			            Document doc= null;
			            try {
			                doc = searcher.doc(scoreDoc.doc);
			            } catch (IOException ex) {
			                ex.printStackTrace();
			            }
			            
			            String aa=doc.toString();
			            ar.add(aa);
			            ar.add("\n");
			            
			            
			            
			            List<IndexableField> list= doc.getFields();
			            for (IndexableField indexableField : list) {
			            	ar.add(indexableField.name()+" : "+doc.get(indexableField.name()));
			            	ar.add("\n");
			            }
			            ar.add("------------------");
			            ar.add("\n");
			        }
			              try {
			            reader.close();
			        } catch (IOException ex) {
			            ex.printStackTrace();
			        }
			    
			        
			        
			    
				String abc= ar.toString();
				ResultField.setText(abc);
			}
		});
		btnNewButton.setBounds(420, 90, 137, 36);
		frame.getContentPane().add(btnNewButton);
		
		ResultField = new JTextArea();
		ResultField.setEditable(false);
		//ResultField.setEnabled(false);
		ResultField.setFont(new Font("Calibri", Font.PLAIN, 22));
		ResultField.setColumns(10);
		JScrollPane sp = new JScrollPane(ResultField);
		sp.setBounds(63, 154, 869, 456);
		frame.getContentPane().add(sp);
						
	}

}
