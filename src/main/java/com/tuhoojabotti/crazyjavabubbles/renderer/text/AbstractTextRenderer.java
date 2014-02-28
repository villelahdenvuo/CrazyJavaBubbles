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

import com.tuhoojabotti.crazyjavabubbles.main.Util;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.ResourceLoader;

/**
 * A foundation for a text renderer.
 * It handles font loading and alignment, but not actual rendering.
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public abstract class AbstractTextRenderer {

    /**
     * How to align the text.
     */
    public enum Align {

        LEFT, RIGHT, CENTER, TOP, MIDDLE, BOTTOM
    }

    /**
     * The font to use.
     */
    protected UnicodeFont font;

    /**
     * Horizontal alignment of the text.
     */
    private Align horizontalAlign = Align.LEFT;

    /**
     * Vertical alignment of the text.
     */
    private Align verticalAlign = Align.TOP;

    /**
     *
     * @param fontName
     * @param size
     */
    public AbstractTextRenderer(String fontName, int size) {
        try {
            font = loadFont(fontName, size);
            font.addAsciiGlyphs();
            font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));

            font.loadGlyphs();
        } catch (SlickException e) {
            Util.fatalError("Failed to initialize font.", AbstractTextRenderer.class, e);
        }
    }

    private UnicodeFont loadFont(String fontName, int size) {
        try {
            InputStream inputStream = ResourceLoader.getResourceAsStream(
                "fonts/" + fontName + ".ttf");
            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtFont = awtFont.deriveFont(size); // Set font size.
            return new UnicodeFont(awtFont, size, false, false);
        } catch (FontFormatException | IOException e) {
            Font awtFont = new Font("Arial", Font.PLAIN, size);
            return new UnicodeFont(awtFont);
        }
    }

    public void setHorizontalAlign(Align horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
    }

    public void setVerticalAlign(Align verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public Align getHorizontalAlign() {
        return horizontalAlign;
    }

    public Align getVerticalAlign() {
        return verticalAlign;
    }

    /**
     * Calculate where to render the text after alignment.
     *
     * @param x
     * @param y
     * @param text the text to render
     * @return the new position of the top left corner
     */
    protected Vector2f calculateAlignment(int x, int y, String text) {
        switch (horizontalAlign) {
            case RIGHT:
                x -= font.getWidth(text);
                break;
            case CENTER:
                x -= font.getWidth(text) / 2;
                break;
        }
        switch (verticalAlign) {
            case BOTTOM:
                y -= font.getHeight(text);
                break;
            case MIDDLE:
                y -= font.getHeight(text) / 2;
                break;
        }
        return new Vector2f(x, y);
    }
}
