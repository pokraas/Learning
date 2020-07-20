package Week5;
import java.util.Random;

/**
 * A class that creates a sorted circular suffix array of a string's suffixes.
 * @author Alex Pokras
 */
public class CircularSuffixArray {
	/** Let i be the index of a suffix in the unsorted suffix array. Then, index[i] is
	 * the index of that suffix in the sorted suffix array.
	 */
	private int[] index;
	/** Length of the string */
	private final int l;
	
	/** Create a circular suffix array from a string s */
	public CircularSuffixArray(String s) {
		if (s == null)
			throw new IllegalArgumentException();
		l = s.length();
		
		index = new int[l];
		for (int i=0; i<l; i++) index[i] = i;
		shuffle(index);
		quicksort(index, s, 0, l-1);
	}
	
	/** Fisher-Yates array shuffle */
	private void shuffle (int[]a) {
		for (int i=0;i<a.length-1;i++) {
			int j=i+new Random().nextInt(a.length-i);
			swap(a,i,j);
		}
	}
	
	/** Quicksort the index[] array, comparing two integers not with x>y, but with compare(x,y,ss) */
	private void quicksort(int[] index, String s, int lo, int hi) {
		if (lo >= hi)
			return;
		int j = partition(index, s, lo, hi);
		quicksort(index, s, lo, j-1);
		quicksort(index, s, j+1, hi);		
	}
	
	private int partition(int[] index, String s, int lo, int hi) {
		if (lo>=hi) return hi;
		int p = index[lo]; //partitioning element
		int l = lo; //left pointer: increases
		int r = hi+1; //right pointer: decreases
		while (true) {
			//find the next suffix that is bigger than suffix with index p in the left part of the array
			while (l < hi) {
				l++;
				int x = index[l];
				if (compare(x,p,s)) break;
			}
			
			//find the next suffix that is smaller than suffix with index p in the right part of the array
			while (r > lo) {
				r--;
				int y = index[r];
				if (compare(p,y,s)) break;
			}
			
			//break if pointers cross
			if (l>=r) break;
			
			swap(index,l,r);
		}
		//put p into its place
		swap(index, lo, r);
		//System.out.println(lo+"--"+r+"->"+hi);
		return r;
	}
	
	/** Compare the circular suffix of the String s starting at the xth char to one starting at the yth char.
	 * @return false if the suffix starting at the yth char is smaller, true otherwise
	 */
	private boolean compare(int x, int y, String s) {
		if (x==y) return true;
		int i=0;
		while (s.charAt((x+i)%l)==s.charAt((y+i)%l)) {
			if (i>l) return true;
			i++;
		}
		return s.charAt((x+i)%l) > s.charAt((y+i)%l);
	}
	
	/** Swap a[x] and a[y] */
	private void swap(int[] a, int x, int y) {
		int buf = a[x];
		a[x] = a[y];
		a[y] = buf;
	}

	/** Length of the string */
	public int length() {
		return l;
	}

	/**
	 * Let i be the index of a suffix in the unsorted suffix array. Then, index(i) is
	 * the index of that suffix in the sorted suffix array.
	 */
	public int index(int i) {
		if (i < 0 || i >= l)
			throw new IllegalArgumentException();
		return index[i];
	}

	// unit testing (required)
	public static void main(String[] args) {
		String s = "couscous";
		CircularSuffixArray c = new CircularSuffixArray(s);
		
		for (int i=0;i<s.length();i++) System.out.print(c.index(i)+" ");

		
	}

}