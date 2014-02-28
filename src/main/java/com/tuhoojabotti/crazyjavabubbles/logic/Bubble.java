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

import com.tuhoojabotti.crazyjavabubbles.main.Settings;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

/**
 * A bubble on the game, all the information about it.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class Bubble extends Vector2f {

    /**
     * Color of the Bubble
     */
    private final Color color;
    /**
     * Is the bubble selected?
     */
    private boolean selected;
    /**
     * Is the bubble dead, these will be garbage collected by the renderer.
     */
    private boolean popped;

    /**
     * Create a new Bubble with random color.
     *
     * @param x
     * @param y
     */
    public Bubble(int x, int y) {
        super(x, y);
        selected = false;
        popped = false;

        this.color = new Color[]{
            Color.red, new Color(0.3f, 0.5f, 1f), Color.green, Color.yellow
        }[new Random().nextInt(4)];
    }

    /**
     * Create a new Bubble with custom color. (for testing)
     *
     * @param color color of the bubble
     * @param x
     * @param y
     */
    public Bubble(Color color, float x, float y) {
        super(x, y);
        this.color = color;
    }

    public void pop() {
        popped = true;
    }

    public boolean isPopped() {
        return popped;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public Color getColor() {
        return color;
    }

    /**
     * @param bubble bubble to compare to
     * @return whether two bubbles are the same colour or not
     */
    public boolean equals(Bubble bubble) {
        if (bubble == null) {
            return false;
        }
        return color.equals(bubble.color);
    }

    /**
     * Where on the screen is this bubble?
     *
     * @return the location on screen
     */
    public Vector2f getScreenPosition() {
        int m = Settings.BOARD_MARGIN,
            r = Settings.BUBBLE_RADIUS;
        return new Vector2f(m + x * r, m + y * r);
    }
}
