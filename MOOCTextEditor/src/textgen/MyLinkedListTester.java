/**
 * 
 */
package textgen;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author UC San Diego MOOC team
 *
 */
public class MyLinkedListTester {

	private static final int LONG_LIST_LENGTH =10; 

	MyLinkedList<String> shortList;
	MyLinkedList<Integer> emptyList;
	MyLinkedList<Integer> longerList;
	MyLinkedList<Integer> list1;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Feel free to use these lists, or add your own
	    shortList = new MyLinkedList<String>();
		shortList.add("A");
		shortList.add("B");
		emptyList = new MyLinkedList<Integer>();
		longerList = new MyLinkedList<Integer>();
		for (int i = 0; i < LONG_LIST_LENGTH; i++)
		{
			longerList.add(i);
		}
		list1 = new MyLinkedList<Integer>();
		list1.add(65);
		list1.add(21);
		list1.add(42);
		
	}

	
	/** Test if the get method is working correctly.
	 */
	/*You should not need to add much to this method.
	 * We provide it as an example of a thorough test. */
	@Test
	public void testGet()
	{
		//test empty list, get should throw an exception
		try {
			emptyList.get(0);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
			
		}
		
		// test short list, first contents, then out of bounds
		assertEquals("Check first", "A", shortList.get(0));
		assertEquals("Check second", "B", shortList.get(1));
		
		try {
			shortList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			shortList.get(2);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		// test longer list contents
		for(int i = 0; i<LONG_LIST_LENGTH; i++ ) {
			assertEquals("Check "+i+ " element", (Integer)i, longerList.get(i));
		}
		
		// test off the end of the longer array
		try {
			longerList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			longerList.get(LONG_LIST_LENGTH);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		}
		
	}
	
	
	/** Test removing an element from the list.
	 * We've included the example from the concept challenge.
	 * You will want to add more tests.  */
	@Test
	public void testRemove()
	{
		int a = list1.remove(0);
		assertEquals("Remove: remove method returns wrong value", 65, a);
		assertEquals("Remove: check element 0 is correct ", (Integer)21, list1.get(0));
		assertEquals("Remove: check size is correct ", 2, list1.size());
		
		try {
			emptyList.remove(0);
			fail("Exception not thrown on an invalid index");
		} catch (IndexOutOfBoundsException e) {}
		try {
			emptyList.remove(1);
			fail("Exception not thrown on an invalid index");
		} catch (IndexOutOfBoundsException e) {}
		try {
			list1.remove(-1);
			fail("Exception not thrown on an invalid index");
		} catch (IndexOutOfBoundsException e) {}
		try {
			list1.remove(list1.size());
			fail("Exception not thrown on an invalid index");
		} catch (IndexOutOfBoundsException e) {}
	}
	
	/** Test adding an element into the end of the list, specifically
	 *  public boolean add(E element)
	 * */
	@Test
	public void testAddEnd()
	{
		try {
			emptyList.add(null);
			fail("Check null pointer exception");
		} catch (NullPointerException e) {}
		try {
			emptyList.add(100);
			emptyList.add(200);
			assertEquals("Check element 0 is correct",(Integer)100,emptyList.get(0));
			assertEquals("Check last element is correct",(Integer)200,emptyList.get(1));
			assertEquals("add method should return true",list1.add(12),true);
			assertEquals("Wrong element added",(Integer)12,list1.get(list1.size()-1));
		}catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			fail("Cannot get element that has just been added");
		}
	}

	
	/** Test the size of the list */
	@Test
	public void testSize()
	{
		assertEquals("Check size of empty list is correct",0,emptyList.size());
		try {
			for (int i=0;i<15;i++) {
				list1.add((Integer)i);
			}
			assertEquals("Check size after adding is correct",list1.size(),18);
		}catch (IndexOutOfBoundsException e) {
			fail("Exception thrown when adding");
		}
		try {
			for (int i=0;i<4;i++) {
				emptyList.add(0,(Integer)i);
			}
		}catch (IndexOutOfBoundsException e) {
			fail("Exception thrown when adding at index 0 to an empty List");
		}
		try {
			for (int i=1;i<4;i++) {
				emptyList.add(i,(Integer)180);
			}
		}catch (IndexOutOfBoundsException e) {
			fail("Exception thrown when adding at a non-0 index");
		}
		assertEquals("Check size after adding at a specified index is correct",emptyList.size(),7);
	}

	
	
	/** Test adding an element into the list at a specified index,
	 * specifically:
	 * public void add(int index, E element)
	 * */
	@Test
	public void testAddAtIndex()
	{
		list1.add(1,60);
		assertEquals("Wrong element at index",list1.get(1),(Integer)60);
		assertEquals("Wrong element before index",list1.get(0),(Integer)65);
		assertEquals("Wrong element after index",list1.get(2),(Integer)21);
		assertEquals("Wrong element over an index",list1.get(3),(Integer)42);
		list1.add(0,12);
		assertEquals("Adding at index 0: wrong element at index",list1.get(0),(Integer)12);
		assertEquals("Adding at index 0: wrong element after index",list1.get(1),(Integer)65);		
		int s = list1.size()-1;
		list1.add(s,51);
		assertEquals("Adding at the last index: wrong element at index",list1.get(s),(Integer)51);
		assertEquals("Adding at the last index: wrong element before index",list1.get(s-1),(Integer)21);
		assertEquals("Adding at the last index: wrong element after index",list1.get(s+1),(Integer)42);
		
		try {
			list1.add(2,null);
			fail("Adding a null object: exception not thrown");
		} catch(NullPointerException e) {}
		try {
			emptyList.add(1,89);
			fail("Exception not thrown on an invalid index");
		} catch (IndexOutOfBoundsException e) {}
		try {
			list1.add(-1,89);
			fail("Exception not thrown on an invalid index");
		} catch (IndexOutOfBoundsException e) {}
		try {
			list1.add(list1.size(),89);
			fail("Exception not thrown on an invalid index");
		} catch (IndexOutOfBoundsException e) {}
	}

	/** Test setting an element in the list */
	@Test
	public void testSet()
	{
		list1.set(1, 72);
		assertEquals("Wrong element set",(Integer)72,list1.get(1));
		try {
			list1.set(2,null);
			fail("Adding a null object: exception not thrown");
		} catch(NullPointerException e) {}
		try {
			emptyList.set(0,89);
			fail("Exception not thrown on an invalid index");
		} catch (IndexOutOfBoundsException e) {}
		try {
			emptyList.set(1,89);
			fail("Exception not thrown on an invalid index");
		} catch (IndexOutOfBoundsException e) {}
		try {
			list1.set(-1,89);
			fail("Exception not thrown on an invalid index");
		} catch (IndexOutOfBoundsException e) {}
		try {
			list1.set(list1.size(),89);
			fail("Exception not thrown on an invalid index");
		} catch (IndexOutOfBoundsException e) {}
	}
	
	
	
}
