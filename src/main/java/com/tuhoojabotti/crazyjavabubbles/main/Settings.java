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
package com.tuhoojabotti.crazyjavabubbles.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.newdawn.slick.util.ResourceLoader;
import org.apache.commons.io.IOUtils;

/**
 * Full of constants for rendering the game.
 *
 * @author Ville Lahdenvuo <tuhoojabotti@gmail.com>
 */
public class Settings {

    private static HashMap<String, Object> settings;

    /**
     * Load settings from config.json.
     */
    public static void loadSettings() {
        settings = new HashMap<>();
        File file = new File("config.json");

        String txt = loadSettingsFile(file);

        // Failed to load settings file, try to reset defaults.
        if (txt == null) {
            txt = resetDefaultSettings(file);
        }

        if (txt != null) {
            parseSettings(txt);
        } else {
            Util.fatalError("Failed to load settings!", Settings.class, null);
        }
    }

    /**
     * Parse a JSON string containing settings.
     * @param str the JSON string
     */
    private static void parseSettings(String str) {
        try {
            JSONObject config = (JSONObject) JSONSerializer.toJSON(str);
            for (Iterator it = config.keySet().iterator(); it.hasNext();) {
                String key = (String) it.next();
                settings.put(key, config.get(key));
            }
        } catch (JSONException e) {
            Util.fatalError("Syntax error in config file, remove it to reset defaults.", Settings.class, e);
        }
    }

    /**
     * Load settings from a file.
     * @param file File where to load settings
     * @return JSON string or null if failed
     */
    private static String loadSettingsFile(File file) {
        StringBuilder txt = new StringBuilder();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                txt.append(line);
            }
        } catch (IOException e) {
            return null;
        }
        return txt.toString();
    }

    /**
     * Reset default settings and return them.
     * @param file where to save them
     * @return JSON string or null if failed
     */
    private static String resetDefaultSettings(File file) {
        try (FileWriter fw = new FileWriter(file, false)) {
            fw.write(IOUtils.toString(ResourceLoader.
                getResourceAsStream("config.default.json")));
        } catch (IOException e) {
            Util.fatalError("Failed to reset default settings!", Settings.class, e);
        }
        return loadSettingsFile(file);
    }

    public static Object get(String key) {
        return settings.get(key);
    }

    // A simple wrapper for boolean values.
    public static boolean is(String key) {
        return (boolean) settings.get(key);
    }

    public static void set(String key, Object value) {
        settings.put(key, value);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Below are the hard coded globals that can not be changed by the user. //
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Size of the {@link Bubble}s.
     */
    public static final int BUBBLE_RADIUS = 32;

    /**
     * How much do the bubbles move.
     */
    public static final float BUBBLE_WOBBLE = 0.04f;

    /**
     * Margin where to draw the {@link Board}.
     */
    public static final int BOARD_MARGIN = 15;

    /**
     * Width of the window.
     */
    public static final int SCREEN_WIDTH = 800;

    /**
     * Height of the window.
     */
    public static final int SCREEN_HEIGHT = 600;
}
