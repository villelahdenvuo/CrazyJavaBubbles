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
package com.tuhoojabotti.crazyjavabubbles.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class WobbleTextRenderer extends TextRenderer {

    private float wobbleplier;
    private String text;
    private String[] chars;
    private float[] widths;
    private float length;

    public WobbleTextRenderer(String fontName, int size, String text) throws SlickException {
        this(fontName, size, text, 1f);
    }

    public WobbleTextRenderer(String fontName, int size, String msg, float wobble) throws SlickException {
        super(fontName, size);
        wobbleplier = wobble;
        text = msg;
        length = text.length();
        chars = new String[(int) length];
        widths = new float[(int) length];
        for (int i = 0; i < length; i++) {
            widths[i] = font.getWidth(text.substring(0, i));
            chars[i] = "" + text.charAt(i);
        }
    }

    public void render(int x, int y) {
        render(x, y, Color.white);
    }

    public void render(int x, int y, Color color) {
        double ti = (System.currentTimeMillis() / 220.0);
        Vector2f position = calculateAlignment(x, y, text);
        for (int i = 0; i < length; i++) {
            font.drawString(
                position.x + widths[i],
                (float) (position.y - Math.sin(
                    Math.max(Math.min(
                            (i * (Math.PI / (length * 2.0)))
                            + (ti % Math.PI),
                            Math.PI), 0)
                ) * 50.0 * wobbleplier),
                chars[i], color);
        }
    }
}
