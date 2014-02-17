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

import com.tuhoojabotti.crazyjavabubbles.gui.Settings;
import static com.tuhoojabotti.crazyjavabubbles.Util.curveValue;
import com.tuhoojabotti.crazyjavabubbles.logic.Bubble;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

/**
 * Renders the {@link Bubble} with cool physics.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class BubbleRenderer {

    private final Graphics graphics;
    private final int radius = Settings.BUBBLE_RADIUS;
    private final int margin = Settings.BOARD_MARGIN;
    private final Vector2f mousePosition;

    private Bubble bubble;
    private Vector2f velocity;
    private Vector2f target;

    private Circle outCircle;
    private Circle inCircle;

    /**
     * Create a {@link Bubble} renderer with mouse interaction.
     *
     * @param bubble the bubble to render
     * @param gfx graphics controller
     * @param mouse mouse position
     */
    public BubbleRenderer(Bubble bubble, Graphics gfx, Vector2f mouse) {
        this.bubble = bubble;
        this.graphics = gfx;
        mousePosition = mouse;

        velocity = new Vector2f();
        target = new Vector2f();

        outCircle = new Circle(0, 0, radius / 2, 16);
        inCircle = new Circle(0, 0, radius / 3, 14);

        outCircle.setLocation(margin + bubble.x * radius + 100, -10);
    }

    /**
     * Create a {@link Bubble} renderer without mouse interaction.
     *
     * @param bubble the bubble to render
     * @param gfx graphics controller
     */
    public BubbleRenderer(Bubble bubble, Graphics gfx) {
        this(bubble, gfx, new Vector2f(-100000, -100000));
    }

    public void applyForce(Vector2f bubble) {
        Vector2f point = new Vector2f(margin + bubble.x * radius,
                margin + bubble.y * radius);
        double angle = Math.atan2(point.y - outCircle.getCenterY(),
                point.x - outCircle.getCenterX());
        float distance = Math.max(10, point.distance(outCircle.getLocation()));

        velocity.x -= 750 / distance * (float) Math.cos(angle);
        velocity.y -= 650 / distance * (float) Math.sin(angle);
    }

    /**
     * Render a {@link Bubble}.
     *
     * @param x board's x-coordinate
     * @param y board's y-coordinate
     */
    public void render(int x, int y) {
        target.set(x + bubble.x * radius, y + bubble.y * radius);

        // Outer circle
        graphics.setColor(bubble.getColor());
        graphics.fill(outCircle);
        // Inner circle
        graphics.setColor(Color.black);
        graphics.fill(inCircle);
    }

    /**
     * Update the renderer.
     *
     * @param gameContainer game container
     * @param delta delta time
     * @return is bubble popped
     */
    public boolean update(GameContainer gameContainer, int delta) {
        double mouseAngle = Math.atan2(
                mousePosition.y - outCircle.getCenterY(),
                mousePosition.x - outCircle.getCenterX());

        updatePhysics(mouseAngle, delta);
        updateHole(mouseAngle, gameContainer.getTime());

        return bubble.isPopped();
    }

    private void updatePhysics(double mouseAngle, int delta) {
        float smooth = Settings.BUBBLE_WOBBLE + (bubble.y + 5) / 200f;

        // Update velocity towards real position + towards mouse cursor.
        velocity.x = curveValue(target.x - outCircle.getX(), velocity.x, smooth)
                + 0.35f * (float) Math.cos(mouseAngle);
        velocity.y = curveValue(target.y - outCircle.getY(), velocity.y, smooth)
                + 0.35f * (float) Math.sin(mouseAngle);

        // Move Bubble according to it's velocity.
        outCircle.setLocation(outCircle.getLocation()
                .add(velocity.scale(delta * 0.04f)));
    }

    private void updateHole(double mouseAngle, long time) {
        float mouseDistance = mousePosition.distance(outCircle.getLocation());

        // Set hole size.
        inCircle.setRadius((float) (Math.max(8, radius / 3f - 10
                / mouseDistance * 50)));

        if (bubble.isSelected()) {
            inCircle.setRadius(inCircle.getRadius() + (float) Math.sin((time / 80.0)
                    + (double) (bubble.x + bubble.y)) * 2.5f);
        }

        // Update the hole position.
        float holeSize = bubble.isSelected() ? 0 : 3.5f,
                offset = radius / 2 - inCircle.getRadius();
        inCircle.setLocation(
                outCircle.getLocation()
                .add(new Vector2f(((float) Math.cos(mouseAngle) * holeSize) + offset,
                                ((float) Math.sin(mouseAngle) * holeSize) + offset)));
    }
}
