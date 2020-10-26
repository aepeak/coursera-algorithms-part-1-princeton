
/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Deques and Randomized Queues, Grade 80/100, https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php
 */

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] q; // queue elements
    private int n; // number of elements on queue
    private int first; // index of first element of queue
    private int last; // index of next available slot

    // construct an empty randomized queue
    @SuppressWarnings("unchecked")
    public RandomizedQueue() {
        q = (Item[]) new Object[2];
        n = 0;
        first = 0;
        last = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // resize the underlying array
    private void resize(int capacity) {
        assert capacity >= n;
        @SuppressWarnings("unchecked")
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = q[(first + i) % q.length];
        }
        q = copy;
        first = 0;
        last = n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        // double size of array if necessary and recopy to front of array
        if (n == q.length) {
            resize(2 * q.length); // double size of array if necessary
        }

        q[last++] = item; // add item

        if (last == q.length) {
            last = 0; // wrap-around
        }
        n++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }

        // take rand item and swap it with the first item
        {
            int randIndex = StdRandom.uniform(first, last);
            Item randItem = q[randIndex];
            Item temp = q[first];
            q[first] = randItem;
            q[randIndex] = temp;
        }

        Item item = q[first];
        q[first] = null; // to avoid loitering
        n--;
        first++;
        if (first == q.length) {
            first = 0; // wrap-around
        }

        // shrink size of array if necessary
        if (n > 0 && n == q.length / 4) {
            resize(q.length / 2);
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }

        return q[StdRandom.uniform(first, last)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayRandomIterator();
    }

    private class ArrayRandomIterator implements Iterator<Item> {
        private int i = 0;
        private Item[] qr;

        public ArrayRandomIterator() {
            qr = Arrays.copyOf(q, n);
            StdRandom.shuffle(qr);
        }

        public boolean hasNext() {
            return i < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = qr[(i + first) % qr.length];
            i++;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> uut = new RandomizedQueue<Integer>();
        StdOut.println("enque 1..10");
        for (int i = 0; i < 10; i++) {
            uut.enqueue(i);
        }
        printQueue(uut);

        StdOut.println("deque 1..10");
        while (uut.size() > 0) {
            StdOut.println(String.format("%d", uut.dequeue()));
        }

        StdOut.println("queue should be empty");
        printQueue(uut);
    }

    private static void printQueue(RandomizedQueue<Integer> uut) {
        Iterator<Integer> it = uut.iterator();
        while (it.hasNext()) {
            StdOut.println(String.format("%d", it.next())); // should remove 1
        }
    }

}