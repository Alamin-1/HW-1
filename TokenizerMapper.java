import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class TokenizerMapper 
       extends Mapper<Object, Text, Text, IntWritable>{
    IntWritable val = new IntWritable(1);
    private Text word = new Text();
    private Text fWord = new Text();
    Text chapter = new Text("CHAPTER");
    int chapterNo = 10;
    // Initializing stop words list array.
    String wordsSet[]={"a","about","above","all","am","an","and","any","are","arent","as","at","be","because","been","before","being","below","between","both","but","by","cant","cannot","could","couldnt","did","didnt","do","does","doesnt","doing","dont","each","few","for","from","further","had","hadnt","has","hasnt","have","havent","having","he","hed","hell","hes","her","here","heres","hers","herself","him","himself","his","how","hows","i","id","ill","im","ive","if","in","into","is","isnt","it","its","its","itself","lets","me","more","most","mustnt","my","myself","no","nor","not","of","off","on","once","only","or","other","ought","our","ours	ourselves","out","over","own","shant","she","shed","shell","shes","should","shouldnt","so","some","such","than","that","thats","the","their","theirs","them","themselves","then","there","theres","these","they","theyd","theyll","theyre","theyve","this","those","through","to","too","under","until","up","very","was","wasnt","we","wed","well","were","weve","were","werent","what","whats","when","whens","where","wheres","which","while","who","whos","whom","why","whys","will","with","wont","would","wouldnt","you","youd","youll","youre","youve","your","yours","yourself","yourselve"};
    // Puting those words in Hash set.
    HashSet<String> stopWords = new HashSet<String>(Arrays.asList(wordsSet));
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	
    	String line = value.toString();
    	// Removing punctuation and space with empty.
    	line = line.replaceAll("[^a-zA-Z ]", "");
    	// Separating each work using tokenizer.
      StringTokenizer itr = new StringTokenizer(line);
      while (itr.hasMoreTokens()) {  
    	  String newWord = itr.nextToken();
	    
	     if(!newWord.equals("CHAPTER")){
	    	 // Converting all word to lowercase to ease the counting job 
	    	 // except capital CHAPTER as it represent new chapter.
	    	newWord = newWord.toLowerCase();
	     }
	    
	     word.set(newWord);
	     
	    	 // Checking and removing stop words from map.
		     if(!stopWords.contains(word.toString())){
			        if(word.equals(chapter) ){
			        	chapterNo++;
			       }
			        if(chapterNo>10){
		        val = new IntWritable(1);
		        IntWritable chapNumber = new IntWritable(chapterNo);
		        if(!word.equals(chapter)){
		        	// Adding chapter number for counting words in each chapter alone.
		        	fWord = new Text(word + chapNumber.toString());
		        }
		        else
		        	fWord = new Text(word);	
		        context.write(fWord, val);
			     }
	    	 }
	   }
    }
  }
  