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
import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class BoardRenderer {

    private final Graphics gfx;
    private final Board board;
    private final ArrayList<BubbleRenderer> bubbleRenderers;

    /**
     * Create new {@link Board} renderer.
     * 
     * @param board the game board to render
     * @param gfx the graphics controller
     * @param mouse the mouse position
     */
    public BoardRenderer(Board board, Graphics gfx, Vector2f mouse) {
        this.board = board;
        this.gfx = gfx;
        
        Bubble[][] bubbles = board.getBubbles();
        bubbleRenderers = new ArrayList<>();
        
        for (int y = 0; y < bubbles.length; y++) {
            for (int x = 0; x < bubbles[0].length; x++) {
                bubbleRenderers.add(new BubbleRenderer(bubbles[y][x], gfx, mouse));
            }
        }
    }

    /**
     * Create a forceful explosion.
     * 
     * @param point origin of the explosion
     * @param power power of the explosion
     */
    public void explode(Vector2f point, float power) {
        for (BubbleRenderer bubbleRenderer : bubbleRenderers) {
            bubbleRenderer.applyForce(point, power);
        }
    }
    
    /**
     * Render the board.
     * @param x
     * @param y
     */
    public void render(int x, int y) {
        for (BubbleRenderer bubbleRenderer : bubbleRenderers) {
            bubbleRenderer.render(x, y);
        }
    }

    public void update(GameContainer gc, int delta) {
        ArrayList<BubbleRenderer> popped = new ArrayList<>();
        
        for (BubbleRenderer bubbleRenderer : bubbleRenderers) {
            if (bubbleRenderer.update(gc, delta)) {
                popped.add(bubbleRenderer);
            }
        }
        
        bubbleRenderers.removeAll(popped);
    }
}
