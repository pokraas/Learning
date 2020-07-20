package Week5;
import java.util.Arrays;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * A class for move-to-front encoding and decoding of files
 * using BinaryStdIn and BinaryStdOut.
 * 
 * @author Alex Pokras
 */
public class MoveToFront {
	private static final int R = 256;

	/** apply move-to-front encoding, reading from standard input and writing to standard output */
	public static void encode() {
		char[] a = new char[R]; //sequence of R different characters
		for (int j=0; j<R; j++) a[j] = (char) j;
		while (!BinaryStdIn.isEmpty()) {
			//System.out.println("Starting to encode...");
			char c = BinaryStdIn.readChar();
			int i=0;
			
			//find the index at which c occurs
			while (i<R && a[i]!=c) i++;
			//System.out.println("i "+i);
			BinaryStdOut.write((char)i);
			
			//move chars at indices from 0 to i one index forward
			while (i>0) {
				a[i] = a[i-1];
				i--;
			}
			
			//put c at the beginning of the array
			a[0] = c;
			//System.out.println((char)i);
			//System.out.println(Arrays.toString(a));
		}
		BinaryStdIn.close();
		BinaryStdOut.close();
	}

	/** apply move-to-front decoding, reading from standard input and writing to standard output */
	public static void decode() {
		char[] a = new char[R]; //sequence of R different characters
		for (int j=0; j<R; j++) a[j] = (char) j;
		while (!BinaryStdIn.isEmpty()) {
			//System.out.println("Starting to encode...");
			int i = (int) BinaryStdIn.readChar();
			char c = a[i];
			BinaryStdOut.write(c);
			
			//move chars at indices from 0 to i one index forward
			while (i>0) {
				a[i] = a[i-1];
				i--;
			}
			
			//put c at the beginning of the array
			a[0] = c;
		}
		BinaryStdIn.close();
		BinaryStdOut.close();
	}

	public static void main(String[] args) {
		String s = args[0];
		if (s.equals("-")) encode();
		if (s.equals("+")) decode();
		//System.out.println(Arrays.toString(args));
	}

}
