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
package com.tuhoojabotti.crazyjavabubbles.renderer.text;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

/**
 * A text renderer that renders text that jumps to a beat.
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class BeatTextRenderer extends AbstractTextRenderer {

    /**
     * How much does the text jump.
     */
    private float jumpHeight;
    /**
     * The text to render.
     */
    private String text;
    /**
     * Characters of the text.
     */
    private String[] chars;
    /**
     * The width of the text up to i letters.
     */
    private float[] widths;
    /**
     * The length of the string.
     */
    private int length;
    /**
     * How fast to jump.
     */
    private final double beatFrequency = 220.0;

    /**
     * Create a new text with default jump height.
     *
     * @param fontName name of the font
     * @param size size of the text
     * @param text the text to draw
     */
    public BeatTextRenderer(String fontName, int size, String text) {
        this(fontName, size, text, 50f);
    }

    /**
     * Create a new text with custom jump height.
     *
     * @param font name of the font
     * @param size size of the text
     * @param text the text to draw
     * @param height how much to boost or reduce the height
     */
    public BeatTextRenderer(String font, int size, String text, float height) {
        super(font, size);
        this.text = text;
        jumpHeight = height;
        precalculate();
    }

    /**
     * Calculate things for faster rendering.
     */
    private void precalculate() {
        length = text.length();
        chars = new String[length];
        widths = new float[length];
        for (int i = 0; i < length; i++) {
            widths[i] = font.getWidth(text.substring(0, i));
            chars[i] = "" + text.charAt(i);
        }
    }

    /**
     * Draw the text with white color.
     *
     * @param x
     * @param y
     */
    public void render(int x, int y) {
        render(x, y, Color.white);
    }

    /**
     * Draw the text with any color.
     *
     * @param x
     * @param y
     * @param color
     */
    public void render(int x, int y, Color color) {
        double ti = (System.currentTimeMillis() / beatFrequency);
        Vector2f position = calculateAlignment(x, y, text);
        for (int i = 0; i < length; i++) {
            font.drawString(
                position.x + widths[i],
                // Just calculate y-coord with this simple equation.
                (float) (position.y - Math.sin(
                    Math.max(Math.min((i * (Math.PI / (length * 2.0)))
                            + (ti % Math.PI), Math.PI), 0)
                ) * jumpHeight),
                chars[i], color);
        }
    }
}
