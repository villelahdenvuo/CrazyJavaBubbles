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

import static com.tuhoojabotti.crazyjavabubbles.Util.curveValue;
import static com.tuhoojabotti.crazyjavabubbles.Util.fatalError;
import com.tuhoojabotti.crazyjavabubbles.gui.TextRenderer;
import com.tuhoojabotti.crazyjavabubbles.gui.WobbleTextRenderer;
import com.tuhoojabotti.crazyjavabubbles.logic.CrazyGameLogic;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class GameOverRenderer extends Vector2f {

    private GameContainer gameContainer;
    private Graphics graphics;

    private WobbleTextRenderer titleText;
    private TextRenderer scoreText;
    private CrazyGameLogic logic;
    private boolean canSkip;
    private final Color bgColor = new Color(1f, 1f, 1f, 0.5f);

    public GameOverRenderer(GameContainer gc, Graphics gfx, CrazyGameLogic game) {
        super(gc.getWidth() / 2, -gc.getHeight());
        logic = game;
        gameContainer = gc;
        graphics = gfx;
        try {
            titleText = new WobbleTextRenderer("sweet-as-candy.regular", 34, "Game Over!", 0.4f);
            titleText.setHorizontalAlign(TextRenderer.ALIGN_CENTER);
            scoreText = new TextRenderer("goodtimes.regular", 16);
        } catch (SlickException e) {
            fatalError("Failed to load text.", this.getClass(), e);
        }
    }

    public void render() {
        int w = gameContainer.getWidth();
        graphics.setColor(bgColor);
        graphics.fillRoundRect(x - w / 4, y, w / 2, gameContainer.getHeight() / 2, 20, 30);

        titleText.render((int) x, (int) y + 35);

        int textX = (int) x - w / 4 + 20, textY = 100;

        scoreText.render(textX, (int) y + textY, "score: " + logic.getScore());
        scoreText.render(textX, (int) y + textY + 20, " + cluster bonus: " + logic.getBiggestClusterBonus());
        scoreText.render(textX, (int) y + textY + 40, "  -  time penalty: " + logic.getTimeBonus());

        scoreText.render(textX, (int) y + textY + 80, "total score: " + logic.getTotalScore());
        scoreText.render(textX, (int) y + textY + 120, "bubbles popped: "
            + String.format("%1$.2f", logic.getBubblesPopped() / (double) (24 * 17) * 100) + " %");

        scoreText.render(textX, (int) y + textY + 160, "Click for a new game :)");

    }

    public void update(int delta) {
        y = curveValue(120, y, 0.03f);
        canSkip = y > 100;
    }

    public boolean canSkip() {
        return canSkip;
    }
}
