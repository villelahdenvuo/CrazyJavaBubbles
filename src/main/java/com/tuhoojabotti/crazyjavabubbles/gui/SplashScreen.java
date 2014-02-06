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

import static com.tuhoojabotti.crazyjavabubbles.Util.curveValue;
import com.tuhoojabotti.crazyjavabubbles.renderer.TextRenderer;
import com.tuhoojabotti.crazyjavabubbles.logic.Bubble;
import com.tuhoojabotti.crazyjavabubbles.renderer.BubbleRenderer;
import com.tuhoojabotti.crazyjavabubbles.renderer.RenderSettings;
import java.awt.Font;
import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A cool splash screen for the game!
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class SplashScreen extends GameWrapper {

    private ArrayList<BubbleRenderer> bubbleRenderers;
    private ArrayList<Bubble> bubbles;
    private TextRenderer titleText;
    private TextRenderer authorText;
    private Vector2f mousePosition;

    private Vector2f titlePos;
    private Vector2f authorPos;

    /**
     * Create a new splash screen.
     *
     * @param ID the ID of this state
     */
    public SplashScreen(int ID) {
        super(ID);
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
        bubbleRenderers = new ArrayList<>();
        bubbles = new ArrayList<>();
        mousePosition = new Vector2f();

        titleText = new TextRenderer("Calibri", Font.BOLD, 84);
        titleText.setHorizontalAlign(TextRenderer.ALIGN_CENTER);
        authorText = new TextRenderer("Calibri", Font.BOLD, 30);
        authorText.setHorizontalAlign(TextRenderer.ALIGN_CENTER);

        int r = RenderSettings.BUBBLE_RADIUS;

        for (int y = 0; y <= gc.getHeight() / r + 1; y++) {
            for (int x = 0; x < gc.getWidth() / r + 1; x++) {
                if ((y < 8 || y > 10) && (y < 2 || y > 4 || x < 4 || x > 20)) {
                    Bubble b = new Bubble(x, y);
                    b.setSelected(true);
                    bubbles.add(b);
                    bubbleRenderers.add(new BubbleRenderer(b, gc.getGraphics(), mousePosition));
                }
            }
        }

        TextureImpl.bindNone();
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame game) throws SlickException {
        setExitRequested(false);
        titlePos = new Vector2f(gc.getWidth() / 2, -200);
        authorPos = new Vector2f(-200, gc.getHeight() / 2 - 50);
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
            renderer.render(-RenderSettings.BUBBLE_RADIUS / 2, -RenderSettings.BUBBLE_RADIUS / 2);
        }

        titleText.render((int) titlePos.x, (int) titlePos.y, "Crazy Bubbles");
        authorText.render((int) authorPos.x, (int) authorPos.y, "by Ville 'Tuhis' Lahdenvuo");
        authorText.render((int) authorPos.x, (int) authorPos.y + 40, "<3 you all");
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
        if (isExitRequested()) {
            sbg.enterState(Application.GAME);
        }

        for (BubbleRenderer renderer : bubbleRenderers) {
            renderer.update(gc, delta);
        }

        updateFakeMouse(gc);
        updateTexts(gc);
    }
    
    private void updateFakeMouse(GameContainer gc) {
        float w = gc.getWidth(), h = gc.getHeight();
        long t = gc.getTime();

        mousePosition.set(w / 2 + (float) Math.cos(t / 100.0) * w,
                h / 2 + (float) Math.sin(t / 100.0) * w);
    }

    private void updateTexts(GameContainer gc) {
        titlePos.y = curveValue(48, titlePos.y, 0.05f);
        if (authorPos.x > gc.getWidth() + 200) {
            setExitRequested(true);
        } else if (authorPos.x > titlePos.x - 5) {
            authorPos.x = curveValue(gc.getWidth() * 2, authorPos.x, 0.04f);
        } else if (titlePos.y > 38) {
            authorPos.x = curveValue(titlePos.x, authorPos.x, 0.02f);
        }
    }
}
