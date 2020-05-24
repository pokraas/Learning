package Sorting;

import java.util.Arrays;
import java.util.Random;

/**
 * A randomized quicksort implementation with 3-way partition for an int array.
 * Particularly useful for arrays with lots of similar elements.
 */
public class QuickSort_3WayPartition {
	public static void quicksort(int[]a) {
		shuffle(a);
		sort(a,0,a.length-1);
	}
	
	/** Fisher-Yates array shuffle: gives performance guarantee */
	private static void shuffle (int[]a) {
		for (int i=0;i<a.length-1;i++) {
			int j=i+new Random().nextInt(a.length-i);
			swap(a,i,j);
		}
	}
	
	/**
	 * The last two lines of code recursively 3-way partition the subarrays [lo, l-1] and [r+1, hi] of a,<br>
	 * where all elements of [lo, l-1] < all (equal to each other) elements of [l, r] < all elements of [r+1, hi]
	 * 
	 * <p>The while loop <b>3-way partitions</b> the subarray [lo, hi] of a:
	 * move the element p=a[lo] between such indices l and r that
	 * all elements to the left of l are less than p,
	 * all elements to the right of r are greater than p,
	 * and all elements between l and r are equal to p.
	 * 
	 * <p>Example: the subarray is {<i><b>2,2</b></i>,0,5,3,1,<b><i>2</b></i>,4}, <b><i>p=2</b></i>,<br>
	 * after partitioning it will turn to {0,1,<i><b>2,2,2,</i></b>,5,3,4}; l=2, r=4.
	 */
	private static void sort(int[]a,int lo, int hi) {
		if (lo>=hi) return;
		
		int l = lo;
		int r = hi;
		int i=lo;
		int p=a[lo]; //the element on which to perform partition
		
		//when i>r, all elements of the subarray have been checked
		while(i<=r) {
			
			//if the element < p, move it to the left part of the subarray,
			//increase i and the left part of the subarray (l++)
			if (a[i]<p) swap(a,l++,i++);
			
			//if the element > p, move it to the right part of the subarray,
			//increase the right part of the subarray (r--)
			else if (a[i]>p) swap(a,r--,i);
			
			//if the element = p, leave it in place (increase i)
			else i++;
			
		}
		
		sort(a,lo,l-1);
		sort(a,r+1,hi);
	}
	
	/** Swaps two elements of the array a on the indices x and y. */
	private static void swap(int[]a, int x, int y) {
		int buf=a[x];
		a[x]=a[y];
		a[y]=buf;
	}
	
	public static void main(String[] args) {
		int[] a = new int[] {0,1,2,3,4,5};
		int[] ref = a.clone();
		shuffle(a);
//		shuffle(a);
//		System.out.println(Arrays.toString(a));
//		quicksort(a);
//		System.out.println(Arrays.toString(a));
		
		//check for all permutations of a * 5 times
//		for (int i=0;i<6*5*4*3*2*5;i++) {
//			quicksort(a);
//			if (!Arrays.equals(a, ref)) {
//				System.out.println(Arrays.toString(a));
//			}
//		}
		compareSorts(5,10000000,1000000);
	}
	
	/**
	 * Compares mergesort and quicksort implementations without and with 3-way partition (this implementation):
	 * creates an array, prints its length and sorting times for all three implementations 
	 * @param differentElements number of different elements in the array<br>
	 * (3-way partition works way better than common partition for arrays with lots of equal elements)
	 * @param maxLength max length of array created
	 * @param step step in length of created arrays: from 0 to maxLength
	 */
	public static void compareSorts(int differentElements,int maxLength,int step) {
		for (int length=step;length<=maxLength;length+=step) {
			System.out.print(length+"\t");
			int[]a = new int[length];
			
			for (int i=0;i<length;i++) {
				a[i]=new Random().nextInt(differentElements);
			}
			int[]b = a.clone();
			int[]c = a.clone();
			
			long start0 = System.nanoTime();
			MergeSort.mergeSort(c);
			long t0 = System.nanoTime()-start0;
			System.out.print(t0/1000000000d+"\t");
			
			long start1 = System.nanoTime();
			QuickSort.quicksort(a);
			long t1 = System.nanoTime()-start1;
			System.out.print(t1/1000000000d+"\t");
			
			long start2 = System.nanoTime();
			QuickSort_3WayPartition.quicksort(b);
			long t2 = System.nanoTime()-start2;
			System.out.println(t2/1000000000d);
			
			if (!Arrays.equals(a, b) || !Arrays.equals(b, c)) throw new IllegalArgumentException();
		}
	}

}
