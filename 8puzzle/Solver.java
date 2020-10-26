/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Collinear, Grade 100/100, https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php
 */

import java.util.HashMap;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private class SNode implements Comparable<SNode> {
        // the root node
        final SNode previous;
        // the current board
        final Board board;
        // the number of moves to reach the current board
        final int moves;

        public SNode(Board board, SNode previous) {
            this.previous = previous;
            this.board = board;
            this.moves = previous == null ? 0 : previous.moves + 1;
        }

        public int fcost() {
            return board.manhattan() + moves;
        }

        @Override
        public int compareTo(SNode o) {
            if (this.fcost() == o.fcost())
                return 0;
            else if (this.fcost() > o.fcost())
                return 1;
            else
                return -1;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            {
                sb.append(String.format("manhattan = %d ", board.manhattan()));
                sb.append(String.format("moves = %d ", moves));
                sb.append(String.format("priority = %d\n", fcost()));
                sb.append(board.toString());
            }

            return sb.toString();
        }
    }

    private SNode solve(Board initial) {
        final MinPQ<SNode> openSet = new MinPQ<>(/* initial.dimension() * initial.dimension() */);
        final HashMap<Board, SNode> _openSet = new HashMap<Board, SNode>();
        final HashMap<Board, SNode> closedSet = new HashMap<Board, SNode>();

        // add the start node to open set
        SNode node = new SNode(initial, null);
        openSet.insert(node);
        _openSet.put(initial, node);

        int steps = 0;
        // loop
        while (!openSet.isEmpty()) {
            steps++;
            
            // get the best candidate from neighbors,
            // which is the node in open with lowest cost
            // and
            // remove the node from open set
            SNode current = openSet.delMin();
            _openSet.remove(current.board);
            StdOut.println("current: \n" + current);

            // add current to closed set
            closedSet.put(current.board, current);

            // is current the target node?
            if (current.board.isGoal()) {
                // is solved => reconstruct the path
                StdOut.println("STEPS: " + steps);
                return current;
            }
            if (current.board.hamming() == 2 && current.board.twin().isGoal()) {
                return null;
            }

            // for each neighbor of current node
            for (Board neighbor : current.board.neighbors()) {
                // if not traversable or already in closed set skip it
                if (closedSet.containsKey(neighbor)) {
                    continue;
                }

                // 1. distance from starting/initial node
                int gcost = current.moves + 1; 
                // 2. distance to target node
                int hcost = neighbor.manhattan();
                // 3. fcost = hcost + gcost        
                int fcost = hcost + gcost;

                // if new path to neighbor is shorter OR neighbor is not in open set
                boolean isInOpenSet = _openSet.containsKey(neighbor);
                if (current.fcost() < fcost || !isInOpenSet) {
                    // set new cost of neighbor
                    // and
                    // set parent of neighbor to current
                    SNode nnode = new SNode(neighbor, current);
                    StdOut.println("neighbor: \n" + nnode);

                    if (!isInOpenSet) {
                        openSet.insert(nnode);
                        _openSet.put(neighbor, nnode);
                    }
                }
            }
        }

        return null;
    }

    private SNode solution = null;

    /**
     * Find a solution to the initial board (using the A* algorithm)
     * 
     * @param initial the starting board
     */
    public Solver(Board initial) {
        this.solution = solve(initial);
    }

    /**
     * @return true if a solution is found, false otherwise
     */
    public boolean isSolvable() {
        return this.solution != null;
    }

    /**
     * @return min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        return isSolvable() ? solution.moves : -1;
    }

    /**
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }

        Stack<Board> queue = new Stack<>();
        SNode node = solution;
        while (node != null) {
            queue.push(node.board);
            node = node.previous;
        }

        return queue;
    }

    // test client (see below)
    public static void main(String[] args) {
        // do nothing
    }

}