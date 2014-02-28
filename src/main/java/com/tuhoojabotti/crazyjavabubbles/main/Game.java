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
package com.tuhoojabotti.crazyjavabubbles.main;

import static com.tuhoojabotti.crazyjavabubbles.main.Util.fatalError;
import com.tuhoojabotti.crazyjavabubbles.states.SplashScreen;
import com.tuhoojabotti.crazyjavabubbles.states.CrazyGame;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The game's main class.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class Game extends StateBasedGame {

    public static void main(String[] args) {
        new Game().run();
    }

    /**
     * The game state IDs. Used for example for enterState(ID).
     */
    public static final int SPLASHSCREEN = 0;
    public static final int GAME = 1;

    /**
     * Name of the game, used as the title of the window.
     */
    public static final String NAME = "Crazy Bubbles";

    /**
     * Creates a new Game.
     */
    public Game() {
        // Set the title of the game window to NAME.
        super(NAME);
        Settings.loadSettings();
    }

    /**
     * Creates and calls init method of each game state. Also changes
     * application icon to a custom one.
     *
     * @param gc game container
     */
    @Override
    public void initStatesList(GameContainer gc) {
        this.addState(new SplashScreen(SPLASHSCREEN));
        this.addState(new CrazyGame(GAME));

        // If we are initializing the AppGameContainer, set custom icon.
        if (gc instanceof AppGameContainer) {
            setIcons((AppGameContainer) gc);
        }
    }

    /**
     * Set the icon of the game window and other places.
     *
     * @param app
     */
    private void setIcons(AppGameContainer app) {
        try {
            app.setIcons(new String[]{"graphics/icon16.png",
                "graphics/icon32.png"});
        } catch (SlickException e) {
        }
    }

    private void startMusic() {
        if (Settings.is("music_on")) {
            try {
                Music gameMusic = new Music("sounds/uk.ogg");
                gameMusic.loop(1, (int) Settings.get("music_volume") / 100f);
            } catch (SlickException e) {
                fatalError("Could not load music.", this.getClass(), e);
            }
        }
    }

    /**
     * Run the application.
     */
    public void run() {
        try {
            startMusic();

            AppGameContainer app = new AppGameContainer(this);
            app.setDisplayMode(Settings.SCREEN_WIDTH,
                Settings.SCREEN_HEIGHT, Settings.is("fullscreen"));
            app.setShowFPS(false);
            // Update game logic every 15-20ms.
            app.setMinimumLogicUpdateInterval(15);
            app.setMaximumLogicUpdateInterval(20);
            // Don't pause if window loses focus.
            app.setAlwaysRender(true);

            app.start();
        } catch (SlickException e) {
            fatalError("Failed to start the game!", this.getClass(), e);
        }
    }
}
