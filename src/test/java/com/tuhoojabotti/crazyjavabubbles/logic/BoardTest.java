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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.newdawn.slick.Color;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class BoardTest {

    Board board;

    public BoardTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        board = new Board(24, 17);
        board.init();
    }

    @After
    public void tearDown() {
    }

    private void boardFromMatrix(Board board, char[][] chars) {
        Bubble[][] bubbles = new Bubble[chars.length][chars[0].length];
        for (int y = 0; y < chars.length; y++) {
            for (int x = 0; x < chars[0].length; x++) {
                Color c = Color.darkGray;
                switch (chars[y][x]) {
                    case 'r':
                        c = Color.red;
                        break;
                    case 'g':
                        c = Color.green;
                        break;
                    case 'b':
                        c = Color.blue;
                        break;
                    case 'y':
                        c = Color.yellow;
                        break;
                    case 'n':
                        bubbles[y][x] = null;
                        continue;
                }
                bubbles[y][x] = new Bubble(c, x, y);
            }
        }
        board.setBubbles(bubbles);
    }

    private int selectionSize() {
        return board.getSelection().size();
    }

    @Test
    public void newBoardHasMoves() {
        assertTrue(board.hasMoreMoves());
    }

    @Test
    public void testPopWithEmptySelection() {
        assertEquals(null, board.pop());
    }

    @Test
    public void canNotPopOneBubble() {
        boardFromMatrix(board, new char[][]{
            {'b'}
        });
        board.select(0, 0);
        assertEquals(null, board.pop());
    }

    @Test
    public void hasMoreMovesWorks() {
        boardFromMatrix(board, new char[][]{
            {'b', 'n'}
        });
        assertFalse(board.hasMoreMoves());
        boardFromMatrix(board, new char[][]{
            {'g', 'r'},
            {'y', 'b'}
        });
        assertFalse(board.hasMoreMoves());
        boardFromMatrix(board, new char[][]{
            {'n', 'n'},
            {'n', 'n'}
        });
        assertFalse(board.hasMoreMoves());
        boardFromMatrix(board, new char[][]{
            {'r', 'n'},
            {'b', 'n'}
        });
        assertFalse(board.hasMoreMoves());
        boardFromMatrix(board, new char[][]{
            {'r', 'n'},
            {'b', 'b'}
        });
        assertTrue(board.hasMoreMoves());
        // Invalid game state.
        boardFromMatrix(board, new char[][]{
            {'n', 'n', 'n'},
            {'n', 'b', 'n'},
            {'n', 'n', 'n'}
        });
        assertFalse(board.hasMoreMoves());
    }

    @Test
    public void selectSelectsAll() {
        boardFromMatrix(board, new char[][]{
            {'r', 'r', 'r', 'r'},
            {'r', 'r', 'r', 'r'},
            {'r', 'r', 'r', 'r'},
            {'r', 'r', 'r', 'r'}
        });
        board.select(0, 0);
        assertEquals(4 * 4, selectionSize());
    }

    @Test
    public void popPopsAll() {
        boardFromMatrix(board, new char[][]{
            {'r', 'r', 'r', 'r'},
            {'r', 'r', 'r', 'r'},
            {'r', 'r', 'r', 'r'},
            {'r', 'r', 'r', 'r'}
        });
        Bubble testBubble = board.getBubbles()[1][1];
        board.select(3, 3);
        board.pop();
        assertTrue(testBubble.isPopped());
        assertFalse(board.hasMoreMoves());
    }

    @Test
    public void selectingSelectsBubblesButNotAll() {
        boardFromMatrix(board, new char[][]{
            {'r', 'r'}, // <- these should be selected
            {'b', 'b'} // <- and these not
        });
        board.select(1, 0);
        Bubble[][] bubbles = board.getBubbles();

        for (Bubble item : bubbles[0]) {
            assertTrue(item.isSelected());
        }
        for (Bubble item : bubbles[1]) {
            assertFalse(item.isSelected());
        }
    }

    @Test
    public void selectOutsideBoardDoesNotCrash() {
        boardFromMatrix(board, new char[][]{
            {'r', 'r', 'r', 'r'},
            {'r', 'r', 'r', 'r'},
            {'r', 'r', 'r', 'r'},
            {'r', 'r', 'r', 'r'}
        });
        board.select(-1, -1);
        board.select(100, 10);
    }

    @Test
    public void moveDownWorks() {
        boardFromMatrix(board, new char[][]{
            {'r', 'r', 'r', 'r'},
            {'r', 'r', 'r', 'r'},
            {'r', 'b', 'r', 'r'},
            {'r', 'b', 'r', 'r'}
        });
        board.select(1, 2);
        board.pop();
        board.select(1, 0);
        assertEquals(0, selectionSize());
        board.select(1, 1);
        assertEquals(0, selectionSize());
    }

    @Test
    public void moveLeftWorks() {
        boardFromMatrix(board, new char[][]{
            {'r', 'b', 'r', 'r'},
            {'r', 'b', 'r', 'r'},
            {'r', 'b', 'r', 'r'},
            {'r', 'b', 'r', 'r'}
        });
        board.select(1, 0);
        board.pop();
        board.select(3, 0);
        assertEquals(0, selectionSize());
        board.select(3, 3);
        assertEquals(0, selectionSize());
    }

    @Test
    public void moveLeftWorks2() {
        boardFromMatrix(board, new char[][]{
            {'r', 'b', 'n', 'b'},
            {'r', 'b', 'n', 'b'},
            {'r', 'b', 'r', 'b'},
            {'r', 'b', 'r', 'b'}
        });
        board.select(1, 0);
        board.pop();
        board.select(3, 0);
        assertEquals(0, selectionSize());
        board.select(3, 3);
        assertEquals(0, selectionSize());
        board.select(1, 0);
        assertEquals(0, selectionSize());
        board.select(1, 1);
        assertEquals(0, selectionSize());
    }

    @Test
    public void updateWorks() {
        boardFromMatrix(board, new char[][]{
            {'n', 'n', 'r', 'r'}
        });
        board.select(0, 0);
        assertEquals(0, selectionSize());
        board.select(1, 0);
        assertEquals(0, selectionSize());

        board.select(2, 0);
        assertEquals(2, selectionSize());
        board.select(3, 0);
        assertEquals(2, selectionSize());

        board.updateBubblePositions();

        board.select(0, 0);
        assertEquals(2, selectionSize());
        board.select(1, 0);
        assertEquals(2, selectionSize());

        board.select(2, 0);
        assertEquals(0, selectionSize());
        board.select(3, 0);
        assertEquals(0, selectionSize());
    }
}
