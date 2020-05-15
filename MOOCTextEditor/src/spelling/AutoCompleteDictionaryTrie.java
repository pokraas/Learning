package spelling;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author You
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size=0;
    

    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
	}
	
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should convert the 
	 * string to all lower case before you insert it. 
	 * 
	 * This method adds a word by creating and linking the necessary trie nodes 
	 * into the trie, as described outlined in the videos for this week. It 
	 * should appropriately use existing nodes in the trie, only creating new 
	 * nodes when necessary. E.g. If the word "no" is already in the trie, 
	 * then adding the word "now" would add only one additional node 
	 * (for the 'w').
	 * 
	 * @return true if the word was successfully added or false if it already exists
	 * in the dictionary.
	 */
	public boolean addWord(String word)
	{
		//System.out.println("Adding "+word+" ...");
		char[] letters = word.toLowerCase().toCharArray();
		TrieNode curr = root;
		for (int i=0; i<letters.length-1;i++) {
			char c = letters[i];
			curr = curr.insert(c);
		}
		char lastLetter = letters[letters.length-1];//last letter in word
		TrieNode last = curr.getChild(lastLetter);
		if (last==null || !last.endsWord()) {
			if (last==null) last = curr.insert(lastLetter);
			last.setEndsWord(true);
			size++;
			//printTree();
			return true;
		}	
		//printTree();
		return false;
	}
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
	    //TODO: Implement this method
	    return size;
	}
	
	
	/** Returns whether the string is a word in the trie, using the algorithm
	 * described in the videos for this week. */
	@Override
	public boolean isWord(String word) 
	{
		//System.out.println("Looking up "+word+" ...");
		char[] letters = word.toLowerCase().toCharArray();
		if (letters.length<1) return false;
		TrieNode curr = root;
		for (int i=0; i<letters.length-1;i++) {
			char c = letters[i];
			curr = curr.getChild(c);
			if (curr==null) return false;
		}
		char lastLetter = letters[letters.length-1];//last letter in word
		TrieNode last = curr.getChild(lastLetter);
		if (last==null || !last.endsWord()) {
			//printTree();
			return false;
		}	
		//printTree();
		return true;
		}

	/** 
     * Return a list, in order of increasing (non-decreasing) word length,
     * containing the numCompletions shortest legal completions 
     * of the prefix string. All legal completions must be valid words in the 
     * dictionary. If the prefix itself is a valid word, it is included 
     * in the list of returned words. 
     * 
     * The list of completions must contain 
     * all of the shortest completions, but when there are ties, it may break 
     * them in any order. For example, if there the prefix string is "ste" and 
     * only the words "step", "stem", "stew", "steer" and "steep" are in the 
     * dictionary, when the user asks for 4 completions, the list must include 
     * "step", "stem" and "stew", but may include either the word 
     * "steer" or "steep".
     * 
     * If this string prefix is not in the trie, it returns an empty list.
     * 
     * @param prefix The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to numCompletions best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // TODO: Implement this method
    	 // This method should implement the following algorithm:
    	 // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
    	 //    empty list
    	 char[] letters = prefix.toLowerCase().toCharArray();
 		//if (letters.length<1) return new ArrayList<String>().ensureCapacity(numCompletions);
 		TrieNode curr = root;
 		for (char c:letters) {
 			//explore the trie character by character
 			curr=curr.getChild(c);
 			//if the stem does not appear on the trie
 			if (curr==null) return new LinkedList<String>();
 		}
 		System.out.println(curr.getText());
 		
    	 // 2. Once the stem is found, perform a breadth first search to generate completions
    	 //    using the following algorithm:
    	 //    Create a queue (LinkedList) and add the node that completes the stem to the back
    	 //       of the list.
 		Queue<TrieNode> q = new LinkedList<TrieNode>();
 		q.add(curr);
    	 //    Create a list of completions to return (initially empty)
 		LinkedList<String> completions = new LinkedList<String>();
    	 //    While the queue is not empty and you don't have enough completions:
 		while(!q.isEmpty() && completions.size()<numCompletions) {
    	 //       remove the first Node from the queue
 			TrieNode head = q.remove();
    	 //       If it is a word, add it to the completions list
 			if (head.endsWord()) completions.add(head.getText());
    	 //       Add all of its child nodes to the back of the queue
 		
 			q.addAll(head.getChildren().values());
 		
 		}
         return completions;
     }

 	// For debugging
 	public void printTree()
 	{
 		printNode("",root);
 		System.out.println("--------------");
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(String s, TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		
 		System.out.println(s+curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(s+"|   ",next);
 		}
 	}
 	

	
}