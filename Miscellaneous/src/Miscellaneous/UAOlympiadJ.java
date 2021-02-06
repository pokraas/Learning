package Miscellaneous;

import java.util.Arrays;

public class UAOlympiadJ {
	
	//n = length of b = length of a;
	//i = number of the mountain which Mustache is standing on
	static int mood(int[] a, int[] b, int i, int n, int x, int y) {
		boolean[] s = new boolean[n]; //s[n] = true if Mustache sees the nth mountain
		s[i] = true;
		
		//Look forward
		int max = 0;
		for (int j = i + 1; j < n; j++) {
			if (max >= a[j]) continue;
			max = a[j];
			s[j] = true;
		}
		
		//Look back
		max = 0;
		for (int j = i - 1; j >= 0; j--) {
			if (max >= a[j]) continue;
			max = a[j];
			s[j] = true;
		}
				
		//Count the seen mountains
		int mood = -x * a[i];
		for (int j = 0; j < n; j++) {
			if (s[j]) mood += y * b[j];
		}
		return mood;
	}
	
	static int maxmood(int[] a, int[] b, int n, int x, int y) {
		int maxmood = Integer.MIN_VALUE; //or whatever you call it in C#
		for (int i = 0; i < n; i++) {
			int mood = mood(a, b, i, n, x, y); 
			if (mood > maxmood) maxmood = mood; 
		}
		return maxmood;
	}
	
	public static void main(String[] args) {
		int[] a = new int[]{5,1,4,3,3,5};
		int[] b = new int[]{3,5,5,2,2,1};
		int x=1, y=2;

		System.out.println(maxmood(a, b, 6, x, y));
	}
}
