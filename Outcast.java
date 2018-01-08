import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
private final WordNet wordNet;

   public Outcast(WordNet wordnet)         // constructor takes a WordNet object
   {
	   this.wordNet = wordnet;
   }
   public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
   {
	   int i = 0;
       int[] dist = new int[nouns.length];
       int max = -1;
       int maxIndex = 0;

       for (String nounA : nouns) {
           for (String nounB : nouns) {
               dist[i] += wordNet.distance(nounA, nounB);
           }
           if (dist[i] >= max) {
               maxIndex = i;
               max = dist[i];
           }
           i++;
       }
       return nouns[maxIndex];
   }
   public static void main(String[] args)  // see test client below
   {
	   WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
       Outcast outcast = new Outcast(wordnet);

       String[] filenames = {"outcast5.txt", "outcast8.txt", "outcast11.txt"};
       for (int t = 0; t < filenames.length; t++) {
           In in = new In(filenames[t]);
           String[] nouns = in.readAllStrings();
           StdOut.println(filenames[t] + ": " + outcast.outcast(nouns));
       }
   }
}