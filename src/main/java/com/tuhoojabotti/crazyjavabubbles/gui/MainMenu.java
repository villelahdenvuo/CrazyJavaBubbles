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

import com.tuhoojabotti.crazyjavabubbles.renderer.TextRenderer;
import java.awt.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class MainMenu extends BasicGameState {

    private final int ID;
    private TextRenderer titleText;

    /**
     * Create a new main menu.
     *
     * @param ID the id of this game state
     */
    public MainMenu(int ID) {
        this.ID = ID;
    }

    /**
     * @return the ID of this state
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * Initialise the main menu.
     *
     * @param gc game container
     * @param sbg game itself
     * @throws SlickException
     */
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        titleText = new TextRenderer("Calibri", Font.BOLD, 80);
        titleText.setHorizontalAlign(TextRenderer.ALIGN_CENTER);
    }

    /**
     * Render the main menu.
     *
     * @param gc game container
     * @param game game itself
     * @param gfx graphics controller
     * @throws SlickException
     */
    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics gfx) throws SlickException {
        gfx.setColor(Color.white);
        titleText.render(gc.getWidth() / 2, 50, "Crazy Bubbles");

    }

    /**
     * Update the main menu.
     *
     * @param gc game container
     * @param sbg game itself
     * @param i delta time
     * @throws SlickException
     */
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
    }
}
