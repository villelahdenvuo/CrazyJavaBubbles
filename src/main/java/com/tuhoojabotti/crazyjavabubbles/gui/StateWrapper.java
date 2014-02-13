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

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A wrapper for BasicGameState that handles some basic stuff like keeping track
 * of the mouse.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public abstract class StateWrapper extends BasicGameState {

    private Vector2f mousePosition;
    private final int ID;
    private boolean exitRequested;

    public StateWrapper(int ID) {
        this.ID = ID;
        mousePosition = new Vector2f();
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if (exitRequested) {
            gc.exit();
        }
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        mousePosition.set(newx, newy);
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        mousePosition.set(newx, newy);
    }

    @Override
    public void keyPressed(int key, char c) {
        switch (key) {
            case 1:
                exitRequested = true;
                break;
        }
    }

    public Vector2f getMousePosition() {
        return mousePosition;
    }

    /**
     * Tells if exit was requested and resets it to false.
     *
     * @return is exit requested
     */
    public boolean isExitRequested() {
        if (exitRequested) {
            exitRequested = false;
            return true;
        }
        return false;
    }

    public void setExitRequested(boolean exitRequested) {
        this.exitRequested = exitRequested;
    }
}
