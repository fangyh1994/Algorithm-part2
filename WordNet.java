import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
	private int v;
	private String[] synsetArray; // List of noun synsets
	private SET<String>[] adjNouns;
	private HashSet<String> wordnouns;
	private HashMap<String,SET<Integer>> map;
	private Digraph g;
	private SAP sap;
	
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
	   if (synsets == null || hypernyms == null) {
		   throw new NullPointerException();
	   }
	   wordnouns = new HashSet<String>();
	   In insynsets = new In(synsets);
	   
	   String line;
	   while ((line = insynsets.readLine()) != null && !line.trim().isEmpty()) {
		   v++;
	   }
	   synsetArray = new String[v];
	   adjNouns = (SET<String>[])new SET[v];
	   map = new HashMap<String,SET<Integer>>();
	   
	   insynsets = new In(synsets);
	   for (int i = 0; i < v; i++) {
		   String synset = insynsets.readLine().split(",")[1]; //split the line into synonym set
		   synsetArray[i] = synset;
		   String[] synonym = synset.split(" ");//split the synset into different synonyms;
		   adjNouns[i] = new SET<String>();
		   
		   for (String temp:synonym) {
			   adjNouns[i].add(temp);
			   SET<Integer> set = new SET<Integer>();
			   set.add(i);
			   map.put(temp,set);
		   }
	   }
	   for (int i = 0; i < v; i++) {
           for (String noun : adjNouns[i]) {
        	   wordnouns.add(noun);
           }
       }
	   
	   g = new Digraph(v);
       In inHypernyms = new In(hypernyms);
       while ((line = inHypernyms.readLine()) != null && !line.trim().isEmpty()) {
           String[] numbers = line.split(",");
           int v = Integer.parseInt(numbers[0]);
           for (String number : numbers) {
               int w = Integer.parseInt(number);
               if (v != w) g.addEdge(v, w);
           }
       }
       //The constructor should throw a java.lang.IllegalArgumentException
       //if the input does not correspond to a rooted DAG
       int root_num = 0;
       for (int i = 0; i < v; i++) {
    	   if(!g.adj(i).iterator().hasNext()) {
    		   root_num++;
    	   }
       }
       if (root_num != 1) {
    	   throw new IllegalArgumentException();
       }
       sap = new SAP(g);
   }

   // returns all WordNet nouns
   
   public Iterable<String> nouns(){
	   return wordnouns;
   }
   

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
	   if (word == null) {
		   throw new NullPointerException();
	   }
	   return wordnouns.contains(word);
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
	   if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
       return sap.length(map.get(nounA), map.get(nounB));
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
	   if (!isNoun(nounA) || !isNoun(nounB)) {
		   throw new IllegalArgumentException();
	   }
	   return synsetArray[sap.ancestor(map.get(nounA), map.get(nounB))];
   }

   // do unit testing of this class
   public static void main(String[] args) {
	   WordNet wordnet = new WordNet("synsets11.txt","hypernyms11ManyPathsOneAncestor.txt");
	   StdOut.println(wordnet.sap("b","c"));
	   StdOut.println(wordnet.distance("b", "c"));
	   //WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
       //StdOut.println(wordnet.sap("grappling_hook", "order_Nudibranchia"));
       //StdOut.println(wordnet.distance("grappling_hook", "order_Nudibranchia"));
   }
}