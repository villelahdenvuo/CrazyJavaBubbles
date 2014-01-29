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

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class CrazyGameLogicTest {

    public CrazyGameLogicTest() {
    }

    private CrazyGameLogic logic;

    @Before
    public void setUp() {
        logic = new CrazyGameLogic();
        logic.init();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void gameShouldEnd() {
        Bubble[][] bubbles = logic.getBoard().getBubbles();
        int i = 0;
        while (!logic.isGameOver()) {
            for (int y = 0; y < bubbles.length; y++) {
                for (int x = 0; x < bubbles[0].length; x++) {
                    logic.select(new Point(x, y));
                    logic.pop();
                }
            }
            i++;
            if (i > 100) {
                fail("Game should end at some point.");
            }
        }

        assertTrue("Score should be bigger than zero.", logic.getScore() > 0);
    }

    @Test
    public void popShouldReturnBubbleCount() {
        Bubble[][] bubbles = logic.getBoard().getBubbles();
        for (int y = 0; y < bubbles.length; y++) {
            for (int x = 0; x < bubbles[0].length; x++) {
                logic.select(new Point(x, y));
                Set<Bubble> selection = logic.getBoard().getSelection();
                if (selection.size() > 1) {
                    assertEquals(selection.size(), logic.pop());
                }
            }
        }
    }
}
