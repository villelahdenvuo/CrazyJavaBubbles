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
 * A simple text rendering utility.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class TextRenderer extends AbstractTextRenderer {

    /**
     * Create a new text renderer.
     *
     * @param fontName font to use
     * @param size size of the font
     */
    public TextRenderer(String fontName, int size) {
        super(fontName, size);
    }

    /**
     * Render a string with white colour.
     *
     * @param x
     * @param y
     * @param text the text to render
     */
    public void render(int x, int y, String text) {
        render(x, y, text, Color.white);
    }

    /**
     * Render a string with any colour.
     *
     * @param x
     * @param y
     * @param text the text to render
     * @param color the colour of the text
     */
    public void render(int x, int y, String text, Color color) {
        Vector2f position = calculateAlignment(x, y, text);
        font.drawString(position.x, position.y, text, color);
    }
}
