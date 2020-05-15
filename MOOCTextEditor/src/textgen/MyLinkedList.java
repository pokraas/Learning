package textgen;

import java.util.AbstractList;


/** A class that implements a doubly linked list
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	private int size;

	/** Create a new empty LinkedList */
	public MyLinkedList() {
		//setting up sentinel nodes (head and tail)
		head = new LLNode<E>(null);
		tail = new LLNode<E>(null);
		//connections between sentinel nodes
		head.setNext(tail);
		tail.setPrev(head);

		size=0;
	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element ) 
	{
		//if adding was unsuccessful
		if(element==null) {
			throw new NullPointerException("Cannot add a null object to the List");
		}

		LLNode<E> n = new LLNode<E>(tail.getPrev(),element,tail);
		tail.getPrev().setNext(n);
		tail.setPrev(n);
		size++;
		return true;
	}

	/** Get the element at position index 
	 * @throws IndexOutOfBoundsException if the index is out of bounds. 
	 */
	public E get(int index) 
	{
		if (index<0 || index>=size) {
			throw new IndexOutOfBoundsException("No element at this index");
		}
		LLNode<E> n = head;
		for (int i=0;i<=index;i++) {
			n = n.getNext();
		}
		return n.getData();
	}

	/**
	 * Add an element to the list at the specified index
	 * @param The index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element ) 
	{
		if (element==null) {
			throw new NullPointerException("Cannot add a null object to the List");
		}
		if (index<0 || (index==size && size>0) || index>size) {
			throw new IndexOutOfBoundsException("Cannot add an element at this index");
		}
		
		size++;
		
		if (index == size) {
			add(element);
			return;
		}
		
		LLNode<E> oldAtIndex = head;
		for (int i=0;i<=index;i++) {
			oldAtIndex = oldAtIndex.getNext();
		}	
		//inserting new node before oldAtIndex
		LLNode<E> newAtIndex = new LLNode<E>(oldAtIndex.getPrev(),element,oldAtIndex);
		//updating next and previous nodes for neighbors of newAtIndex
		oldAtIndex.getPrev().setNext(newAtIndex);
		oldAtIndex.setPrev(newAtIndex);
	}


	/** Return the size of the list */
	public int size() 
	{
		return size;
	}

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 * 
	 */
	public E remove(int index) 
	{

		if (index<0 || index>=size) {
			throw new IndexOutOfBoundsException("Cannot remove an element at this index");
		}
		
		size--;
		//finding which node to remove in O(n) time
		LLNode<E> toRemove = head;
		for (int i=0;i<=index;i++) {
			toRemove = toRemove.getNext();
		}	
		//updating next and previous nodes for neighbors of toRemove
		toRemove.getPrev().setNext(toRemove.getNext());
		toRemove.getNext().setPrev(toRemove.getPrev());
		return toRemove.getData();
	}

	/**
	 * Set an index position in the list to a new element
	 * @param index The index of the element to change
	 * @param element The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public E set(int index, E element) 
	{
		if (element==null) {
			throw new NullPointerException("Cannot add a null object to the List");
		}
		if (index<0 || index>=size) {
			throw new IndexOutOfBoundsException("Cannot add an element at this index");
		}
		LLNode<E> toChange = head;
		for (int i=0;i<=index;i++) {
			toChange = toChange.getNext();
		}
		E oldData = toChange.getData();
		toChange.setData(element);
		return oldData;
	}   
}

class LLNode<E> 
{
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	public void setNext(LLNode<E> n) {
		this.next = n;
	}
	public void setPrev(LLNode<E> n) {
		this.prev = n;
	}
	
	public LLNode<E> getNext() {
		return next;
	}
	public LLNode<E> getPrev() {
		return prev;
	}
	
	public E getData() {
		return data;
	}
	public void setData(E newData) {
		this.data = newData;
	}
	
	public LLNode(LLNode<E> prev,E e, LLNode<E> next) {
		this.prev = prev;
		this.next = next;
		this.data = e;
	}

	public LLNode(E e) 
	{
		this.data = e;
		this.prev = null;
		this.next = null;
	}

}
