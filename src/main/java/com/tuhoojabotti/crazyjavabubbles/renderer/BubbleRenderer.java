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
import java.awt.geom.Point2D;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class BubbleRenderer {

    private final Graphics gfx;
    private final int r = RenderSettings.BUBBLE_RADIUS;
    private final Point2D.Float mousePosition;
    private Bubble bubble;
    private Color inColor;
    private Point2D.Float position;
    private Point2D.Float velocity;

    /**
     * Create a {@link Bubble} renderer with mouse interaction.
     *
     * @param bubble the bubble to render
     * @param gfx graphics controller
     * @param mouse mouse position
     */
    public BubbleRenderer(Bubble bubble, Graphics gfx, Point2D.Float mouse) {
        Random rand = new Random();

        this.bubble = bubble;
        this.gfx = gfx;
        mousePosition = mouse;
        position = new Point2D.Float(RenderSettings.BOARD_MARGIN + bubble.x * r
                + 50 - rand.nextInt(100), 0);
        velocity = new Point2D.Float(0, 0);

        Color color = bubble.getColor();
        java.awt.Color tempColor = new java.awt.Color(color.r, color.g, color.b)
                .darker().darker();
        inColor = new Color(tempColor.getRGB());
    }

    /**
     * Create a {@link Bubble} renderer without mouse interaction.
     *
     * @param bubble the bubble to render
     * @param gfx graphics controller
     */
    public BubbleRenderer(Bubble bubble, Graphics gfx) {
        this(bubble, gfx, new Point2D.Float(-1000, -1000));
    }

    public void applyForce(Point2D.Float point, float power) {
        double angle = Math.atan2(point.y - position.y, point.x - position.x);
        double dist = point.distance(position.x, position.y) / 60;

        velocity.x -= power / (1 + dist) * (float) Math.cos(angle) * 10;
        velocity.y -= power / (1 + dist) * (float) Math.sin(angle) * 5;
    }

    /**
     * Render a {@link Bubble}.
     *
     * @param x board's x-coordinate
     * @param y board's y-coordinate
     */
    public void render(int x, int y) {
        if (bubble.isPopped()) {
            return;
        }
        updatePosition(x, y);

        double d = mousePosition.distance(position.x + r / 2, position.y + r / 2);
        float inR = bubble.isSelected()
                ? 16 : (float) Math.max(4, r / 3.f - 10 / d * 50);
        float outR = (float) (r / (1 + position.distance(x + bubble.x * r, y + bubble.y * r) / 100));

        // Outer circle
        gfx.setColor(bubble.getColor());
        gfx.fillOval(position.x, position.y, outR, outR);
        // Inner circle
        gfx.setColor(bubble.isSelected() ? inColor : Color.black);
        gfx.fillOval(position.x + r / 2 - inR / 2, position.y + r / 2 - inR / 2, inR, inR);

        // Debug box
        if (RenderSettings.DEBUG) {
            gfx.setColor(Color.gray);
            gfx.drawRect(x + bubble.x * r, y + bubble.y * r, r, r);
            gfx.drawLine(x + bubble.x * r + r / 2, y + bubble.y * r + r / 2,
                    position.x + r / 2, position.y + r / 2);
        }
    }

    private void updatePosition(int x, int y) {
        float smooth = RenderSettings.BUBBLE_WOBBLE + bubble.y / 80f;
        double angle = Math.atan2(mousePosition.y - position.y, mousePosition.x - position.x);

        // Move Bubble according to it's velocity.
        position.x += velocity.x;
        position.y += velocity.y;

        // Update velocity towards real position + towards mouse cursor.
        velocity.x = curveValue((x + bubble.x * r) - position.x, velocity.x, smooth)
                + 0.4f * (float) Math.cos(angle);
        velocity.y = curveValue((y + bubble.y * r) - position.y, velocity.y, smooth)
                + 0.4f * (float) Math.sin(angle);
    }

    private float curveValue(float newValue, float oldValue, float smooth) {
        return oldValue + (newValue - oldValue) * smooth;
    }
}
