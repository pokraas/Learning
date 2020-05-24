package Sorting;
import java.util.Arrays;
import java.util.Random;

/**
 * A randomized quicksort implementation for an int array.
 */
public class QuickSort {
	public static void quicksort(int[]a) {
		shuffle(a);
		sort(a,0,a.length-1);
	}
	
	/** Fisher-Yates array shuffle */
	private static void shuffle (int[]a) {
		for (int i=0;i<a.length-1;i++) {
			int j=i+new Random().nextInt(a.length-i);
			swap(a,i,j);
		}
	}
	
	/**
	 * Recursively calls the partition procedure on the subarrays [lo,j-1]
	 * and [j+1,hi] of a, where j is the result of partition()<br>
	 * (i.e., everything to the left of j is less than a[j]<br>
	 * ans everything to the right of j is greater than a[j]).
	 */
	private static void sort(int[]a,int lo, int hi) {
		if (lo>=hi) return;
		int j=partition(a,lo,hi);
		sort(a,lo,j-1);
		sort(a,j+1,hi);
	}
	
	/** <b>Partition</b> the subarray [lo,hi] of a:
	 * <br>move the element p=a[lo] to such an index that
	 * all elements to the left of p are less than p
	 * and all elements to the right of p are greater than p.
	 * <p>Example: the subarray is {<i><b>2</b></i>,0,5,3,1,4}, <b><i>p=2</b></i>,<br>
	 * after partitioning it will turn to {0,1,<i><b>2</i></b>,5,3,4}.
	 * @return the index (j) to which the partitioned element has been moved:<br>
	 * 2 in the example, as <b><i>p=2</b></i> has been moved to the 2nd index.
	 */
	private static int partition(int[]a, int lo, int hi) {
		int p = a[lo];//element on which the array is partitioned
		int i=lo;//left pointer: increases
		int j=hi+1;//right pointer: decreases
		while (true) {
			//increasing i until a[i] reaches an element bigger than p
			while (a[++i]<p) {
				if (i==hi)break;
			}
			
			//decreasing j until a[j] reaches an element smaller than p
			while(a[--j]>p) {
				if (j==lo)break;
			}
			
			//break if pointers cross
			if (i>=j)break;
			
			swap(a,i,j);
		}
		swap(a,lo,j);
		return j;
	}
	
	/** Swaps two elements of the array a on the indices x and y. */
	private static void swap(int[]a, int x, int y) {
		int buf=a[x];
		a[x]=a[y];
		a[y]=buf;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a = new int[] {0,1,2,3,4,5};
		int[] ref = a.clone();
		shuffle(a);
//		shuffle(a);
//		System.out.println(Arrays.toString(a));
//		quicksort(a);
//		System.out.println(Arrays.toString(a));
		for (int i=0;i<6*5*4*3*2*5;i++) {
			quicksort(a);
			if (!Arrays.equals(a, ref)) {
				System.out.println(Arrays.toString(a));
			}
		}

	}

}
