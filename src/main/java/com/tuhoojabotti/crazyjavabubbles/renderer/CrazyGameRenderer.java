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

import com.tuhoojabotti.crazyjavabubbles.logic.Board;
import com.tuhoojabotti.crazyjavabubbles.logic.Bubble;
import com.tuhoojabotti.crazyjavabubbles.logic.CrazyGameLogic;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * Renders the whole game.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class CrazyGameRenderer {

    private final Graphics gfx;
    private final BoardRenderer boardRenderer;
    private TextRenderer scoreText;
    private final GameContainer gameContainer;
    private int deltaTime;
    private ParticleSystem particleSystem;
    private ConfigurableEmitter explosion;

    /**
     * Create new {@link CrazyGame} renderer.
     *
     * @param board board of the game
     * @param gfx graphics controller
     * @param gc game container
     * @param mouse mouse position
     */
    public CrazyGameRenderer(Board board, Graphics gfx, GameContainer gc, Vector2f mouse) {
        this.gfx = gfx;
        gameContainer = gc;
        boardRenderer = new BoardRenderer(board, gfx, mouse);

        try {
            scoreText = new TextRenderer("goodtimes.regular", 16);
        } catch (SlickException ex) {
            Logger.getLogger(CrazyGameRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (RenderSettings.PARTICLE_EFFECTS) {
            initParticleSystem();
        }
    }

    public void explode(Set<Bubble> bubbles) {
        boardRenderer.explode(bubbles);
        if (RenderSettings.PARTICLE_EFFECTS) {
            int rp2 = RenderSettings.BUBBLE_RADIUS / 2;
            for (Bubble bubble : bubbles) {
                Vector2f point = bubble.getScreenPosition();
                ConfigurableEmitter e = explosion.duplicate();
                e.setPosition(point.x + rp2, point.y + rp2, false);
                e.addColorPoint(0f, bubble.getColor());
                e.addColorPoint(1f, bubble.getColor().darker(0.5f));
                e.setEnabled(true);
                particleSystem.addEmitter(e);
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

        gfx.setColor(new Color(1f, 1f, 1f, 0.5f));
        gfx.fillRect(0, gameContainer.getHeight() - 28, gameContainer.getWidth(), 28);

        int textY = gameContainer.getHeight() - 22;

        scoreText.render(gameContainer.getWidth() - 100, textY, "fps: " + gameContainer.getFPS());
        scoreText.render(gameContainer.getWidth() - 180, textY, "ms: " + deltaTime);

        scoreText.render(6, textY, "score: " + game.getScore());

        if (RenderSettings.PARTICLE_EFFECTS) {
            particleSystem.render();
        }
    }

    public void update(int delta) {
        deltaTime = delta;
        boardRenderer.update(gameContainer, delta);
        if (RenderSettings.PARTICLE_EFFECTS) {
            particleSystem.update(delta);
        }
    }

    private void initParticleSystem() {
        ImageBuffer ib = new ImageBuffer(4, 4);
        //ib.setRGBA(0, 0, 255, 255, 255, 255);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                ib.setRGBA(x, y, 255, 255, 255, 255);
            }
        }
        particleSystem = new ParticleSystem(new Image(ib), RenderSettings.MAX_PARTICLES);
        //particleSystem.getEmitter(0).setEnabled(false); // disable the initial emitter
        particleSystem.setRemoveCompletedEmitters(true); // remove emitters once they finish    

        try {
            explosion = ParticleIO.loadEmitter("effects/bubble_emitter.xml");
            explosion.setEnabled(false);
        } catch (IOException ex) {
            Logger.getLogger(CrazyGameRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
