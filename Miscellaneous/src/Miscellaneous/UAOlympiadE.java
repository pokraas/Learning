package Miscellaneous;

import java.util.Arrays;

public class UAOlympiadE {
	//v should already be sorted, v should be of length n
	static int[] b(int[] v, int n) {
		int[] b = new int[n];
		int j = 0; //index of the next free space in b
		for (int c = 1; c < v[0]; c++) {
			b[j] = c;
			j++;
		}
		
		for (int i = 1; i < n; i++) {
			for (int c = v[i - 1] + 1; c < v[i]; c++) {
				b[j] = c;
				j++;
			}
			
		}
		
		for (int c = v[n - 1] + 1; c <= 2 * n; c++) {
			b[j] = c;
			j++;
		}
		return b;
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(b(new int[]{4, 5, 6},3)));
	}
}
