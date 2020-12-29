package Miscellaneous;

public class UAOlympiadLastTask {
	public static long numPairs(long n) {
		long numPairs = 0;
		for (long i = (long) Math.pow(10, n-1); i < Math.pow(10, n) - Math.pow(10, n - 1); i++) {
			long x = i;
			long numPairsToX = 1;
			while (x > 9) {
				long lastDigit = x % 10;
				numPairsToX *= (10 - lastDigit);
				x /= 10;
			}
			numPairsToX *= x; 
			numPairs += numPairsToX;
		}
		return numPairs;
	}
	
	public static void main(String[] args) {
		for (int n = 1; n < 6; n++)
			System.out.println("n: "+n+", number of pairs: "+numPairs(n));
	}
}
