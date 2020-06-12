package Week1;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {
	/** Contains hypernym relations between synsets as edges in a directed graph
	 * in which the vertex's number = noun's id */
	private SAP sap;
	/** Maps an id to each synset (represented as a whole string)*/
	private HashMap<Integer, String> synsets;
	/** Maps a noun to its ids (represented as a HashSet of integers)*/
	private HashMap<String, HashSet<Integer>> nouns;
	
	/** Constructor takes the name of two input files */
	public WordNet(String synsetsFile, String hypernymsFile) {
		if (synsetsFile==null || hypernymsFile==null) throw new IllegalArgumentException();
		In synsetsIn = new In(synsetsFile);
		In hypernymsIn = new In(hypernymsFile);

		//Create and fill the maps: synsets and nouns
		synsets = new HashMap<Integer, String>();
		nouns = new HashMap<String, HashSet<Integer>>();
		
		for (String line : synsetsIn.readAllLines()) {
				String[] lineArray = line.split(",");
				int id = Integer.parseInt(lineArray[0]);
				
				//parse lineArray[1] into synsetArray of nouns
				String[] synsetArray = lineArray[1].split(" ");
				//Map a noun to its ids (HashSet of ids)
				for (String noun : synsetArray) {
					//if noun already in the map, add the id to noun's ids
					if (nouns.containsKey(noun)) nouns.get(noun).add(id);
					//create a new HashSet of ids for the newly-found noun and add the id there
					else nouns.put(noun, new HashSet<Integer>(Arrays.asList(id)));
					
				}
				
				//Map id to the synset (HashSet of nouns)
				synsets.put(id, lineArray[1]);
		}
		
		//Create and fill the graph of the SAP
		Digraph g = new Digraph(this.size());
		for (String entry : hypernymsIn.readAllLines()) {
			//add hypernyms to each node
			String[] line = entry.split(",");
			int from = Integer.parseInt(line[0]);
			for (int i=1;i<line.length;i++) {
				int to = Integer.parseInt(line[i]);
				g.addEdge(from, to);
			}
		}
		sap = new SAP(g);
	}
	
	/** Number of entries (and different ids) in this WordNet */
	private int size() {
		return synsets.keySet().size();
	}
	
	/** Returns all WordNet nouns */
	public Iterable<String> nouns() {
		return nouns.keySet();
	}

	/** Is the word a WordNet noun? */
	public boolean isNoun(String word) {
		if (word==null) throw new IllegalArgumentException();
		return nouns.containsKey(word);
	}

	/** Distance between nounA and nounB (length of the shortest ancestral path) */
	public int distance (String nounA, String nounB) {
		if (nounA==null || nounB==null || !isNoun(nounA) || !isNoun(nounB))
				throw new IllegalArgumentException();
		return sap.length(nouns.get(nounA), nouns.get(nounB));
	}
	
	/** A synset that is the common ancestor of nounA and nounB in a shortest ancestral path*/
	public String sap (String nounA, String nounB) {
		if (nounA==null || nounB==null || !isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException();
		int ancestorId = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
		return synsets.get(ancestorId);
	}
	
	public static void main(String[] args) {
		WordNet w = new WordNet("Week1/synsets.txt","Week1/hypernyms.txt");
		System.out.println(w.sap("Aachen", "Kentucky"));
	}

}
