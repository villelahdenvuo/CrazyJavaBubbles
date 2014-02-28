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

import com.tuhoojabotti.crazyjavabubbles.main.Util;
import java.awt.Point;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class BubbleTest {

    public BubbleTest() {
    }

    private Bubble bubble;

    @Before
    public void setUp() {
        bubble = new Bubble(Color.yellow, 0, 1);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetColor() {
        assertEquals(Color.yellow, bubble.getColor());
    }

    @Test
    public void testIsPopped() {
        assertFalse(bubble.isPopped());
    }

    @Test
    public void getScreenPositionWorks() {
        testPoint(0, 0);
        testPoint(0, 1);
        testPoint(1, 0);
        testPoint(10, 10);
        testPoint(0, -10);
    }
    
    private void testPoint(int x, int y) {
        Random rand = new Random();
        bubble = new Bubble(Color.yellow, x, y);
        Vector2f screenPos = bubble.getScreenPosition();
        Point boardPos = new Point(x, y);
        
        screenPos.x += rand.nextFloat();
        screenPos.y += rand.nextFloat();
        
        assertEquals(Util.getPositionOnBoard(screenPos), boardPos);
    }
}
