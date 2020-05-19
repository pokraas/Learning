package Parsing;
/**
 * A class to represent a mathematical operator.
 * Operators with bigger precedence will be evaluated first.
 * @author alexanderpokras
 *
 */
class Operator {
	
	String opString;
	private int precedence;
	/**
	 * Determine precedence
	 * @param opString String that represents an operator in the expression
	 */
	Operator(String opString){
		this.opString=opString;
		switch(opString) {
		case("+") : precedence=1; break;
		case("-") : precedence=1; break;
		case("*") : precedence=2; break;
		case("/") : precedence=2; break;
		case("^") : precedence=3; break;
//		case("("):break;
//		case(")"):break;
//		default: throw new IllegalArgumentException(opString+" is not a supported operation");
		}
	}
	
	/**
	 * Perform an operation according to the operator's type
	 * for operators that take two arguments
	 * (e.g., addition for "+", subtraction for "-" etc.)
	 * @param val2
	 * @param val1
	 * @return result of the operation
	 */
	double processTwoArgs(double val2, double val1) {
		switch(opString) {
		case("+") : return val1+val2;
		case("-") : return val1-val2;
		case("*") : return val1*val2;
		case("/") : return val1/val2;
		case("^") : return Math.pow(val1, val2);
		default: throw new UnsupportedOperationException(opString+" is an unknown operation");
		}
	}
	
	/**
	 * Perform an operation according to the operator's type
	 * for operators that take one argument and have a ( at their end
	 * (e.g., square root for "sqrt(" )
	 * @param val
	 * @return result of the operation
	 */
	double processOneArg(double val) {
		switch(opString) {
		case("(") : return val;
		//case("-(") : return -val;
		case("sin(") : return Math.sin(val);
		case("cos(") : return Math.cos(val);
		case("tan(") :
		case("tg(") : return Math.tan(val);
		case("cot(") :
		case("ctg(") : return 1/Math.tan(val);
		case("arcsin(") : return Math.asin(val);
		case("arccos(") : return Math.acos(val);
		case("arctan") : 
		case("arctg(") : return Math.atan(val);
		case("arccot(") :
		case("arcctg(") : return Math.atan(1/val);
		case("sqrt(") : return Math.sqrt(val);
		case("ln(") : return Math.log(val);
		case("log(") : return Math.log10(val);

		default: throw new UnsupportedOperationException(opString+" is an unknown operation");	
		}
	}
	
	
	int getPrecedence() {
		return precedence;
	}
	
	boolean isLeftParen() {
		return opString.endsWith("(");
	}
	
	boolean isRightParen() {
		return opString.equals(")");
	}
	
	@Override
	public String toString() {
		return opString;
	}
}
