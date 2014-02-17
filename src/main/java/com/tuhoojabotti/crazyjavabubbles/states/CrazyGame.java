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
package com.tuhoojabotti.crazyjavabubbles.states;

import com.tuhoojabotti.crazyjavabubbles.Game;
import static com.tuhoojabotti.crazyjavabubbles.Util.fatalError;
import com.tuhoojabotti.crazyjavabubbles.gui.Settings;
import com.tuhoojabotti.crazyjavabubbles.logic.Bubble;
import com.tuhoojabotti.crazyjavabubbles.logic.CrazyGameLogic;
import com.tuhoojabotti.crazyjavabubbles.renderer.CrazyGameRenderer;
import com.tuhoojabotti.crazyjavabubbles.renderer.GameOverRenderer;
import java.io.IOException;
import java.util.Set;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.ResourceLoader;

/**
 * The game state that handles updating and rendering of the game.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class CrazyGame extends StateWrapper {

    /**
     * The game logic.
     */
    private CrazyGameLogic logic;

    /**
     * The game renderer.
     */
    private CrazyGameRenderer renderer;

    /**
     * The sound effect for popping the bubbles.
     */
    private Audio blimSound;

    /**
     * Game over overlay.
     */
    private GameOverRenderer gameOver;
    private boolean newGame;

    /**
     * Create a new game.
     *
     * @param ID the id of this game state
     */
    public CrazyGame(int ID) {
        super(ID);
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {
        logic = new CrazyGameLogic();

        if (Settings.is("sound_on")) {
            try {
                blimSound = AudioLoader.getAudio("OGG",
                    ResourceLoader.getResourceAsStream("sounds/blim.ogg"));
            } catch (IOException e) {
                fatalError("Could not load sounds.", this.getClass(), e);
            }
        }
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame game) {
        gameOver = new GameOverRenderer(gc, gc.getGraphics(), logic);
        newGame = false;
        logic.init();
        renderer = new CrazyGameRenderer(logic, gc.getGraphics(), gc, getMousePosition());
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics gfx) {
        renderer.render();
        if (logic.isGameOver()) {
            gameOver.render();
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        super.update(gc, sbg, delta);

        renderer.update(delta);
        logic.updateSelection(getMousePosition());

        if (logic.isGameOver()) {
            gameOver.update(delta);
            if (newGame) {
                sbg.enterState(Game.SPLASHSCREEN, new FadeOutTransition(Color.black, 200), new EmptyTransition());
            }
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (button == 0 && !logic.isGameOver()) {
            // Pop the bubbles!
            Set<Bubble> bubbles = logic.pop();

            if (bubbles != null) {
                if (Settings.is("sound_on")) {
                    blimSound.playAsSoundEffect((float) Math.sqrt(bubbles.size())
                        / 2.0f, (int) Settings.get("sound_volume") / 100f, false);
                }
                // Force update of selection.
                logic.forceUpdateSelection(getMousePosition());
                renderer.explode(bubbles);
            }
        }
        if (logic.isGameOver() && gameOver.canSkip()) {
            newGame = true;
        }
    }
}
