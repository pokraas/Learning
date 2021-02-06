package Miscellaneous;

public class UAOlympiadD {
	
	static int min(int[,] a, int n) {
		int min = 0;
		int curr = 0; //currently occupied places on the train
		for (int i = 0; i < n; i++) {
			//Exiting
			int exited = 0;
			for (int j = 0; j < n; j++) exited += a[j,i];
			curr -= exited;
			if (curr < 0) curr = 0;
			
			//Entering
			int entered = 0;
			for (int j = 0; j < n; j++) entered += a[i,j];
			curr += entered;
			
			if (min < curr) min = curr;
		}
		for (int i = 0; i < n; i++) {
			//Exiting
			int exited = 0;
			for (int j = 0; j < n; j++) exited += a[j,i];
			curr -= exited;
			if (curr < 0) curr = 0;
			
			//Entering
			int entered = 0;
			for (int j = 0; j < n; j++) entered += a[i,j];
			curr += entered;
			
			if (min < curr) min = curr;
		}
		
		return min;
	}
}
