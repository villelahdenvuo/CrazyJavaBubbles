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

import com.tuhoojabotti.crazyjavabubbles.gui.RenderSettings;
import com.tuhoojabotti.crazyjavabubbles.gui.TextRenderer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class ScoreEffectRenderer {

    private class ScoreParticle extends Vector2f {

        private String number;
        private Vector2f velocity;
        private final Vector2f gravity;

        public ScoreParticle(Vector2f pos, Vector2f vel, String c) {
            super(pos);
            velocity = vel;
            gravity = new Vector2f(0, 0.0981f);
            number = c;
        }

        public void render(TextRenderer font) {
            font.render((int) x - 1, (int) y - 1, number, Color.black);
            font.render((int) x, (int) y, number);
        }

        public boolean update(GameContainer gc, int delta) {
            velocity.add(gravity);
            this.add(velocity);
            return y > gc.getHeight() + 10;
        }

        private void applyForce(Vector2f point) {
            double angle = Math.atan2(point.y - this.y,
                point.x - this.x);
            float distance = Math.max(50, point.distance(this));

            velocity.x -= 40 / distance * (float) Math.cos(angle);
            velocity.y -= 60 / distance * (float) Math.sin(angle);
        }
    }

    private TextRenderer font;
    private ArrayList<ScoreParticle> particles;
    private Random rand;

    public ScoreEffectRenderer() {
        rand = new Random();
        try {
            font = new TextRenderer("goodtimes.regular", 20);
        } catch (SlickException ex) {
            Logger.getLogger(ScoreEffectRenderer.class.getName())
                .log(Level.WARNING, "Failed to load font.", ex);
        }
        particles = new ArrayList<>();
    }

    public void addScoreEffect(int score, Vector2f pos) {
        String text = "" + score;
        for (int i = 0; i < text.length(); i++) {
            String c = "" + text.charAt(i);
            
            pos.add(new Vector2f(font.getFont().getWidth(c), 0));
            Vector2f vel = new Vector2f(rand.nextFloat() * 0.2f - 0.1f, -5);
            
            particles.add(new ScoreParticle(pos, vel, c));
        }
    }

    public void applyForce(Vector2f point) {
        for (ScoreParticle scoreParticle : particles) {
            scoreParticle.applyForce(point);
        }
    }

    public void render() {
        for (ScoreParticle scoreParticle : particles) {
            scoreParticle.render(font);
        }
    }

    public void update(GameContainer gc, int delta) {
        HashSet<ScoreParticle> dead = new HashSet<>();
        for (ScoreParticle scoreParticle : particles) {
            if (scoreParticle.update(gc, delta)) {
                dead.add(scoreParticle);
            }
        }
        particles.removeAll(dead);
    }

}
