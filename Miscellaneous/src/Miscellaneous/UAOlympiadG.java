package Miscellaneous;

public class UAOlympiadG {
	
	static int min(int[][] a, int n) {
		int min = 0;
		int curr = 0; //currently occupied places on the train
		for (int i = 0; i < n; i++) {
			//Exiting
			int exited = 0;
			for (int j = 0; j < n; j++) exited += a[j][i];
			curr -= exited;
			if (curr < 0) curr = 0;
			
			//Entering
			int entered = 0;
			for (int j = 0; j < n; j++) entered += a[i][j];
			curr += entered;
			
			if (min < curr) min = curr;
		}
		for (int i = 0; i < n; i++) {
			//Exiting
			int exited = 0;
			for (int j = 0; j < n; j++) exited += a[j][i];
			curr -= exited;
			if (curr < 0) curr = 0;
			
			//Entering
			int entered = 0;
			for (int j = 0; j < n; j++) entered += a[i][j];
			curr += entered;
			
			if (min < curr) min = curr;
		}
		
		return min;
	}
	
	public static void main(String[] args) {
		int n = 3;
		int[][] a = new int [][] {
			{0,2,3},
			{2,0,4},
			{3,0,0},
		};
		System.out.println(min(a,n));
		
		//TODO: read the matrix and feed it to the function!
	}
}
