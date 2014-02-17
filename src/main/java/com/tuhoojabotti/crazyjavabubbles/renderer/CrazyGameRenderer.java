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
package com.tuhoojabotti.crazyjavabubbles.renderer;

import com.tuhoojabotti.crazyjavabubbles.renderer.effect.BubbleEffectRenderer;
import com.tuhoojabotti.crazyjavabubbles.gui.TextRenderer;
import com.tuhoojabotti.crazyjavabubbles.gui.Settings;
import static com.tuhoojabotti.crazyjavabubbles.Util.fatalError;
import com.tuhoojabotti.crazyjavabubbles.gui.WobbleTextRenderer;
import com.tuhoojabotti.crazyjavabubbles.logic.Bubble;
import com.tuhoojabotti.crazyjavabubbles.logic.CrazyGameLogic;
import com.tuhoojabotti.crazyjavabubbles.renderer.effect.ScoreEffectRenderer;
import com.tuhoojabotti.crazyjavabubbles.states.CrazyGame;
import java.util.Set;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * Renders the whole game. Board, effects, score, everything.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class CrazyGameRenderer {

    private final CrazyGameLogic game;
    private final Graphics graphics;
    private final BoardRenderer boardRenderer;
    private TextRenderer text;
    private WobbleTextRenderer scoreText;
    private final GameContainer gameContainer;
    private BubbleEffectRenderer particleRenderer;
    private ScoreEffectRenderer scoreRenderer;
    private Vector2f mousePosition;

    private final int windowWidth;
    private final int windowHeight;
    private final Color barColor = new Color(1f, 1f, 1f, 0.5f);

    /**
     * Create new {@link CrazyGame} renderer.
     *
     * @param game the game logic to render
     * @param gfx graphics controller
     * @param gc game container
     * @param mouse mouse position
     */
    public CrazyGameRenderer(CrazyGameLogic game, Graphics gfx, GameContainer gc, Vector2f mouse) {
        this.game = game;
        windowWidth = gc.getWidth();
        windowHeight = gc.getHeight();
        gameContainer = gc;
        graphics = gfx;
        mousePosition = mouse;

        boardRenderer = new BoardRenderer(game.getBoard(), gfx, mouse);

        try {
            scoreText = new WobbleTextRenderer("goodtimes.regular", 16, "score:", 0.15f);
            text = new TextRenderer("goodtimes.regular", 16);
        } catch (SlickException e) {
            fatalError("Failed to load text.", this.getClass(), e);
        }

        particleRenderer = new BubbleEffectRenderer();
        particleRenderer.initParticleSystem();
        scoreRenderer = new ScoreEffectRenderer();
    }

    /**
     * Create an explosion on the screen with particles and physics.
     *
     * @param bubbles the bubbles that explode
     */
    public void explode(Set<Bubble> bubbles) {
        boardRenderer.explode(bubbles);

        if (Settings.is("particle_effects")) {
            for (Bubble bubble : bubbles) {
                particleRenderer.addExplosion(bubble);
            }
        }
        if (Settings.is("score_effects")) {
            for (Bubble bubble : bubbles) {
                scoreRenderer.applyForce(bubble.getScreenPosition());
            }
            scoreRenderer.addScoreEffect(game.calculateScore(bubbles.size()), mousePosition);
        }
    }

    /**
     * Render the game.
     */
    public void render() {
        boardRenderer.render(Settings.BOARD_MARGIN,
            Settings.BOARD_MARGIN);

        scoreRenderer.render();

        graphics.setColor(barColor);
        graphics.fillRect(0, windowHeight - 28, windowWidth, 28);

        int textY = windowHeight - 22;
        text.render(windowWidth - 120, textY, "fps: " + gameContainer.getFPS());

        scoreText.render(6, textY + 2);
        text.render(84, textY, "" + game.getScore());
        text.render(206, textY, "biggest: " + game.getBiggestCluster());
        text.render(356, textY, "time: " + game.getTime());

        particleRenderer.render();
    }

    /**
     * Update the renderer physics, particles, etc.
     *
     * @param delta delta time
     */
    public void update(int delta) {
        boardRenderer.update(gameContainer, delta);
        particleRenderer.update(delta);
        scoreRenderer.update(gameContainer, delta);
    }
}
