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

import com.tuhoojabotti.crazyjavabubbles.logic.CrazyGameLogic;
import java.awt.Font;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class CrazyGameRenderer {

    private final Graphics gfx;
    private final BoardRenderer boardRenderer;
    private TextRenderer scoreText;
    private final GameContainer gameContainer;

    /**
     * Create new {@link CrazyGame} renderer.
     *
     * @param gfx graphics controller
     * @param gc game container
     * @param mouse mouse position
     */
    public CrazyGameRenderer(Graphics gfx, GameContainer gc, Point mouse) {
        this.gfx = gfx;
        gameContainer = gc;
        boardRenderer = new BoardRenderer(gfx, mouse);
        try {
            scoreText = new TextRenderer("Arial", Font.PLAIN, 16);
            scoreText.setVerticalAlign(TextRenderer.ALIGN_BOTTOM);
        } catch (SlickException ex) {
            Logger.getLogger(CrazyGameRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Render the game.
     *
     * @param game game itself
     */
    public void render(CrazyGameLogic game) {
        scoreText.render(2, gameContainer.getHeight() - 2, "score: "
                + game.getScore());

        boardRenderer.render(game.getBoard(), RenderSettings.BOARD_MARGIN,
                RenderSettings.BOARD_MARGIN);
    }

}
