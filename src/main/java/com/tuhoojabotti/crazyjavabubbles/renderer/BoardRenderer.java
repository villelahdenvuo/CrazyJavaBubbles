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
package com.tuhoojabotti.crazyjavabubbles.renderer;

import com.tuhoojabotti.crazyjavabubbles.logic.*;
import java.awt.Point;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class BoardRenderer {

    private final Graphics gfx;
    private final BubbleRenderer bubbleRenderer;

    /**
     * Create new {@link Board} renderer.
     * 
     * @param gfx the graphics controller
     * @param mouse the mouse position
     */
    public BoardRenderer(Graphics gfx, Point mouse) {
        this.gfx = gfx;
        bubbleRenderer = new BubbleRenderer(gfx, mouse);
    }

    /**
     * Render the board.
     * @param b the board to render
     * @param x
     * @param y
     */
    public void render(Board b, int x, int y) {
        int br = RenderSettings.BUBBLE_RADIUS;
        Bubble[][] bubbles = b.getBubbles();

        gfx.setAntiAlias(true);
        for (int y2 = 0; y2 < bubbles.length; y2++) {
            for (int x2 = 0; x2 < bubbles[0].length; x2++) {
                bubbleRenderer.render(bubbles[y2][x2], x + x2 * br, y + y2 * br);
            }
        }
        gfx.setAntiAlias(false);
    }
}
