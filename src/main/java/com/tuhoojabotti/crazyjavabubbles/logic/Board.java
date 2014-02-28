/*
 * The MIT License
 *
 * Copyright 2014 Ville Lahdenvuo <tuhoojabotti@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tuhoojabotti.crazyjavabubbles.logic;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * The logic of the game, it's all here.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class Board {

    /**
     * Contents of the whole board
     */
    private Bubble[][] bubbles;
    /**
     * Contents of the selection
     */
    private Set<Bubble> selection;
    /**
     * Width of the board
     */
    private int width;
    /**
     * Height of the board
     */
    private int height;
    /**
     * Does the board contain any valid moves?
     */
    private boolean hasMoreMoves;

    /**
     * Create new board.
     *
     * @param w width
     * @param h height
     */
    public Board(int w, int h) {
        bubbles = new Bubble[h][w];
        width = w;
        height = h;
    }

    /**
     * initialize the board.
     */
    protected void init() {
        selection = new HashSet<>();
        hasMoreMoves = false;

        // Randomize board until we have moves left. (Should just take one loop.)
        while (!hasMoreMoves) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    bubbles[y][x] = new Bubble(x, y);
                }
            }
            updateHasMoreMoves();
        }
    }

    /**
     * Pop (remove) selected {@link Bubble}s from the board.
     *
     * @return the set of bubbles that were removed
     */
    protected Set<Bubble> pop() {
        if (selection.size() < 2) {
            return null; // Can't pop if selection is smaller than 2.
        }

        // Remove selected bubbles from the board.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (selection.contains(bubbles[y][x])) {
                    bubbles[y][x].pop();
                    bubbles[y][x] = null;
                }
            }
        }

        updateBubblePositions();
        return selection;
    }

    public Bubble[][] getBubbles() {
        return bubbles;
    }

    /**
     * Set the content of the board manually (for tests).
     *
     * @param newBubbles the new bubbles
     */
    protected void setBubbles(Bubble[][] newBubbles) {
        bubbles = newBubbles;
        height = bubbles.length;
        width = bubbles[0].length;
        updateHasMoreMoves();
    }

    protected Set<Bubble> getSelection() {
        return selection;
    }

    /**
     * Select {@link Bubble}s from the board.
     *
     * @param x
     * @param y
     */
    protected void select(int x, int y) {
        updateSelection(x, y);
        // Tell the individual bubbles if they're selected or not for rendering.
        for (y = 0; y < height; y++) {
            for (x = 0; x < width; x++) {
                Bubble b = bubbles[y][x];
                if (b != null) {
                    b.setSelected(selection.contains(b));
                }
            }
        }
    }

    /**
     * Check if game is over.
     *
     * @return whether groups exist on the board or not.
     */
    protected boolean hasMoreMoves() {
        return hasMoreMoves;
    }

    /**
     * Is the point on the board?
     *
     * @param x
     * @param y
     * @return true or false
     */
    private boolean isOnBoard(int x, int y) {
        return x >= 0 && x < width
                && y >= 0 && y < height && bubbles[y][x] != null;
    }

    /**
     * Get a bubble. 
     * @param x
     * @param y
     * @return a Bubble from the board or null if out of bounds
     */
    private Bubble get(int x, int y) {
        if (!isOnBoard(x, y)) {
            return null;
        }
        return bubbles[y][x];
    }

    /**
     * Update bubble selection.
     * USes breadth-first search algorithm.
     *
     * @param startX the x-coordinate of the new selection
     * @param startY the y-coordinate of the new selection
     */
    private void updateSelection(int startX, int startY) {
        selection = new HashSet<>(); // Could be .clear(), but this is for test.

        if (isOnBoard(startX, startY)) {
            selectNeighbours(startX, startY);
        }

        // Can't select just one bubble.
        if (selection.size() == 1) {
            selection.clear();
        }
    }

    /**
     * Basically do a BFS on the board and select all neighbours.
     *
     * @param startX the x-coordinate of the new selection
     * @param startY the y-coordinate of the new selection
     */
    private void selectNeighbours(int startX, int startY) {
        Queue<Bubble> queue = new LinkedList<>();
        queue.add(get(startX, startY));

        while (!queue.isEmpty()) {
            Bubble current = queue.poll();
            queueIfMatches(current, queue, current.x + 1, current.y);
            queueIfMatches(current, queue, current.x - 1, current.y);
            queueIfMatches(current, queue, current.x, current.y + 1);
            queueIfMatches(current, queue, current.x, current.y - 1);
            selection.add(current);
        }
    }

    /**
     * Add Bubble to BFS queue if matches with current one.
     * @param current current Bubble processed
     * @param queue the queue
     * @param x the new Bubble's x-coordinate
     * @param y the new Bubble's y-coordinate
     */
    private void queueIfMatches(Bubble current, Queue<Bubble> queue, float x, float y) {
        Bubble next = get((int) x, (int) y);
        if (current.equals(next) && !selection.contains(next)) {
            queue.add(next);
        }
    }

    /**
     * Update the Bubbles on the board.
     */
    protected void updateBubblePositions() {
        // Run updateBubblePositions until we didn't move anything.
        if (moveBubblesDown() || moveBubblesLeft()) {
            updateBubblePositions();
        }
        updateHasMoreMoves();
    }

    /**
     * Move bubbles hanging in the air down.
     *
     * @return true if something moved
     */
    private boolean moveBubblesDown() {
        boolean moved = false;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Bubble b = bubbles[y][x];
                if (b == null && isOnBoard(x, y - 1)) {
                    moved = true;
                    moveBubble(get(x, y - 1), x, y);
                }
            }
        }
        return moved;
    }

    /**
     * Pack bubbles to the left.
     *
     * @return true if something moved
     */
    private boolean moveBubblesLeft() {
        boolean moved = false;
        for (int x = 0; x < width; x++) {
            if (!isOnBoard(x, height - 1) && isOnBoard(x + 1, height - 1)) {
                moved = true;
                for (int y = 0; y < height; y++) {
                    if (isOnBoard(x + 1, y)) {
                        moveBubble(get(x + 1, y), x, y);
                    }
                }
            }
        }
        return moved;
    }

    /**
     * Move a bubble to a new location on the board.
     *
     * @param bubble the bubble to move
     * @param x new x-coordinate
     * @param y new y-coordinate
     */
    private void moveBubble(Bubble bubble, int x, int y) {
        bubbles[(int) bubble.y][(int) bubble.x] = null;
        bubble.set(x, y);
        bubbles[y][x] = bubble;
    }

    /**
     * Check if the board still has moves left. Basically checks if there
     * exists two bubbles of the same color together somewhere.
     */
    private void updateHasMoreMoves() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Bubble b = bubbles[y][x];
                if (b != null && (b.equals(get(x + 1, y))
                        || b.equals(get(x, y + 1)))) {
                    hasMoreMoves = true;
                    return; // No need to check further.
                }
            }
        }
        hasMoreMoves = false;
    }
}
