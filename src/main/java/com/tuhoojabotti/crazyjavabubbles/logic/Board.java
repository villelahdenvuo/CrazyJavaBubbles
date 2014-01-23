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
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class Board {

    private final Bubble[][] bubbles;
    private Set<Bubble> selection;
    private final int width;
    private final int height;

    /**
     * Create new game board.
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
     * initialize game board.
     */
    public void init() {
        selection = new HashSet<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bubbles[y][x] = new Bubble(x, y);
            }
        }
    }

    /**
     * Pop (remove) selected {@link Bubble}s from the board.
     *
     * @return amount of bubbles popped
     */
    public int pop() {
        // Can't pop if selection is smaller than 2.
        if (selection.size() < 2) {
            return -1;
        }

        // Remove selected bubbles from the board.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (selection.contains(bubbles[y][x])) {
                    bubbles[y][x] = null;
                }
            }
        }

        // Update bubble positions;
        update();

        return selection.size();
    }

    /**
     * @return the bubbles on the board.
     */
    public Bubble[][] getBubbles() {
        return bubbles;
    }

    /**
     * Select {@link Bubble}s from the board.
     *
     * @param x
     * @param y
     */
    public void select(int x, int y) {
        updateSelection(x, y);

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
     * @return whether groups exist on the board or not.
     */
    public boolean hasMoreMoves() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Bubble b = bubbles[y][x];
                if (b != null
                        && (b.equals(get(x + 1, y))
                        || b.equals(get(x - 1, y))
                        || b.equals(get(x, y + 1))
                        || b.equals(get(x, y - 1)))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOnBoard(int x, int y) {
        return x >= 0 && x < width
                && y >= 0 && y < height && bubbles[y][x] != null;
    }

    private Bubble get(int x, int y) {
        if (!isOnBoard(x, y)) {
            return null;
        }
        return bubbles[y][x];
    }

    private void updateSelection(int startX, int startY) {
        selection.clear();

        if (!isOnBoard(startX, startY)) {
            return; // Nothing to select.
        }

        // Basically do a BFS on the board.
        Queue<Bubble> queue = new LinkedList<>();
        queue.add(get(startX, startY));

        while (!queue.isEmpty()) {
            Bubble current = queue.poll();
            int x = (int) current.x, y = (int) current.y;

            addIfOk(current, queue, x + 1, y);
            addIfOk(current, queue, x - 1, y);
            addIfOk(current, queue, x, y + 1);
            addIfOk(current, queue, x, y - 1);
            selection.add(current);
        }
    }

    private void addIfOk(Bubble current, Queue<Bubble> queue, int x, int y) {
        Bubble next = get(x, y);
        if (current.equals(next) && !selection.contains(next)) {
            queue.add(next);
        }
    }

    private void update() {
        boolean moved = false;

        // Move bubbles down.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Bubble b = bubbles[y][x];
                if (b == null && isOnBoard(x, y - 1)) {
                    moved = true;
                    moveBubble(get(x, y - 1), x, y);
                }
            }
        }

        // Move columns left.
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

        // Run update until we didn't move anything.
        if (moved) {
            update();
        }
    }

    private void moveBubble(Bubble b, int x, int y) {
        bubbles[(int) b.y][(int) b.x] = null;
        b.setLocation(x, y);
        bubbles[y][x] = b;
    }
}
