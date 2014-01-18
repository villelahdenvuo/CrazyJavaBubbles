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

import java.awt.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class Text {

    public static final int ALIGN_LEFT = 0, ALIGN_RIGHT = 1, ALIGN_CENTER = 2;
    private final UnicodeFont U_FONT;

    private int align = ALIGN_LEFT;

    public Text(String f, int style, int size) throws SlickException {
        Font font = new Font(f, style, size);
        U_FONT = new UnicodeFont(font, font.getSize(), font.isBold(), font.isItalic());
        U_FONT.addAsciiGlyphs();
        //uFont.addGlyphs(400, 600);

        U_FONT.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
        U_FONT.loadGlyphs();
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public void draw(int x, int y, String text) {
        draw(x, y, text, Color.white);
    }

    public void draw(int x, int y, String text, Color c) {
        switch (align) {
            case ALIGN_LEFT:
                U_FONT.drawString(x, y, text, c);
                break;
            case ALIGN_RIGHT:
                U_FONT.drawString(x - U_FONT.getWidth(text), y, text, c);
                break;
            case ALIGN_CENTER:
                U_FONT.drawString(x - U_FONT.getWidth(text) / 2, y, text, c);
                break;
        }
    }

}
