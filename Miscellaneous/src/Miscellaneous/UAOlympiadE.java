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
	
	//Maximize the points for w (winner), return how many points the winner gets.
	//The winner and loser arrays should be already sorted and of length n.
	static int maximize(int[] w, int[] l, int n) {
		int p = 0; //winner's points
		int j = 0; //index in l
		for (int i = 0; i < n; i++) { //i - index in w
			if (w[i] > l[j]) {
				j++;
				p++;
			}
		}
		return p;
	}
	
	public static void main(String[] args) {
		int n = 3;
		int[] vcard = new int[]{4, 5, 6};
		int[] bcard = b(vcard,n);
		System.out.println(Arrays.toString(bcard));
		System.out.println();
		System.out.println(maximize(vcard, bcard, n));
	}
}
