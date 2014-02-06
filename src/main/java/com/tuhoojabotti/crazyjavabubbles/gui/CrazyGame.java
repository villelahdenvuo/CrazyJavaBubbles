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
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class CrazyGame extends GameWrapper {

    private CrazyGameLogic logic;
    private CrazyGameRenderer renderer;

    public CrazyGame(int ID) {
        super(ID);
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.init(gc, sbg);
        logic = new CrazyGameLogic();
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame game) throws SlickException {
        logic.init();
        renderer = new CrazyGameRenderer(logic.getBoard(), gc.getGraphics(), gc, getMousePosition());
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics gfx) throws SlickException {
        renderer.render(logic);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        super.update(gc, sbg, delta);

        renderer.update(delta);
        logic.select(getMousePosition());

        if (logic.isGameOver()) {
            sbg.enterState(Application.SPLASHSCREEN, new FadeOutTransition(), new FadeInTransition());
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (button == 0) {
            int count = logic.pop();
            if (count > 0) {
                logic.forceSelect(getMousePosition());
                renderer.explode(getMousePosition(), count);
            }
        }
    }
}
