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
import java.util.HashSet;
import java.util.Set;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 * Renders the {@link Board}.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class BoardRenderer {

    private final Set<BubbleRenderer> bubbleRenderers;

    /**
     * Create new {@link Board} renderer.
     *
     * @param board the game board to render
     * @param gfx the graphics controller
     * @param mouse the mouse position
     */
    public BoardRenderer(Board board, Graphics gfx, Vector2f mouse) {
        Bubble[][] bubbles = board.getBubbles();
        bubbleRenderers = new HashSet<>();

        for (int y = 0; y < bubbles.length; y++) {
            for (int x = 0; x < bubbles[0].length; x++) {
                bubbleRenderers.add(new BubbleRenderer(bubbles[y][x], gfx, 
                        mouse));
            }
        }
    }

    /**
     * Create a forceful explosion.
     *
     * @param bubbles
     */
    public void explode(Set<Bubble> bubbles) {
        for (Bubble bubble : bubbles) {
            for (BubbleRenderer bubbleRenderer : bubbleRenderers) {
                bubbleRenderer.applyForce((Vector2f) bubble);
            }
        }
    }

    /**
     * Render the board.
     *
     * @param x
     * @param y
     */
    public void render(int x, int y) {
        for (BubbleRenderer bubbleRenderer : bubbleRenderers) {
            bubbleRenderer.render(x, y);
        }
    }

    /**
     * Update bubble physics.
     * @param gameContainer game container
     * @param delta delta time
     */
    public void update(GameContainer gameContainer, int delta) {
        Set<BubbleRenderer> popped = new HashSet<>();

        for (BubbleRenderer bubbleRenderer : bubbleRenderers) {
            if (bubbleRenderer.update(gameContainer, delta)) {
                popped.add(bubbleRenderer);
            }
        }
        // These won't need updating anymore.
        bubbleRenderers.removeAll(popped);
    }
}
