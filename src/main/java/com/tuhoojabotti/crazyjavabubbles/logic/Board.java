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

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class Board {

    private Bubble[][] bubbles;
    private int width;
    private int height;

    public Board(int w, int h) {
        bubbles = new Bubble[h][w];
        width = w;
        height = h;
    }

    public void init() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bubbles[y][x] = new Bubble(x, y);
            }
        }
    }

    private boolean isOnBoard(int x, int y) {
        return x > 0 && x < width
                && y > 0 && y < height && bubbles[y][x] != null;
    }

    private Bubble get(int x, int y) {
        if (!isOnBoard(x, y)) {
            return null;
        }
        return bubbles[y][x];
    }

    public int pop(int x, int y) {
        int amount = 1;

        if (!isOnBoard(x, y)) {
            return -1;
        }

        Bubble current = bubbles[y][x];
        bubbles[y][x] = null;

        if (current.equals(get(x + 1, y))) {
            amount += 1;
            amount += pop(x + 1, y);
        }
        if (current.equals(get(x - 1, y))) {
            amount += 1;
            amount += pop(x - 1, y);
        }
        if (current.equals(get(x, y + 1))) {
            amount += 1;
            amount += pop(x, y + 1);
        }
        if (current.equals(get(x, y - 1))) {
            amount += 1;
            amount += pop(x, y - 1);
        }
        
//        if (amount == 1) {
//            bubbles[y][x] = current;
//            return -1;
//        }

        return amount;
    }

    public Bubble[][] getBubbles() {
        return bubbles;
    }
}
