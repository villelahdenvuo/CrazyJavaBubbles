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
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class Bubble extends Point2D.Double {

    private Color color;

    public Bubble(int x, int y) {
        super(x, y);

        Random rand = new Random();

        switch (rand.nextInt(4)) {
            case 0:
                this.color = Color.red;
                break;
            case 1:
                this.color = Color.blue;
                break;
            case 2:
                this.color = Color.green;
                break;
            case 3:
                this.color = Color.yellow;
                break;
        }
    }

    public Bubble(Color color, int x, int y) {
        super(x, y);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public boolean equals(Bubble b) {
        if (b == null) {
            return false;
        }
        return color.equals(b.color);
    }

}
