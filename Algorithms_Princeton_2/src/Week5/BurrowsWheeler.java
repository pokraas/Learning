package Week5;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

	/** apply Burrows-Wheeler transform,
	 * reading from standard input and writing to standard output */
	public static void transform() {
		StringBuilder sb = new StringBuilder();
		while (!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar();
			sb.append(c);
		}
		BinaryStdIn.close();
		String s = sb.toString();
		int l = s.length();
		
		CircularSuffixArray a = new CircularSuffixArray(s);
		int first=-1;
		LinkedList<Character> list = new LinkedList<Character>();
		
		for (int i=0; i<l; i++) {
			int pos = a.index(i);
			char c = pos<1 ? s.charAt(l-1) : s.charAt(pos-1);
			list.add(c);
			if (pos==0) first=i;
		}
		
		assert(first!=-1);
		BinaryStdOut.write(first);
		for (char c : list) BinaryStdOut.write(c);
		BinaryStdOut.close();
	}

	
	/** Recover the original string
	 * @param ends array of last chars in suffixes (last column in the CSA)
	 * @param next if a jth original suffix is on ith place in the CSA,
	 * the j+1th original suffix is on next[i]th placs in the CSA
	 * @param first position of the original string in the CSA
	 * @return the original string's characters as a LinkedList
	 */
	private static LinkedList<Character> recover(char[]ends, int[]next, int first) {
		LinkedList<Character> ret = new LinkedList<Character>();
		Arrays.sort(ends);
		ret.add(ends[first]);
		
		int i=first;
		int j=1;
		while (j<ends.length) {
			j++;
			i = next[i];
			ret.add(ends[i]);
		}
		return ret;
	}

	/** apply Burrows-Wheeler inverse transform,
	 * reading from standard input and writing to standard output */
	public static void inverseTransform() {
		//read data from input
		int first = BinaryStdIn.readInt();
		String input = BinaryStdIn.readString();
		int l = input.length();
		char[] ends = input.toCharArray();
		char[] starts = input.toCharArray();
		Arrays.sort(starts);
		int[] next = new int[l];
		
		//maps a character to its occurrences in ends[] queued from first to last
		HashMap<Character,Queue<Integer>> m = new HashMap<Character, Queue<Integer>>();
		for (int i=0; i<l; i++) {
			char c = ends[i];
			m.putIfAbsent(c, new LinkedList<Integer>());
			m.get(c).add(i);
		}
		
		//create the next[] array
		for (int i=0; i<l; i++) {
			char start = starts[i];
			next[i] = m.get(start).poll();
		}
		
		LinkedList<Character> out = recover(ends, next, first);
		for (char c : out) BinaryStdOut.write(c);
		BinaryStdIn.close();
		BinaryStdOut.close();
	}

	/** if args[0] is "-", apply Burrows-Wheeler transform
	 * if args[0] is "+", apply Burrows-Wheeler inverse transform */
	public static void main(String[] args) {
		String s = args[0];
		if (s.equals("-")) transform();		
		if (s.equals("+")) inverseTransform();		
	}

}

