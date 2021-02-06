package Miscellaneous;

public class UAOlympiadE {
	//n2 = 2 * n, v should already be sorted
	int[] b(int[] v, int n2) {
		int[] b = new int[n2];
		int j = 0; //index of the next free space in b
		for (int c = 1; c < v[0]; c++) {
			b[j] = c;
			j++;
		}
		
		for (int i = 1; i < n2; i++) {
			for (int c = v[i - 1] + 1; c < v[i]; c++) {
				b[j] = c;
				j++;
			}
			
		}
		
		for (int c = v[n2 - 1] + 1; c < n2; c++) {
			b[j] = c;
			j++;
		}
		return b;
	}
	
	public static void main(String[] args) {
		
	}
}
