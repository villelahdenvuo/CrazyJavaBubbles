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

import static com.tuhoojabotti.crazyjavabubbles.Util.getPositionOnBoard;
import java.awt.Point;
import java.util.Set;
import org.newdawn.slick.geom.Vector2f;

/**
 * Handles updating of the game logic from the game class.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class CrazyGameLogic {

    private int score;
    private int poppedTotal;
    private int biggestCluster;
    private long startTime;
    private long endTime;

    private final Board board;
    private final Point lastSelection;

    /**
     * Create new {@link CrazyGame} logic.
     */
    public CrazyGameLogic() {
        board = new Board(24, 17);
        lastSelection = new Point(-1, -1);
    }

    /**
     * Initialise the logic.
     */
    public void init() {
        startTime = System.currentTimeMillis();
        board.init();
        score = 0;
        poppedTotal = 0;
        biggestCluster = 0;
    }

    public boolean isGameOver() {
        if (board.hasMoreMoves()) {
            endTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }

    public int getTotalScore() {
        return (int) Math.round(score
            + getBiggestClusterBonus()
            - getTimeBonus());
    }

    public int getScore() {
        return score;
    }

    public int getBiggestClusterBonus() {
        return (int) Math.pow(biggestCluster, 3);
    }

    public int getBiggestCluster() {
        return biggestCluster;
    }

    public int getTimeBonus() {
        return (int) Math.pow(getTime(), 3);
    }

    public int getBubblesPopped() {
        return poppedTotal;
    }

    public Board getBoard() {
        return board;
    }

    public int getTime() {
        return (int) Math.round((endTime - startTime) / 1000);
    }

    /**
     * Pop the currently updateSelectioned {@link Bubble}s.
     *
     * @return all the bubbles that were popped
     */
    public Set<Bubble> pop() {
        Set<Bubble> popped = board.pop();

        if (popped != null) {
            updateScore(popped.size());
        }

        return popped;
    }

    /**
     * Update updateSelectionion on the board, but only if the position has
     * changed.
     *
     * @param mousePosition
     */
    public void updateSelection(Vector2f mousePosition) {
        Point point = getPositionOnBoard(mousePosition);
        // Update selection only if mouse has moved.
        if (!lastSelection.equals(point)) {
            board.select(point.x, point.y);
            lastSelection.setLocation(point);
        }
    }

    /**
     * Update updateSelectionion on the board.
     *
     * @param mousePosition
     */
    public void forceUpdateSelection(Vector2f mousePosition) {
        Point point = getPositionOnBoard(mousePosition);
        board.select(point.x, point.y);
    }

    /**
     * Update the score of the game.
     *
     * @param popped amount of bubbles popped
     */
    private void updateScore(int popped) {
        poppedTotal += popped;
        biggestCluster = Math.max(biggestCluster, popped);
        score += calculateScore(popped);
    }

    public int calculateScore(int popped) {
        return (int) Math.round(Math.pow(popped, 2)) * 100;
    }
}
