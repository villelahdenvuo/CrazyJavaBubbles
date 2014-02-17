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
import static com.tuhoojabotti.crazyjavabubbles.Util.curveValue;
import static com.tuhoojabotti.crazyjavabubbles.Util.fatalError;
import com.tuhoojabotti.crazyjavabubbles.gui.TextRenderer;
import com.tuhoojabotti.crazyjavabubbles.logic.Bubble;
import com.tuhoojabotti.crazyjavabubbles.renderer.BubbleRenderer;
import com.tuhoojabotti.crazyjavabubbles.gui.RenderSettings;
import com.tuhoojabotti.crazyjavabubbles.gui.WobbleTextRenderer;
import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A cool splash screen for the game!
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class SplashScreen extends StateWrapper {

    // For the background.
    private ArrayList<BubbleRenderer> bubbleRenderers;
    private ArrayList<Bubble> bubbles;
    private Vector2f mousePosition;
    // For the texts.
    private WobbleTextRenderer titleText;
    private WobbleTextRenderer authorText;
    private WobbleTextRenderer greetingText;
    private Vector2f titlePos;
    private Vector2f authorPos;

    private final String[] greetings = new String[]{
        "<3 y'all",
        "Hey mum, look at me!",
        "JavaLabra <3",
        "Matti, missä oluet?",
        "TKO-äly rulz!",
        "CoolBasic respect!",
        "Wow, such splash; so screen.",
        "- Insert Bitcoin -"
    };

    /**
     * Create a new splash screen.
     *
     * @param ID the ID of this state
     */
    public SplashScreen(int ID) {
        super(ID);
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {
        bubbleRenderers = new ArrayList<>();
        bubbles = new ArrayList<>();
        mousePosition = new Vector2f();

        createBackground(gc.getWidth(), gc.getHeight(), gc.getGraphics());
        createTexts();
    }

    private void createTexts() {
        try {
            titleText = new WobbleTextRenderer("sweet-as-candy.regular", 60, "Crazy Bubbles");
            authorText = new WobbleTextRenderer("goodtimes.regular", 28, "by Ville 'Tuhis' Lahdenvuo", 0.3f);
        } catch (SlickException e) {
            fatalError("Could not load font.", this.getClass(), e);
        }
        titleText.setHorizontalAlign(TextRenderer.ALIGN_CENTER);
        authorText.setHorizontalAlign(TextRenderer.ALIGN_CENTER);
    }

    private void createBackground(int w, int h, Graphics gfx) {
        int r = RenderSettings.BUBBLE_RADIUS;

        for (int y = 0; y <= h / r + 1; y++) {
            for (int x = 0; x < w / r + 1; x++) {
                if ((y < 8 || y > 10) && (y < 1 || y > 4 || x < 2 || x > 23)) {
                    Bubble b = new Bubble(x, y);
                    b.setSelected(true);
                    bubbles.add(b);
                    bubbleRenderers.add(new BubbleRenderer(b, gfx, mousePosition));
                }
            }
        }
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame game) throws SlickException {
        Random rand = new Random();
        setExitRequested(false);
        titlePos = new Vector2f(gc.getWidth() / 2, -200);
        authorPos = new Vector2f(-400, gc.getHeight() / 2 - 46);
        greetingText = new WobbleTextRenderer("goodtimes.regular", 26, greetings[rand.nextInt(greetings.length)], 0.3f);
        greetingText.setHorizontalAlign(TextRenderer.ALIGN_CENTER);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics gfx) {
        for (BubbleRenderer renderer : bubbleRenderers) {
            renderer.render(-RenderSettings.BUBBLE_RADIUS / 2, -RenderSettings.BUBBLE_RADIUS / 2);
        }

        titleText.render((int) titlePos.x, (int) titlePos.y);
        authorText.render((int) authorPos.x, (int) authorPos.y);
        greetingText.render((int) authorPos.x, (int) authorPos.y + 40);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame game, int delta) {
        if (isExitRequested()) {
            game.enterState(Game.GAME);
        }

        for (BubbleRenderer renderer : bubbleRenderers) {
            renderer.update(gameContainer, delta);
        }

        updateFakeMouse(gameContainer);
        updateTexts(gameContainer);
    }

    @Override
    public void keyPressed(int key, char c) {
        // Any key to skip splash screen.
        setExitRequested(true);
    }

    /**
     * Move a fake mouse around the screen to affect the bubbles.
     *
     * @param gc game container
     */
    private void updateFakeMouse(GameContainer gc) {
        float w = gc.getWidth(), h = gc.getHeight();
        long t = gc.getTime();

        mousePosition.set(w / 2 + (float) Math.cos(t / 100.0) * w,
            h / 2 + (float) Math.sin(t / 100.0) * w);
    }

    /**
     * Animate texts smoothly.
     *
     * @param gc game container
     */
    private void updateTexts(GameContainer gc) {
        titlePos.y = curveValue(60, titlePos.y, 0.05f);
        if (authorPos.x > gc.getWidth() + 250) {
            setExitRequested(true);
        } else if (authorPos.x > titlePos.x - 5) {
            authorPos.x = curveValue(gc.getWidth() * 2.4f, authorPos.x, 0.04f);
        } else if (titlePos.y > 50) {
            authorPos.x = curveValue(titlePos.x, authorPos.x, 0.02f);
        }
    }
}
