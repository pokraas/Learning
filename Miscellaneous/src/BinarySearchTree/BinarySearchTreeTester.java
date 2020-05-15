package BinarySearchTree;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class BinarySearchTreeTester {
	BinarySearchTree<Integer> smallTree;
	
	@Before
	public void setup() {
		smallTree = new BinarySearchTree<Integer>();
	}
	
	@Test
	public void test1() {
		try {
			//smallTree.insert(1);
			//smallTree.insert(null);
			fail("NullPointerException not thrown!");
		} catch (NullPointerException e) {}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
