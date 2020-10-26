/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Deques and Randomized Queues, Grade 80/100, https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
	private Node<Item> first; // beginning of queue
	private Node<Item> last; // end of queue
	private int n; // number of elements on queue

	// helper linked list class
	private static class Node<Item> {
		private Item item;
		private Node<Item> next;
		private Node<Item> previous;

		@Override
		public String toString() {
			return item.toString();
		}
	}

	// construct an empty deque
	public Deque() {
		first = null;
		last = null;
		n = 0;
	}

	// is the deque empty?
	public boolean isEmpty() {
		return size() == 0;
	}

	// return the number of items on the deque
	public int size() {
		return n;
	}

	// add the item to the front
	public void addFirst(Item item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		
		Node<Item> oldFirst = first;
		first = new Node<Item>();
		first.item = item;
		first.next = oldFirst;
		first.previous = null;

		if (isEmpty()) {
			last = first;
		} else {
			oldFirst.previous = first;
		}

		n++;
	}

	// add the item to the back
	public void addLast(Item item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		
		Node<Item> oldLast = last;
		last = new Node<Item>();
		last.item = item;
		last.next = null;
		last.previous = oldLast;

		if (isEmpty()) {
			first = last;
		} else {
			oldLast.next = last;
		}

		n++;
	}

	// remove and return the item from the front
	public Item removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException("Deque is empty");
		}

		Item oldFirst = first.item;
		{
			Node<Item> newFirst = first.next;
			this.first = newFirst;
			if (size() > 1) {
				newFirst.previous = null;
			}
			if (isEmpty()) {
				first = last = null;
			}
			n--;
		}
		return oldFirst;

	}

	// remove and return the item from the back
	public Item removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException("Deque is empty");
		}

		Item oldLast = last.item;
		{
			Node<Item> newLast = last.previous;
			last = newLast;
			if (size() > 1) {
				newLast.next = null;
			}
			if (isEmpty()) {
				first = last = null;
			}
			n--;
		}

		return oldLast;
	}

	// return an iterator over items in order from front to back
	public Iterator<Item> iterator() {
		return new Iterator<Item>() {
			private Node<Item> current = first;
			private int count = n;

			@Override
			public boolean hasNext() {
				return count > 0;
			}
			
			@Override
			public void remove() {
				throw new java.lang.UnsupportedOperationException();
			}

			@Override
			public Item next() {
				Item it = current.item;
				{
					count--;
					current = current.next;
				}
				return it;
			}
		};

	}

	// unit testing (required)
	public static void main(String[] args) {
		Deque<Integer> uut = new Deque<>();

		uut.addFirst(3);
		uut.addFirst(2);
		uut.addFirst(1);
		printDeque(uut);

		uut.removeFirst();
		uut.removeFirst();
		uut.removeFirst();
		printDeque(uut);
		
		uut.addLast(3);
		uut.addLast(2);
		uut.addLast(1);
		printDeque(uut);
		
		uut.removeLast();
		uut.removeLast();
		uut.removeLast();
		printDeque(uut);
		
		uut.addFirst(42);
		uut.removeLast();
		printDeque(uut);
	}

	private static void printDeque(Deque<Integer> uut) {
		Iterator<Integer> it = uut.iterator();
		while (it.hasNext()) {
			StdOut.println(String.format("%d", it.next())); // should remove 1
		}
	}

}