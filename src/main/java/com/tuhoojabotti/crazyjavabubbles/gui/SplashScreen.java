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

import com.tuhoojabotti.crazyjavabubbles.logic.Bubble;
import java.util.ArrayList;
import java.util.Random;
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
public class SplashScreen extends BasicGameState {

    private final int ID;
    private ArrayList<Bubble> renderables;
    private Random rand;
    
    SplashScreen(int id) {
        ID = id;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        renderables = new ArrayList<>();
        rand = new Random();
        
        for (int i = 0; i < 500; i++) {
            renderables.add(new Bubble(rand.nextInt(gc.getScreenWidth())-20, -20-rand.nextInt(gc.getScreenHeight())));
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics gfx) throws SlickException {
        gfx.setColor(Color.white);
        gfx.drawString("Hello!", 10, 30);
        
        for (Drawable drawable : renderables) {
            drawable.render(gfx);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        long t = gc.getTime();
        
        for (Bubble b : renderables) {
            b.setLocation(b.getX() + Math.sin(t / 100) * rand.nextDouble() * 10, b.getY() + 0.5 * i);
            if (b.getY() > gc.getScreenHeight()) {
                b.setLocation(rand.nextInt(gc.getScreenWidth())-20, -20-rand.nextInt(200));
            }
        }
    }
    
}
