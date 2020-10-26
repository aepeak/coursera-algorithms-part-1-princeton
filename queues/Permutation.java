/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Deques and Randomized Queues, Grade 80/100, https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php
 */

import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> uut = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                uut.enqueue(item);
            } else if (!uut.isEmpty()) {
                printQueue(uut, k);
            }
        }
    }

    private static void printQueue(RandomizedQueue<String> uut, int k) {
        Iterator<String> it = uut.iterator();
        while (it.hasNext() && k > 0) {
            StdOut.println(String.format("%s", it.next()));
            k--;
        }
    }
}
