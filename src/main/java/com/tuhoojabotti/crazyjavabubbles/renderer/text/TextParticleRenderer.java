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
package com.tuhoojabotti.crazyjavabubbles.renderer.text;

import com.tuhoojabotti.crazyjavabubbles.main.Settings;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

/**
 * A text particle effect renderer
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class TextParticleRenderer {

    /**
     * A text particle a.k.a. a character.
     */
    private class TextParticle extends Vector2f {

        /**
         * The character that this particle represents
         */
        private String character;
        /**
         * Particle's velocity
         */
        private Vector2f velocity;
        /**
         * Gravitational pull
         */
        private final Vector2f gravity = new Vector2f(0, 0.0981f);

        ;

        /**
         * Create a new particle.
         * @param pos position of it
         * @param vel starting velocity
         * @param c the character
         */
        public TextParticle(Vector2f pos, Vector2f vel, String c) {
            super(pos);
            velocity = vel;
            character = c;
        }

        /**
         * Render the particle.
         *
         * @param font the font to use
         */
        public void render(TextRenderer font) {
            // Draw shadow for the particle.
            font.render((int) x - 1, (int) y - 1, character, Color.black);
            font.render((int) x, (int) y, character);
        }

        /**
         * Update the particle's location. The particle dies when it drops below
         * the screen border.
         *
         * @param gc game container
         * @param delta delta time
         * @return whether the particle is dead or not
         */
        public boolean update(GameContainer gc, int delta) {
            velocity.add(gravity);
            this.add(velocity);
            return y > gc.getHeight();
        }

        /**
         * Apply force to the particle.
         *
         * @param point point of origin
         */
        private void applyForce(Vector2f point) {
            double angle = Math.atan2(point.y - this.y,
                point.x - this.x);
            float distance = Math.max(30, point.distance(this));

            velocity.x -= 50 / distance * (float) Math.cos(angle);
            velocity.y -= 100 / distance * (float) Math.sin(angle);
        }
    }

    /**
     * The font to render the effect with.
     */
    private TextRenderer font;
    /**
     * The list of particles.
     */
    private Set<TextParticle> particles;
    /**
     * A random generator.
     */
    private Random rand;

    /**
     * Create a new text particle renderer.
     */
    public TextParticleRenderer() {
        rand = new Random();
        font = new TextRenderer("goodtimes.regular", 20);
        particles = new HashSet<>();
    }

    /**
     * Add an effect.
     *
     * @param text the text to break into particles
     * @param pos where to start it
     */
    public void addParticleEffect(String text, Vector2f pos) {
        float mul = rand.nextFloat() * 3;
        for (int i = 0; i < text.length(); i++) {
            String c = "" + text.charAt(i);

            pos.add(new Vector2f(font.font.getWidth(c), 0));
            // Add some randomness to the velocity.
            Vector2f vel = new Vector2f(rand.nextFloat() * 0.2f - 0.1f,
                -4 - rand.nextFloat() * 0.5f - mul);

            particles.add(new TextParticle(pos, vel, c));
        }
    }

    /**
     * Apply force to all particles.
     *
     * @param point point of origin
     */
    public void applyForce(Vector2f point) {
        for (TextParticle scoreParticle : particles) {
            scoreParticle.applyForce(point);
        }
    }

    /**
     * Render the effects unless disabled in settings.
     */
    public void render() {
        if (!Settings.is("score_effects")) {
            return;
        }
        for (TextParticle scoreParticle : particles) {
            scoreParticle.render(font);
        }
    }

    /**
     * Update the effects unless disabled in settings.
     *
     * @param gc game container
     * @param delta delta time
     */
    public void update(GameContainer gc, int delta) {
        if (!Settings.is("score_effects")) {
            return;
        }
        HashSet<TextParticle> dead = new HashSet<>();
        for (TextParticle scoreParticle : particles) {
            if (scoreParticle.update(gc, delta)) {
                dead.add(scoreParticle);
            }
        }
        particles.removeAll(dead);
    }
}
