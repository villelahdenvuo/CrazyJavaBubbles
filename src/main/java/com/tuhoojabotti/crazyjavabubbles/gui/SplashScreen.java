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
import com.tuhoojabotti.crazyjavabubbles.logic.Bubble;
import com.tuhoojabotti.crazyjavabubbles.renderer.BubbleRenderer;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.RotateTransition;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class SplashScreen extends BasicGameState {

    private final int ID;
    private ArrayList<Bubble> bubbles;
    private Random rand;
    private TextRenderer titleText;
    private TextRenderer authorText;
    private BubbleRenderer bubbleRenderer;

    /**
     * Create a new splash screen.
     *
     * @param ID the ID of this state
     */
    public SplashScreen(int ID) {
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
     * Initialise the splash screen.
     *
     * @param gc game container
     * @param sbg game itself
     * @throws SlickException
     */
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        bubbles = new ArrayList<>();
        rand = new Random();

        titleText = new TextRenderer("Calibri", Font.BOLD, 80);
        titleText.setHorizontalAlign(TextRenderer.ALIGN_CENTER);
        authorText = new TextRenderer("Calibri", Font.BOLD, 30);
        authorText.setHorizontalAlign(TextRenderer.ALIGN_CENTER);

        //bubbleRenderer = new BubbleRenderer(gc.getGraphics());

        for (int i = 0; i < 100; i++) {
            bubbles.add(new Bubble(rand.nextInt(gc.getWidth()) - 20, -20 - rand.nextInt(gc.getHeight())));
        }

        TextureImpl.bindNone();
    }

    /**
     * Render the splash screen.
     *
     * @param gc game container
     * @param game game itself
     * @param gfx graphics controller
     * @throws SlickException
     */
    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics gfx) throws SlickException {
        gfx.setAntiAlias(true);
//        for (Bubble bubble : bubbles) {
//            bubbleRenderer.render(bubble, (int) bubble.x, (int) bubble.y);
//        }
        gfx.setAntiAlias(false);

        titleText.render(gc.getWidth() / 2, 50, "Crazy Bubbles");
        authorText.render(gc.getWidth() / 2, 160, "by Ville 'Tuhis' Lahdenvuo", Color.gray);
        authorText.render(gc.getWidth() / 2, 200, "for JavaLabra 2014", Color.gray);
    }

    /**
     * Update the splash screen.
     *
     * @param gc game container
     * @param sbg game itself
     * @param i delta time
     * @throws SlickException
     */
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        long t = gc.getTime();
        boolean end = true;

        for (Bubble b : bubbles) {
            b.setLocation(b.getX() + Math.sin(t / 200) * rand.nextDouble() * 10, b.getY() + 0.5 * i);
            if (b.getY() < 0) {
                end = false;
            }
        }

        if (end) {
            sbg.enterState(Application.GAME, new EmptyTransition(), new RotateTransition());
        }
    }
}
