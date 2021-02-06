package Miscellaneous;

import java.util.Scanner;

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
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[] a = new int[n];
		int[] b = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = sc.nextInt();
			b[i] = sc.nextInt();
		}
		int x = sc.nextInt();
		int y = sc.nextInt();
		sc.close();
		System.out.println(maxmood(a, b, n, x, y));
	}
}
