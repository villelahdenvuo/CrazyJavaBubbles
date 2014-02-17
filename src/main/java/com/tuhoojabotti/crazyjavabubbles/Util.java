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
package com.tuhoojabotti.crazyjavabubbles;

import com.tuhoojabotti.crazyjavabubbles.gui.RenderSettings;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.newdawn.slick.geom.Vector2f;

/**
 * Helpful utilities.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class Util {

    /**
     * Change a value smoothly.
     *
     * @param current
     * @param old
     * @param smoothness the smaller the smoother
     * @return something between new and old
     */
    public static float curveValue(float current, float old, float smoothness) {
        return old + (current - old) * smoothness;
    }

    /**
     * Create a nice pop up window telling why the game must close.
     *
     * @param message why the game must close
     * @param caller where did the problem occur
     * @param e what was the problem
     */
    public static void fatalError(String message, Class caller, Throwable e) {
        Logger.getLogger(caller.getName()).log(Level.SEVERE, message, e);
        JOptionPane.showMessageDialog(null, message,
                Game.NAME, JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    /**
     * Convert screen coordinates to board coordinates.
     *
     * @param position on screen
     * @return position on board
     */
    public static Point getPositionOnBoard(Vector2f position) {
        int margin = RenderSettings.BOARD_MARGIN,
                r = RenderSettings.BUBBLE_RADIUS;

        return new Point(
                Math.round((position.x - margin - r / 2) / r),
                Math.round((position.y - margin - r / 2) / r));
    }
}
