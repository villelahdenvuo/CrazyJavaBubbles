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

import com.tuhoojabotti.crazyjavabubbles.logic.Bubble;
import java.awt.Point;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class BubbleRenderer {

    private final Graphics gfx;
    private final int r = RenderSettings.BUBBLE_RADIUS;
    private final Point mousePosition;

    /**
     * Create a {@link Bubble} renderer with mouse interaction.
     *
     * @param gfx graphics controller
     * @param mouse mouse position
     */
    public BubbleRenderer(Graphics gfx, Point mouse) {
        this.gfx = gfx;
        mousePosition = mouse;
    }

    /**
     * Create a {@link Bubble} renderer without mouse interaction.
     *
     * @param gfx graphics controller
     */
    public BubbleRenderer(Graphics gfx) {
        this.gfx = gfx;
        mousePosition = new Point(-1000, -1000);
    }

    /**
     * Render a {@link Bubble}.
     *
     * @param b the bubble to render
     * @param x
     * @param y
     */
    public void render(Bubble b, int x, int y) {
        if (b == null) {
            return;
        }

        double d = mousePosition.distance(x + r / 2, y + r / 2);
        float inR = b.isSelected()
                ? 16 : (float) Math.max(4, r / 3.f - 10 / d * 50);

        Color color = b.getColor();
        java.awt.Color inColor = new java.awt.Color(color.r, color.g, color.b)
                .darker().darker();

        gfx.setColor(color);
        gfx.fillOval(x, y, r, r);
        gfx.setColor(b.isSelected() ? new Color(inColor.getRGB()) : Color.black);
        gfx.fillOval(x + r / 2 - inR / 2, y + r / 2 - inR / 2, inR, inR);
    }
}
