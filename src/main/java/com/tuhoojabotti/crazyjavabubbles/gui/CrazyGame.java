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

import com.tuhoojabotti.crazyjavabubbles.logic.CrazyGameLogic;
import com.tuhoojabotti.crazyjavabubbles.renderer.CrazyGameRenderer;
import com.tuhoojabotti.crazyjavabubbles.renderer.RenderSettings;
import java.awt.Point;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class CrazyGame extends BasicGameState {

    private final int ID;
    private CrazyGameLogic logic;
    private CrazyGameRenderer renderer;
    private Point mousePosition;

    /**
     * Create a new CrazyGame.
     * @param id of the game state
     */
    public CrazyGame(int id) {
        ID = id;
    }

    /**
     * @return ID of this game state
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * Initialise everything.
     *
     * @param gc game container
     * @param sbg game itself
     * @throws SlickException
     */
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        mousePosition = new Point();
        logic = new CrazyGameLogic();
        logic.init();
        renderer = new CrazyGameRenderer(gc.getGraphics(), gc, mousePosition);
    }

    /**
     * Update game graphics
     *
     * @param gc game container
     * @param sbg game itself
     * @param gfx graphics controller
     * @throws SlickException
     */
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics gfx) throws SlickException {
        renderer.render(logic);
    }

    /**
     * Update the game logic
     *
     * @param gc game container
     * @param sbg game itself
     * @param i delta time
     * @throws SlickException
     */
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        logic.select(getMousePositionOnBoard());
        
        if (logic.isGameOver()) {
            gc.exit();
        }
    }

    /**
     * A listener for mouse movements.
     *
     * @param oldx
     * @param oldy
     * @param newx
     * @param newy
     */
    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        mousePosition.move(newx, newy);
    }

    /**
     * A listener for mouse drag movements.
     *
     * @param oldx
     * @param oldy
     * @param newx
     * @param newy
     */
    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        mousePosition.move(newx, newy);
    }

    /**
     * A listener for mouse release events.
     *
     * @param button
     * @param x
     * @param y
     */
    @Override
    public void mouseReleased(int button, int x, int y) {
        mousePosition.move(x, y);
        if (button == 0) {
            logic.pop();
        }
    }

    private Point getMousePositionOnBoard() {
        int margin = RenderSettings.BOARD_MARGIN,
                r = RenderSettings.BUBBLE_RADIUS;
        return new Point(Math.round((mousePosition.x - margin) / r),
                Math.round((mousePosition.y - margin) / r));
    }

}
