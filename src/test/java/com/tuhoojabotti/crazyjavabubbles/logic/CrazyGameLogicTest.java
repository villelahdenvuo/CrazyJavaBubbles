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

import static com.tuhoojabotti.crazyjavabubbles.main.Util.getPositionOnBoard;
import com.tuhoojabotti.crazyjavabubbles.main.Settings;
import java.awt.Point;
import java.util.Random;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class CrazyGameLogicTest {

    private int r = Settings.BUBBLE_RADIUS,
            margin = Settings.BOARD_MARGIN;

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
        int loops = 0, count = 0;
        while (!logic.isGameOver()) {
            for (int y = 0; y < bubbles.length; y++) {
                for (int x = 0; x < bubbles[0].length; x++) {
                    logic.updateSelection(new Vector2f(margin + x * r, margin + y * r));
                    Set<Bubble> popped = logic.pop();
                    if (popped != null) {
                        count += popped.size();
                        assertEquals(count, logic.getBubblesPopped());
                        assertTrue(logic.getBiggestClusterBonus() >= popped.size());
                    }
                }
            }
            loops++;
            if (loops > 200) {
                fail("Game should end at some point.");
            }
        }

        assertTrue("Score should be bigger than zero.", logic.getTotalScore() > 0);
    }

    @Test
    public void popShouldReturnBubbleCount() {
        Bubble[][] bubbles = logic.getBoard().getBubbles();
        for (int y = 0; y < bubbles.length; y++) {
            for (int x = 0; x < bubbles[0].length; x++) {
                logic.updateSelection(new Vector2f(margin + x * r, margin + y * r));
                Set<Bubble> selection = logic.getBoard().getSelection();
                if (!selection.isEmpty()) {
                    assertEquals(selection.size(), logic.pop().size());
                }
            }
        }
    }

    @Test
    public void selectionIsCachedUnlessForced() {
        Random rand = new Random();
        Vector2f vec = new Vector2f(margin + rand.nextInt(24) * r, margin + rand.nextInt(17) * r);

        while (logic.getBoard().getSelection().isEmpty()) {
            vec = new Vector2f(margin + rand.nextInt(24) * r, margin + rand.nextInt(17) * r);
            logic.updateSelection(vec);
        }

        // Selection shouldn't be updated.
        Set<Bubble> selection = logic.getBoard().getSelection();
        logic.updateSelection(vec);
        assertEquals(selection, logic.getBoard().getSelection());

        // Forcing selection should create new selection.
        logic.forceUpdateSelection(vec);
        assertNotSame(selection, logic.getBoard().getSelection());
    }

    @Test
    public void getMousePositionOnBoardWorks() {
        testPoint(4, 3);
        testPoint(3, 4);
        testPoint(24, 17);
        testPoint(0, 0);
        testPoint(100, 100);
        testPoint(-4, -56);
    }

    private void testPoint(int x, int y) {
        Random rand = new Random();
        Vector2f vec = new Vector2f(margin + x * r + r * rand.nextFloat(), margin + y * r + r * rand.nextFloat());
        assertEquals(new Point(x, y), getPositionOnBoard(vec));
    }
}
