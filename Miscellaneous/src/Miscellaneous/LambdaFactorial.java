package Miscellaneous;

import java.util.function.*;
public class LambdaFactorial {
	private static IntFunction<Integer> fact;
	
	public static void main(String[] args) {
		//Factorials
		fact = n -> n==0 ? 1 : n*fact.apply(n-1); 
		System.out.println(fact.apply(10));
		
		//Fibonacci numbers
		fact = n -> n==0 ? 0 :
					n==1 ? 1 : fact.apply(n-1)+fact.apply(n-2);
		System.out.println(fact.apply(10));
		
	}
}
