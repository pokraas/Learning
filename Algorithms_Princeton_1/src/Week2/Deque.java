package Week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A deque (double-ended queue) implemented with a doubly-linked list. 
 *
 */
public class Deque<Item> implements Iterable<Item> {
	//a doubly-linked list node
	private class Node<Item> {
		Item data;
		Node<Item> next;
		Node<Item> prev;
	}
	
	private Node<Item> start;
	private Node<Item> end;	//sentinel nodes (without data)
	private int size=0;
	
	public Deque() {
		start = new Node<Item>();
		end = new Node<Item>();
		start.next=end;
		end.prev=start;
	}
	
	public boolean isEmpty() {
		return start.next==end;
	}
	
	public int size() {
		return size;
	}
	
	public void addFirst(Item item) {
		Node<Item> first = new Node<Item>();
		first.data=item;
		Node<Item> second = start.next;
		start.next=first;
		first.next=second;
		second.prev=first;
		first.prev=start;	
		size++;
	}
	
	public void addLast(Item item) {
		Node<Item> last = new Node<Item>();
		last.data=item;
		Node<Item> secondToLast = end.prev;
		secondToLast.next=last;
		last.next=end;
		end.prev=last;
		last.prev=secondToLast;
		size++;
	}
	
	public Item returnFirst() {
		Node<Item> first = start.next;
		start.next = first.next;
		start.next.prev = start;
		size--;
		return first.data;
	}
	
	public Item returnLast() {
		Node<Item> last = end.prev;
		end.prev=last.prev;
		end.prev.next=end;
		size--;
		return last.data;
	}
	
	private class dequeIterator implements Iterator<Item> {
		private Node<Item> current;
		@Override
		public boolean hasNext() {
			return current!=end;
		}

		@Override
		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();
			Item data = current.data;
			current = current.next;
			return data;
		}
		
		public void remove() {//not used
			throw new UnsupportedOperationException();
		}
		
		public dequeIterator() {
			current = start.next;
		}
	}
	
	public Iterator<Item> iterator(){
		return new dequeIterator();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Deque<Integer> deque = new Deque<Integer>();
		deque.addFirst(5);
		deque.addFirst(4);
		deque.addLast(7);
		deque.addFirst(3);
		System.out.println(deque.returnFirst());
		deque.addFirst(2);
		deque.addLast(8);
		for (int i:deque) {
			System.out.print(i+" ");
		}
		System.out.print("\nsize: "+deque.size);
		
	}



}
