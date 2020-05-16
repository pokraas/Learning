package Week2;

//import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;
/**
 * A random-access queue based on ArrayList implementation.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
	
	private Item[] a;
	private int size;
	
    // construct an empty randomized queue
    public RandomizedQueue() {
    	a = (Item[]) new Object[1];
    	size=0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
    	return size==0;
    }

    // return the number of items on the randomized queue
    public int size() {
    	return size;
    }
    
    //resize array a
    private void resize(int capacity) {
    	//cloning a into temp
    	Item[] temp = (Item[]) new Object[capacity];
    	for (int i=0;i<size;i++) {
    		temp[i]=a[i];
    	}
    	a = temp;
    }

    // add the item
    public void enqueue(Item item) {
    	if (item==null) throw new IllegalArgumentException();
    	if (size+1>a.length) {
    		resize(size*2);
    	}
    	a[size]=item;
    	size++;
    }
    
    private void p() {
//    	System.out.println(Arrays.toString(a));
    }
    

    // remove and return a random item
    public Item dequeue() {
    	if (isEmpty()) throw new NoSuchElementException();
    	int index = StdRandom.uniform(size);
    	Item ret = a[index];
    	a[index]=a[size-1];
    	a[size-1]=null;
    	size--;
    	if (size<a.length/2) {
    		resize(a.length/2);
    	}
    	return ret;
    }

    // return a random item (but do not remove it)
    public Item sample() {
    	int index = StdRandom.uniform(size);
    	return a[index];
    }

	private class RandomIterator implements Iterator<Item> {
		int used;
		int[] permutation;
		@Override
		public boolean hasNext() {
			return used<size;
		}

		@Override
		public Item next() {
			return a[permutation[used++]];
		}
		
		public void remove() {//not used
			throw new UnsupportedOperationException();
		}
		
		public RandomIterator() {
			used=0;
			permutation = StdRandom.permutation(size);
		}
	}
	
	public Iterator<Item> iterator(){
		return new RandomIterator();
	}

    // unit testing (required)
    public static void main(String[] args) {
    	RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
    	rq.enqueue(1);
    	rq.enqueue(2);
    	rq.enqueue(3);
    	rq.enqueue(4);
    	rq.enqueue(5);
    	rq.p();
    	System.out.println("dequeued "+rq.dequeue());
    	System.out.println("dequeued "+rq.dequeue());
    	rq.p();
    	rq.enqueue(9);
    	rq.p();
    	rq.enqueue(10);
    	rq.p();
    	System.out.println("Size: "+rq.size);
    	for (int item:rq) {
    		System.out.print(item+" ");
    	}
    	System.out.println();
    	for (int item:rq) {
    		System.out.print(item+" ");
    	}

    }

}