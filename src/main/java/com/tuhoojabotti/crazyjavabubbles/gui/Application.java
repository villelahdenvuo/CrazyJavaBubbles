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

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class Application extends StateBasedGame {

    /**
     * The splash screen.
     */
    public static final int SPLASHSCREEN = 0;

    /**
     * The main menu.
     */
    public static final int MAINMENU = 1;

    /**
     * The game itself.
     */
    public static final int GAME = 2;

    /**
     * Name of the game, used as the title of the window.
     */
    public static final String NAME = "Crazy Bubbles";

    /**
     * Width of the window.
     */
    public static final int WIDTH = 800;

    /**
     * Height of the window.
     */
    public static final int HEIGHT = 600;

    /**
     * Preferred framerate.
     */
    public static final int FPS = 60;

    /**
     * Version of the game.
     */
    public static final double VERSION = 1.0;

    /**
     * Creates a new Application.
     */
    public Application() {
        super(NAME);
    }

    /**
     * Calls init method of each game state, and set's the state ID.
     *
     * @param gc game container
     * @throws SlickException
     */
    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
//        this.addState(new SplashScreen(SPLASHSCREEN));
//        this.addState(new MainMenu(MAINMENU));
        this.addState(new CrazyGame(GAME));
    }

    /**
     * Run the application.
     */
    public void run() {
        try {
            AppGameContainer app = new AppGameContainer(this);
            app.setDisplayMode(WIDTH, HEIGHT, false);
            app.setTargetFrameRate(FPS);
            app.setShowFPS(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

}
