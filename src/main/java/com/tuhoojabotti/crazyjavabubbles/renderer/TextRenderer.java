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

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.util.ResourceLoader;

/**
 * A simple text rendering utility.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class TextRenderer {

    public static final int ALIGN_LEFT = 0, ALIGN_RIGHT = 1, ALIGN_CENTER = 2;
    public static final int ALIGN_TOP = 0, ALIGN_MIDDLE = 1, ALIGN_BOTTOM = 2;
    //private final UnicodeFont U_FONT;
    private UnicodeFont font;

    private int horizontalAlign = ALIGN_LEFT;
    private int verticalAlign = ALIGN_TOP;

    /**
     * Create a new text renderer.
     *
     * @param fontName font to use
     * @param style style of the font (e.g. Font.BOLD)
     * @param size size of the font
     * @throws SlickException
     */
    public TextRenderer(String fontName, int size) throws SlickException {
        try {
            InputStream inputStream = ResourceLoader.getResourceAsStream(fontName + ".ttf");
            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtFont = awtFont.deriveFont(size); // set font size
            font = new UnicodeFont(awtFont, size, false, false);
        } catch (FontFormatException | IOException e) {
            Font awtFont = new Font("Times New Roman", Font.PLAIN, size);
            font = new UnicodeFont(awtFont);            
        }
        font.addAsciiGlyphs();
        font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
        font.loadGlyphs();
    }

    /**
     * Set the horizontal alignment of the text.
     *
     * @param horizontalAlign
     */
    public void setHorizontalAlign(int horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
    }

    /**
     * Set the vertical alignment of the text.
     *
     * @param verticalAlign
     */
    public void setVerticalAlign(int verticalAlign) {
        this.verticalAlign = verticalAlign;
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
        switch (horizontalAlign) {
            case ALIGN_RIGHT:
                x = x - font.getWidth(text);
                break;
            case ALIGN_CENTER:
                x = x - font.getWidth(text) / 2;
                break;
        }
        switch (verticalAlign) {
            case ALIGN_BOTTOM:
                y = y - font.getHeight(text);
                break;
            case ALIGN_MIDDLE:
                y = y - font.getHeight(text) / 2;
                break;
        }

        font.drawString(x, y, text, color);
    }

}
