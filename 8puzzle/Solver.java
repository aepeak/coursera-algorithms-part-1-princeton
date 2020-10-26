/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Collinear, Grade 100/100, https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php
 */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private final SearchNode solution;

    /**
     * Find a solution to the initial board (using the A* algorithm)
     * 
     * @param initial the starting board
     */
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        this.solution = solve(initial);
    }

    /**
     * @return true if a solution is found, false otherwise
     */
    public boolean isSolvable() {
        return this.solution.isTwin == false;
    }

    /**
     * @return min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        if (isSolvable()) {
            return solution.moves;
        }

        return -1;
    }

    /**
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }

        Stack<Board> queue = new Stack<>();
        SearchNode node = solution;
        while (node != null) {
            queue.push(node.board);
            node = node.previous;
        }
        return queue;
    }

    private SearchNode solve(Board initial) {
        final MinPQ<SearchNode> openList = new MinPQ<>();

        // add the start node to open set
        SearchNode start = new SearchNode(initial, null, false);
        openList.insert(start);
        SearchNode startTwin = new SearchNode(initial.twin(), null, true);
        openList.insert(startTwin);

        // loop
        while (!openList.isEmpty()) {
            SearchNode current = openList.delMin();

            // is current the target node?
            if (current.board.isGoal()) {
                return current;
            }

            // for each neighbor of current node
            for (Board board : current.board.neighbors()) {
                SearchNode neighbor = new SearchNode(board, current);
                if (current.previous == null || //
                        !current.previous.board.equals(board)) {
                    openList.insert(neighbor);
                }
            }
        }

        return null;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final SearchNode previous;
        private final Board board;
        // moves represents the g(n) function cost
        // how far away the node is from initial node
        private final int moves;
        private final boolean isTwin;

        private int fcost = Integer.MIN_VALUE;
        private int hcost = Integer.MIN_VALUE;
        
        public SearchNode(Board board, SearchNode previous, boolean isTwin) {
            this.previous = previous;
            this.board = board;
            if (previous == null) {
                this.moves = 0;
            } else {
                this.moves = previous.moves + 1;
            }
            this.isTwin = isTwin;
        }
        
        public SearchNode(Board board, SearchNode previous) {
            this(board, previous, previous.isTwin);
        }

        private int hcost() {
            if (this.hcost == Integer.MIN_VALUE) {
                this.hcost = board.manhattan();
            }

            return this.hcost;
        }

        private int fcost() {
            if (this.fcost == Integer.MIN_VALUE) {
                this.fcost = hcost() + moves;
            }

            return this.fcost;
        }

        @Override
        public int compareTo(SearchNode that) {
            int costDiff = this.fcost() - that.fcost();
            return costDiff == 0 ? this.hcost() - that.hcost() : costDiff;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("manhattan = %d ", board.manhattan()));
            sb.append(String.format("moves = %d ", moves));
            sb.append(String.format("priority = %d\n", fcost()));
            sb.append(board.toString());

            return sb.toString();
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // do nothing
    }

}