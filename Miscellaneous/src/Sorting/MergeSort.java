package Sorting;
import java.util.Arrays;

public class MergeSort {
	
	public static void mergeSort(int[]a) {
		int n = a.length;
		if (n==1) return;
		//dividing a into temporary arrays l and r
		int mid = n/2;
		int[]l = new int[mid];
		int[]r = new int[n-mid];
		for (int i=0;i<mid;i++) {
			l[i]=a[i];		
		}
		System.out.println(Arrays.toString(l));
		for (int i=mid;i<n;i++) {
			r[i-mid]=a[i];		
		}
		System.out.println(Arrays.toString(r));
		//recursively dividing l and r
		mergeSort(l);
		mergeSort(r);
		//merging l and r back into array a
		merge (a,l,r);
	}
	
	private static void merge(int[] a, int[] l, int[] r) {
		int leftLength = l.length;
		int rightLength = r.length;
		int i=0,j=0,k=0; //indices in l,r, and a
		while(i<leftLength && j<rightLength) {
			if (l[i]<r[j]) {	//comparing ith element of l and jth element of r
				a[k]=l[i];	//writing ith element of l into a
				k++;
				i++;
			} else {
				a[k]=r[j];  //writing jth element of r into a
				k++;
				j++;
			}
		}
		//at this point, one of the arrays (l or r) is completely merged into a
		//writing the rest of the remaining array (r or l) into a:
		while(i<leftLength) {
			a[k]=l[i];
			k++;
			i++;
		}
		while(j<rightLength) {
			a[k]=r[j];
			k++;
			j++;
		}
	}

	public static void main(String[] args) {
		int[] a = {10,5,2,4,18,6,15};
		mergeSort(a);
		System.out.println(Arrays.toString(a));
	}

}
