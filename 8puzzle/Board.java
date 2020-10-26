/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Collinear, Grade 100/100, https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php
 */

import edu.princeton.cs.algs4.Queue;

public class Board {

    private final int[][] tiles;
    private int hamming = Integer.MIN_VALUE;
    private int manhattan = Integer.MIN_VALUE;

    /**
     * create a board from an n-by-n array of tiles, where tiles[row][col] = tile at
     * (row, col)
     * 
     * @param tiles an n-by-n array of tiles
     */
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        if (!(2 <= tiles.length && tiles.length < 128)) {
            throw new IllegalArgumentException();
        }

        this.tiles = Board.cloneTiles(tiles);
    }

    /**
     * @return the string representation of this board
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.dimension()).append("\n");
        for (int r = 0; r < this.dimension(); r++) {
            for (int c = 0; c < this.dimension(); c++) {
                sb.append(String.format("%2d ", tiles[r][c]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * @return board dimension n
     */
    public int dimension() {
        return this.tiles.length;
    }

    /**
     * The Hamming distance between a board and the goal board is the number of
     * tiles in the wrong position.
     * 
     * @return number of tiles out of place
     */
    public int hamming() {
        if (this.hamming != Integer.MIN_VALUE) {
            return this.hamming;
        }

        int distance = 0;
        for (int r = 0; r < this.dimension(); r++) {
            for (int c = 0; c < this.dimension(); c++) {
                // v(r,c), ...
                // 1(0,0), 2(0,1), 3(0,2)
                // 4(1,0), 5(1,1), 6(1,2)
                // 7(2,0), 8(2,1), 9(2,2)

                int val = tiles[r][c];
                if (val == 0) {
                    continue;
                }

                int eval = (this.dimension() * r + c) + 1;
                // StdOut.println(String.format("v=%d, (%d,%d), ev=%d, %b", val, r, c, eval, val
                // == eval));

                if (val != eval) {
                    distance++;
                }
            }
        }

        this.hamming = distance;
        return distance; // -1 because 0 is ignored
    }

    /**
     * The Manhattan distance between a board and the goal board is the sum of the
     * Manhattan distances (sum of the vertical and horizontal distance) from the
     * tiles to their goal positions.
     * 
     * @return sum of Manhattan distances between tiles and goal
     */
    public int manhattan() {
        if (this.manhattan != Integer.MIN_VALUE) {
            return this.manhattan;
        }

        int distance = 0;
        for (int r = 0; r < this.dimension(); r++) {
            for (int c = 0; c < this.dimension(); c++) {
                // v(r,c), ...
                // 1(0,0), 2(0,1), 3(0,2)
                // 4(1,0), 5(1,1), 6(1,2)
                // 7(2,0), 8(2,1), 9(2,2)

                // where it should be? as er = expected row, ec = expected col
                int val = tiles[r][c];
                if (val == 0) {
                    continue;
                }

                int er = (val - 1) / dimension();
                int ec = (val - 1) % dimension();
                int d = abs(er - r) + abs(ec - c);
                // StdOut.println(String.format("v=%d, (%d,%d), (%d,%d), d=%d", val, r, c, er,
                // ec, d));

                if (d != 0) {
                    distance += d;
                }
            }
        }

        this.manhattan = distance;
        return distance; // -1 because 0 is ignored
    }

    private static int abs(int a) {
        return (a < 0) ? -a : a;
    }
    
    /**
     * Two boards are equal if they are have the same size and their corresponding
     * tiles are in the same positions.
     * 
     * @return true if equals, false otherwise
     */
    public boolean equals(Object thatObject) {
        // is same reference?
        if (this == thatObject) {
            return true;
        }
        // null check
        if (thatObject == null) {
            return false;
        }
        // type check
        if (this.getClass() != thatObject.getClass()) {
            return false;
        }
        
        Board that = (Board) thatObject;
        if (this.dimension() != that.dimension()) {
            return false;
        }

        for (int r = 0; r < this.dimension(); r++) {
            for (int c = 0; c < this.dimension(); c++) {
                int thisVal = this.tiles[r][c];
                int thatVal = that.tiles[r][c];
                if (thisVal != thatVal) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @return true if all tiles are in place, false otherwise
     */
    public boolean isGoal() {
        return hamming() == 0;
    }

    /**
     * @return an iterable containing the neighbors of the board. Depending on the
     *         location of the blank square, a board can have 2, 3, or 4 neighbors.
     */
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();

        // where is 0, the blank tile?
        for (int row = 0; row < this.dimension(); row++) {
            for (int col = 0; col < this.dimension(); col++) {
                int val = tiles[row][col];
                if (val != 0) {
                    continue;
                }

                // take the neighbors as north, east, west, south
                // * n *
                // e 0 w
                // * s *
                int[] news = { row - 1, col, row, col - 1, row, col + 1, row + 1, col };

                int n = this.dimension() - 1;
                for (int i = 0; i < news.length; i = i + 2) {
                    // m prefix from mutation
                    int mrow = news[i];
                    int mcol = news[i + 1];
                    if ((mrow < 0 || mrow > n) || (mcol < 0 || mcol > n)) {
                        continue;
                    }

                    int[][] neighbor = Board.cloneTiles(tiles);
                    neighbor[mrow][mcol] = 0;
                    neighbor[row][col] = tiles[mrow][mcol];
                    neighbors.enqueue(new Board(neighbor));
                }
            }
        }

        return neighbors;
    }

    /**
     * Used it to determine whether a puzzle is solvable: exactly one of a board and
     * its twin are solvable. A twin is obtained by swapping any pair of tiles (the
     * blank square is not a tile). For example, here is a board and several
     * possible twins. Your solver will use only one twin.
     * 
     * @return a board that is obtained by exchanging any pair of tiles
     */
    public Board twin() {
        int[][] twin = Board.cloneTiles(tiles);

        for (int row = 0; row < this.dimension(); row++) {
            for (int col = 1; col < this.dimension(); col++) {
                // get 2 consecutive tiles from same row which are != from 0
                int val = tiles[row][col];
                int pval = tiles[row][col - 1];

                if (val != 0 && pval != 0) {
                    twin[row][col] = tiles[row][col - 1];
                    twin[row][col - 1] = tiles[row][col];

                    return new Board(twin);
                }
            }
        }

        return new Board(twin);
    }

    private static int[][] cloneTiles(int[][] original) {
        int n = original.length;
        int[][] tiles = new int[n][n];

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                tiles[r][c] = original[r][c];
            }
        }

        return tiles;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // do nothing
    }

}