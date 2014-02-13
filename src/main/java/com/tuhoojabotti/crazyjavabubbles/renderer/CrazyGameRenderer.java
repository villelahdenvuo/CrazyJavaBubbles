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

import static com.tuhoojabotti.crazyjavabubbles.Util.fatalError;
import com.tuhoojabotti.crazyjavabubbles.logic.Board;
import com.tuhoojabotti.crazyjavabubbles.logic.Bubble;
import com.tuhoojabotti.crazyjavabubbles.logic.CrazyGameLogic;
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

    private final Graphics graphics;
    private final BoardRenderer boardRenderer;
    private TextRenderer text;
    private final GameContainer gameContainer;
    private BubbleEffectRenderer particleRenderer;

    private final int windowWidth;
    private final int windowHeight;
    private final Color barColor = new Color(1f, 1f, 1f, 0.5f);

    /**
     * Create new {@link CrazyGame} renderer.
     *
     * @param board board of the game
     * @param gfx graphics controller
     * @param gc game container
     * @param mouse mouse position
     */
    public CrazyGameRenderer(Board board, Graphics gfx, GameContainer gc, Vector2f mouse) {
        windowWidth = gc.getWidth();
        windowHeight = gc.getHeight();
        gameContainer = gc;
        graphics = gfx;

        boardRenderer = new BoardRenderer(board, gfx, mouse);

        try {
            text = new TextRenderer("goodtimes.regular", 16);
        } catch (SlickException ex) {
            fatalError("Failed to load text.", this.getClass(), ex);
            gc.exit();
        }

        particleRenderer = new BubbleEffectRenderer();
        particleRenderer.initParticleSystem();
    }

    /**
     * Create an explosion on the screen with particles and physics.
     *
     * @param bubbles the bubbles that explode
     */
    public void explode(Set<Bubble> bubbles) {
        boardRenderer.explode(bubbles);

        if (RenderSettings.PARTICLE_EFFECTS) {
            for (Bubble bubble : bubbles) {
                particleRenderer.addExplosion(bubble);
            }
        }
    }

    /**
     * Render the game.
     *
     * @param game game logic
     */
    public void render(CrazyGameLogic game) {
        boardRenderer.render(RenderSettings.BOARD_MARGIN,
                RenderSettings.BOARD_MARGIN);

        graphics.setColor(barColor);
        graphics.fillRect(0, windowHeight - 28, windowWidth, 28);

        int textY = windowHeight - 22;
        text.render(windowWidth - 120, textY, "fps: " + gameContainer.getFPS());

        text.render(6, textY, "score: " + game.getScore());
        text.render(156, textY, "biggest: " + game.getBiggestCluster());
        text.render(306, textY, "total: " + game.getBubblesPopped());

        particleRenderer.render();
    }

    /**
     * Update the renderer physics, particles, etc.
     *
     * @param delta delta time
     */
    public void update(int delta) {
        boardRenderer.update(gameContainer, delta);
        particleRenderer.update(delta, gameContainer.getFPS());
    }
}
