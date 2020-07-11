package Sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 3-way radix quicksort for strings. This algorithm works especially good on problems 
 * where the strings have shared prefixes.
 * @author Alex Pokras
 */
public class RadixQuicksort {
	
	/** Return the numeric value of the char at the index d in the string or -1 if d > length of the string. */
	private static int charAt(String s, int d) {
		if (d>=s.length()) return -1;
		return Character.getNumericValue(s.charAt(d));
	}
	
	/** Swap the elements of the array at indices x and y */
	private static void swap (String[] a, int x, int y) {
		String buf = a[x];
		a[x] = a[y];
		a[y] = buf;
	}
	
	/**
	 * Sort the array of strings: 3-way partition it and recursively sort each part.<br>
	 * 3-way partition is the partition of the array [lo,hi] into three subarrays:
	 * [lo,l-1] with elements with dth char less than the partitioning element's dth char,
	 * [l,r] with elements with dth char equal to the partitioning element's dth char,
	 * [r+1,hi] with elements with dth char greater than the partitioning element's dth char.
	 */
	private static void sort (String[] a, int lo, int hi, int d) {
		if (hi<=lo) return;
		int v = charAt(a[lo], d); //partitioning element is a[lo]
		int l = lo, r = hi; 
		int i=lo; //iterates through the array
		
		//Partition the array [lo,hi] into a subarray [lo,l-1] whose elements have t<v,
		//a subarray [l,r] whose elements have t=v, and a subarray [r+1,hi] whose elements have t>v.
		while (i<=r) {
			int t = charAt(a[i], d);
			if (t<v) swap(a, l++, i++);
			else if (t>v) swap(a, i, r--);
			else i++;
		}
		
		//Recursively sort the left subarray
		sort (a, lo, l-1, d);
		//Recursively sort the middle subarray based on chars at the index d+1,
		//because the chars at the indices 0 to d are equal 
		if (v>=0) sort (a, l, r, d+1);
		//Recursively sort the right subarray
		sort (a, r+1, hi, d);
	}
	
	public static void sort (String[] a) {
		sort(a, 0,a.length-1,0);
	}
	
	/** Find the number of syllables in a word (not for sorting), considerng two vowels in a row one syllable. */
	private static int syllables (String s) {
		int n=0;
		boolean prev = false;
		String vowels = "aeiouy";
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			
			if (vowels.indexOf(c) != -1) { //if c is a vowel
				if (!prev) n++;
				prev = true;
			} else prev = false;
		}
		return n;
	}
	
	public static void main(String[] args) {
		//Sort the words in file in alphabetical order
		ArrayList<String> list = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(new File("src/Sorting/Big Iron.txt"));
			while (sc.hasNext()) {				
				sc.useDelimiter("[^a-zA-Z']+");
				String word = sc.next().toLowerCase();
				list.add(word);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String[] a = list.toArray(new String[0]);
		sort(a);
		//System.out.println(Arrays.toString(a));
		
		//Output the result
		int lines=1; //number of this line
		int syllables=0; //syllables in the line so far
		for (String word : a) {
			System.out.print(word+" ");
			syllables += syllables(word);
			int threshold = lines%5==0 ? 5 : 15; //maximum possible amount of syllables on this line
			if (syllables>=threshold) {
				syllables=0;
				System.out.println();
				lines++;
			}
		}
	}

}
