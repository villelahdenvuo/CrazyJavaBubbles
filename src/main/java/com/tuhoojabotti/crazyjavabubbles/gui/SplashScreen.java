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
import com.tuhoojabotti.crazyjavabubbles.renderer.RenderSettings;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.RotateTransition;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class SplashScreen extends BasicGameState {

    private final int ID;
    private ArrayList<BubbleRenderer> bubbleRenderers;
    private ArrayList<Bubble> bubbles;
    private Random rand;
    private TextRenderer titleText;
    private TextRenderer authorText;
    private Vector2f mousePosition;

    private float startTime;
    private Vector2f titlePos;
    private Vector2f authorPos;

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
        startTime = gc.getTime() + 1000;
        bubbleRenderers = new ArrayList<>();
        bubbles = new ArrayList<>();
        rand = new Random();
        mousePosition = new Vector2f();

        titleText = new TextRenderer("Calibri", Font.BOLD, 80);
        titleText.setHorizontalAlign(TextRenderer.ALIGN_CENTER);
        authorText = new TextRenderer("Calibri", Font.BOLD, 30);
        authorText.setHorizontalAlign(TextRenderer.ALIGN_CENTER);

        titlePos = new Vector2f(gc.getWidth() / 2, -200);
        authorPos = new Vector2f(-200, gc.getHeight() / 2 - 40);

        int r = RenderSettings.BUBBLE_RADIUS;

        for (int y = 0; y <= gc.getHeight() / r; y++) {
            for (int x = 0; x < gc.getWidth() / r; x++) {
                if (y < 8 || y > 10) {
                    Bubble b = new Bubble(x, y);
                    bubbles.add(b);
                    bubbleRenderers.add(new BubbleRenderer(b, gc.getGraphics(), mousePosition));
                }
            }
        }

        TextureImpl.bindNone();
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        //mousePosition.set(newx, newy);
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
        for (BubbleRenderer renderer : bubbleRenderers) {
            renderer.render(-1, -6);
        }

        Color shadow = new Color(0, 0, 0, 0.7f);

        titleText.render((int) titlePos.x - 3, (int) titlePos.y - 3, "Crazy Bubbles", shadow);
        titleText.render((int) titlePos.x, (int) titlePos.y, "Crazy Bubbles");

        authorText.render((int) authorPos.x - 2, (int) authorPos.y - 2, "by Ville 'Tuhis' Lahdenvuo", shadow);
        authorText.render((int) authorPos.x, (int) authorPos.y, "by Ville 'Tuhis' Lahdenvuo");
        authorText.render((int) authorPos.x - 2, (int) authorPos.y + 40 - 2, "for JavaLabra 2014", shadow);
        authorText.render((int) authorPos.x, (int) authorPos.y + 40, "for JavaLabra 2014");

    }

    /**
     * Update the splash screen.
     *
     * @param gc game container
     * @param sbg game itself
     * @param delta delta time
     * @throws SlickException
     */
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        for (BubbleRenderer renderer : bubbleRenderers) {
            renderer.update(gc, delta);
        }

        mousePosition.set(
                gc.getWidth() / 2 + (float) Math.cos(gc.getTime() / 100) * gc.getWidth(),
                gc.getHeight() / 2 + (float) Math.sin(gc.getTime() / 100) * gc.getWidth());

        titlePos.y = titlePos.y + (50 - titlePos.y) * 0.05f;

        if (titlePos.y > 35f) {
            authorPos.x = authorPos.x + (titlePos.x - authorPos.x) * 0.03f;
        }

        if (authorPos.x > titlePos.x - 5) {
            authorPos.x = authorPos.x + (gc.getWidth() * 3f - authorPos.x) * 0.03f;
        }

        if (authorPos.x > gc.getWidth() * 1.6f) {
            sbg.enterState(Application.MAINMENU, new FadeOutTransition(), new EmptyTransition());
        }
    }
}
