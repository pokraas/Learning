package Parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
/**
 * Evaluates an arithmetical expression passed in as String
 * using Dijkstra's shunting-yard algorithm.
 * @author alexanderpokras
 */
public class ExpressionEvaluator {
	
	private static ArrayList<String> a = new ArrayList<String> (Arrays.asList( 
			new String[]{
					"-","3","*","(","2","*","sqrt(","2","^","2",")","-","6",")"
	}));	//for testing
	
	private static Stack<Operator> ops = new Stack<Operator>();
	private static Stack<Double> vals = new Stack<Double>();
	
	/**
	 * @return true if the parameter is a digit, a comma or a point
	 */
	private static boolean isPartOfNumber(char c) {
		if (c==',') c='.';
		return Character.isDigit(c)||c=='.';
	}
	
	/**
	 * Parses the expression into the <i><b>ArrayList<String> a</i></b> of numbers and operators,
	 * each in a separate entry.
	 */
	private static void parseExpression(String expression) {

		a = new ArrayList<String>(1);
		char[] charArray = expression.toCharArray();
		StringBuilder sb = new StringBuilder();
		int i=0;
		//if the expression starts with a minus sign
		if (charArray[0]=='-') a.add(i++,"0");
		sb.append(charArray[0]);
		
		for (int j=1; j<charArray.length;j++) {
			char curr = charArray[j];
			char prev = charArray[j-1];
			char penult='\n';
			if (j>1) penult = charArray[j-2];

			//starting a new entry in a
			if ("()+-*/".indexOf(prev)>-1 ||
					(isPartOfNumber(prev)^isPartOfNumber(curr))) {
				
				//(-N is substituted by ( -1 * N,
				//where N is any number or expression			
				if (prev=='-' && penult=='(') {	
					a.add(i++,"-1");
					a.add(i++,"*");
				}
				
				// Adding a * sign where the user can omit it
				// (between parentheses, before parentheses,
				// before an operator that begins with a letter)
				else if (((curr=='(' || Character.isAlphabetic(curr)) &&
							(isPartOfNumber(prev)||prev==')'))) {
					a.add(i++,sb.toString());
					a.add(i++,"*");
				}
				
				//Adding a * sign where the user can omit it
				//(after parentheses, between parentheses and an operator
				//that begins with a letter)
				else if (penult==')' && 
						(isPartOfNumber(prev) || Character.isAlphabetic(prev))) {
					a.add(i++,"*");
					a.add(i++,sb.toString());
				}
				
				//Other cases
				else {
					a.add(i++,sb.toString());
				}
				
				sb = new StringBuilder();	
			}
			//System.out.println(curr+" "+a.toString());
			sb.append(curr);
		}
		if(!sb.toString().isEmpty()) a.add(sb.toString());
		
	}
	
	public static void main(String[] args) {
		//ExpressionEvaluator ee = new ExpressionEvaluator();
		//System.out.println("Answer: "+evaluateArrayList());
		parseExpression("sin(3.14159264)");
		System.out.println(a.toString());
		System.out.println("Answer: "+evaluateArrayList());

	}
	
	/**
	 * Returns true if the String parameter can be parsed to a Double.
	 */
	private static boolean isNumeric(String s) {
		try {
			Double.parseDouble(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Process the last operator:
	 * Pop the operator off <i><b>ops</i></b>, process the top two values in <i><b>vals</i></b>
	 * and push the result on <i><b>vals</i></b>.
	 */
	private static void processTopOfOps() {
		double d = ops.pop().processTwoArgs(vals.pop(), vals.pop());
		vals.push(d);
	}
	
	/**
	 * Evaluate a math expression from the <i><b>ArrayList<String> a</i></b>
	 * in which each number and each operation (including parentheses)
	 * is in a separate entry
	 * @return Double result of evaluation
	 */
	private static double evaluateArrayList() {
		for (String s : a) {
			//System.out.println(vals.toString());
			//System.out.println(ops.toString());
			//push a new Double onto vals
			if (isNumeric(s)) vals.push(Double.parseDouble(s));
			//work with a new operator
			else {
				Operator newOp = new Operator(s);
				//System.out.println(newOp);
				while (true) {
					//work with right parenthesis:
					if (newOp.isRightParen()) {
						//System.out.print("Working with )... ");
						while(!ops.peek().isLeftParen()) {
							processTopOfOps();
						}
						double val = vals.pop();
						val = ops.pop().processOneArg(val);
						//System.out.println(val);
						vals.push(val);
						//System.out.println(val);
						break;
					}
					//pushing the new operator onto the operator stack
					if (ops.isEmpty() || newOp.getPrecedence()>ops.peek().getPrecedence()
							|| newOp.isLeftParen()) { 
						ops.push(newOp); 
						break;
					//processing operators in ops stack until we get to
					//an operator whose precedence is <= newOp or
					//until ops is empty
					} else {
						processTopOfOps();
					}
				}
			}
		}
		while (!ops.isEmpty()) {
			processTopOfOps();
		}
		return vals.get(0);
	}
	
	public static double evaluate(String expression) {
		parseExpression(expression);
		return evaluateArrayList();
	}
	


}
