package Week4;
import java.util.Arrays;
import java.util.HashSet;

import edu.princeton.cs.algs4.In;

/** A class to find all valid words (words in the dictionary)
 * given a board of a Hasbro's Boggle game. 
 * @author Alex Pokras
 */
public class BoggleSolver
{
	private static final int ALPHABET_LENGTH = 26; 
	private String[] dictionary;
	/** Root of the trie that represents the dictionary */
	private DictNode dictionaryRoot;
	
	/** Initializes the data structure using the given array of strings as the dictionary.
	 * (Assumption: each word in the dictionary contains only the uppercase letters A through Z.) */
	public BoggleSolver(String[] dictionary) {
		this.dictionary = Arrays.copyOf(dictionary, dictionary.length);
		this.dictionaryRoot = new DictNode();
		//Fill the dictionary trie
		newWord:
		for (int i=0; i<dictionary.length; i++) {
			String word = dictionary[i];
			//don't include words shorter than 3 letters into the trie
			if (word.length()<3) {
				this.dictionary[i]=null;
				continue; 
			}
			DictNode curr = dictionaryRoot;
			for (int j=0; j<word.length(); j++) {
				char c = word.charAt(j);
				
				//Qu special case
				if (c=='Q') {
					//if Q is the last letter
					if (j==word.length()-1) {
						this.dictionary[i]=null;
						continue newWord; 
					}
					//if Q is followed by something other than U
					if (word.charAt(j+1)!='U') {
						this.dictionary[i]=null;
						continue newWord;
					}
					else j++;
				}
				
				DictNode next = curr.children[charToInt(c)];
				if (next==null) {
					next = new DictNode();
					curr.children[charToInt(c)] = next;
				}
				curr = next;
			}
			curr.endsWord=true;
		}
	}
	
	/** A node in an R-way dictionary trie. Each node represents a character. */
	private class DictNode {
		DictNode[] children;
		/** Is this Node the last in the word? */
		boolean endsWord;
		DictNode(){
			children = new DictNode[ALPHABET_LENGTH];
			endsWord = false;
		}
		
		DictNode getChild(char letter){
			return children[charToInt(letter)];
		}
		
		@Override
		public String toString() {
			String s = ">";
			for (int i=0; i<children.length; i++) {
				if (children[i] != null) s+=(intToChar(i))+" ";
			}
			return s;
		}
	}
	
	/** The number of the capital letter in the English alphabet */
	private static int charToInt(char letter) {
		return Character.getNumericValue(letter) - Character.getNumericValue('A');
	}

	/** The number of the capital letter in the English alphabet */
	private static char intToChar(int number) {
		return (char)(number+'A');
	}
	
	/** 
	 * Create an array that contains a HashSet for each letter. The HashSet contains all
	 * x- or y- coordinates on the board where the letter occurs.
	 * @param board the BoggleBoard
	 * @param axis true for x-axis, false for y-axis
	 */
	private HashSet<Dice>[] initializeMap(BoggleBoard board) {
		HashSet<Dice>[] array = (HashSet<Dice>[]) new HashSet[ALPHABET_LENGTH];
		for (int i=0; i<array.length; i++) array[i] = new HashSet<Dice>();
		
		for (int i=0; i<board.cols(); i++) {
			for (int j=0; j<board.rows(); j++) {
				char c = board.getLetter(j, i);
				array[charToInt(c)].add(new Dice(i, j));
			}
		}
		return array;
	}
	
	/** A class to store two coordinates of a dice on the board. */
	private class Dice {
		int x,y;
		boolean visited;
		
		Dice(int x, int y){
			this.x=x;
			this.y=y;
			this.visited=false;
		}
		
		boolean isAdjacentTo(Dice other) {
			if (Math.abs(x - other.x) > 1) return false;
			if (Math.abs(y - other.y) > 1) return false;
			return true;
		}
		
		@Override
		public String toString() {
			return "("+x+" "+y+")";
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(x)+Integer.hashCode(y);
		}
	}
	
	private HashSet<Dice>[] map;
	private HashSet<String> validWords;
	private int searchCalled=0;
	
	private void search (String word, int offset, Dice curr) {
		searchCalled++;
		char c = word.charAt(offset);
//		System.out.println("curr: "+c+curr);
		if (offset==word.length()-1) {
			validWords.add(word);
			return;
		}
		curr.visited = true;
		
		char d = word.charAt(offset+1);
		//Qu special case
		if (c=='Q' && d=='U') {
			if (offset==word.length()-2) {
				validWords.add(word);
				return;
			}
			offset++;
			d = word.charAt(offset+1);
		}
		
		for (Dice next : map[charToInt(d)]) {
//			System.out.println("next: "+d+next);
			if (curr.isAdjacentTo(next) && !next.visited) {
				search(word, offset+1, next);
			}
		}
		curr.visited = false;
	}
	
	/** Returns the set of all valid words in the given Boggle board, as an Iterable. */
	public Iterable<String> getAllValidWords(BoggleBoard board){
		validWords = new HashSet<String>();
		map = initializeMap(board);
		
		//search for each dictionary word on the board
		//TODO optimize: use not the String[] representation of the dictionary, but the dictionary trie. 
		for (String word : dictionary) {
			if (word==null) continue;
			for (Dice first : map[charToInt(word.charAt(0))]) {
				search(word, 0, first);
			}
		}
		return validWords;
	}


	/** Returns the score of the given word if it is in the dictionary, zero otherwise.
	 * (Assumption: the word contains only the uppercase letters A through Z.) */
	public int scoreOf(String word) {
		int l = word.length();
		if (l<3) return 0; 
		DictNode curr = dictionaryRoot;
		
		//iterate through all the chars of the string
		for (int i=0; i<word.length(); i++) {
			char c = word.charAt(i);
			if (c=='Q') i++;
			int n = charToInt(c);
			
			//find nth child of the current node
			curr = curr.children[n];
			if (curr==null) return 0;
		}
		if (!curr.endsWord) return 0;
		
		//find the score of the word
		if (l<5) return 1;
		if (l==5) return 2;
		if (l==6) return 3;
		if (l==7) return 5;
		return 11;
	}
	

	
	public static void main (String[] args) {
		In in = new In("src/Week4/dictionary-yawl.txt");
		String[] dictionary = in.readAllLines();
		in.close();
		BoggleSolver bs = new BoggleSolver(dictionary);
		//System.out.println(Arrays.toString(bs.dictionary));
		System.out.println();
		System.out.println(bs.dictionaryRoot);
		System.out.println();
		
		BoggleBoard board = new BoggleBoard(new char[][] {
//			{'A','L','E','X'}
//		});
			{'A','B','A'},
			{'E','M','N'},
			{'T','O','P'}
		});
		
		bs.map = bs.initializeMap(board);
		bs.validWords = new HashSet<String>();
//		
//		System.out.println(Arrays.toString(bs.map));
//		for (Dice dice : bs.map[charToInt('Q')]) {
//			System.out.println("Searching from "+dice);
//			bs.search("QUQU", 0, dice);
//		}
//		System.out.println(bs.validWords);
		System.out.println(bs.getAllValidWords(board));
	}
}
