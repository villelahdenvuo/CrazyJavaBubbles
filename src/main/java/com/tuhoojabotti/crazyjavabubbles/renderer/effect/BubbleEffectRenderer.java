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
package com.tuhoojabotti.crazyjavabubbles.renderer.effect;

import com.tuhoojabotti.crazyjavabubbles.main.Settings;
import com.tuhoojabotti.crazyjavabubbles.logic.Bubble;
import java.io.IOException;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class BubbleEffectRenderer {

    private ParticleSystem particleSystem;
    private ConfigurableEmitter explosion;

    /**
     * Create an explosion particle effect.
     *
     * @param bubble where to create it and what color
     */
    public void addExplosion(Bubble bubble) {
        int offset = Settings.BUBBLE_RADIUS / 2;
        Vector2f point = bubble.getScreenPosition();
        ConfigurableEmitter e = explosion.duplicate();
        e.setPosition(point.x + offset, point.y + offset, false);
        e.addColorPoint(0f, bubble.getColor());
        e.addColorPoint(1f, bubble.getColor());
        e.setEnabled(true);
        particleSystem.addEmitter(e);
    }

    /**
     * Create a particle system for the renderer.
     */
    public void initParticleSystem() {
        particleSystem = new ParticleSystem(createParticle());
        particleSystem.setRemoveCompletedEmitters(true);

        try {
            explosion = ParticleIO.loadEmitter("effects/bubble_emitter.xml");
            explosion.setEnabled(false);
        } catch (IOException e) {
            // Do not try to render effects.
            Settings.set("particle_effects", false);
        }
    }

    /**
     * Render the particle effect.
     */
    public void render() {
        if (Settings.is("particle_effects")) {
            particleSystem.render();
        }
    }

    /**
     * Update the particle effect.
     *
     * @param delta delta time
     */
    public void update(int delta) {
        if (Settings.is("particle_effects")) {
            particleSystem.update(delta);
        }
    }

    /**
     * Create 4x4 white image for particles.
     *
     * @return 4x4 white image
     */
    private Image createParticle() {
        ImageBuffer ib = new ImageBuffer(4, 4);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                ib.setRGBA(x, y, 255, 255, 255, 255);
            }
        }
        return new Image(ib);
    }
}
