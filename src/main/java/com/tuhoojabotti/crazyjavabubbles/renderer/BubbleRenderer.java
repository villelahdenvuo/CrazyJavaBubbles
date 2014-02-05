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
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

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
    private Vector2f velocity;
    private Vector2f target;
    Circle bubbleShape;
    Circle innerShape;

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
        velocity = new Vector2f();
        target = new Vector2f();

        Color color = bubble.getColor();
        java.awt.Color tempColor = new java.awt.Color(color.r, color.g, color.b)
                .darker().darker();
        inColor = new Color(tempColor.getRGB());

        bubbleShape = new Circle(0, 0, r / 2, 17);
        innerShape = new Circle(0, 0, r / 3, 17);
        
        bubbleShape.setLocation(RenderSettings.BOARD_MARGIN + bubble.x * r
                + 100 - rand.nextInt(200), -100);
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
        double angle = Math.atan2(point.y - bubbleShape.getCenterY(), point.x - bubbleShape.getCenterX());
        double dist = point.distance(bubbleShape.getCenterX(), bubbleShape.getCenterY()) / 60;
        
        velocity.x -= power / (1 + dist) * (float) Math.cos(angle) * 100;
        velocity.y -= power / (1 + dist) * (float) Math.sin(angle) * 50;
    }

    /**
     * Render a {@link Bubble}.
     *
     * @param x board's x-coordinate
     * @param y board's y-coordinate
     */
    public void render(int x, int y) {
        target.set(x + bubble.x * r, y + bubble.y * r);

        // Outer circle
        gfx.setColor(bubble.getColor());
        gfx.fill(bubbleShape);
        // Inner circle
        gfx.setColor(bubble.isSelected() ? inColor : Color.black);
        gfx.fill(innerShape);

        // Debug box
        if (RenderSettings.DEBUG) {
            gfx.setColor(Color.gray);
            gfx.drawRect(x + bubble.x * r, y + bubble.y * r, r, r);
            gfx.drawLine(x + bubble.x * r + r / 2, y + bubble.y * r + r / 2,
                    bubbleShape.getCenterX(), bubbleShape.getCenterY());
        }
    }

    private float curveValue(float newValue, float oldValue, float smooth) {
        return oldValue + (newValue - oldValue) * smooth;
    }

    public boolean update(int delta) {
        float smooth = RenderSettings.BUBBLE_WOBBLE + bubble.y / 80f;
        double angle = Math.atan2(mousePosition.y - bubbleShape.getCenterY(), mousePosition.x - bubbleShape.getCenterX());
        double d = mousePosition.distance(bubbleShape.getCenterX(), bubbleShape.getCenterY());
        innerShape.setRadius((float) (Math.max(8, r / 3.f - 10 / d * 50)
                + Math.sin((System.currentTimeMillis() / 80)) * (bubble.isSelected() ? 0.8 : 0)));
        float inR = innerShape.getRadius();

        // Move Bubble according to it's velocity.
        bubbleShape.setLocation(
                bubbleShape.getLocation() 
                .add(velocity.scale(delta * 0.06f))
        );

        innerShape.setLocation(
                bubbleShape.getLocation()
                .add(new Vector2f( (bubble.isSelected() ? 0 : (float) Math.cos(angle) * 3f) + r / 2 - inR,
                                   (bubble.isSelected() ? 0 : (float) Math.sin(angle) * 3f) + r / 2 - inR))
        );

        // Update velocity towards real position + towards mouse cursor.
        velocity.x = curveValue(target.x - bubbleShape.getX(), velocity.x, smooth)
                + 0.4f * (float) Math.cos(angle);
        velocity.y = curveValue(target.y - bubbleShape.getY(), velocity.y, smooth)
                + 0.4f * (float) Math.sin(angle);

        return bubble.isPopped();
    }
}
