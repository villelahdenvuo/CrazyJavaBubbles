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

import java.awt.geom.Point2D;
import java.util.Random;
import org.newdawn.slick.Color;

/**
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class Bubble extends Point2D.Double {

    private final Color color;
    private boolean selected;

    /**
     * Create a new Bubble.
     *
     * @param x
     * @param y
     */
    public Bubble(int x, int y) {
        super(x, y);
        selected = false;

        this.color = new Color[]{
            Color.red, Color.blue, Color.green, Color.yellow
        }[new Random().nextInt(4)];
    }

    /**
     * @param selected whether the bubble is selected or not
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return whether the bubble is selected or not
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @return the colour of the bubble
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param b bubble to compare to
     * @return whether two bubbles are the same colour or not
     */
    public boolean equals(Bubble b) {
        if (b == null) {
            return false;
        }
        return color.equals(b.color);
    }
}
