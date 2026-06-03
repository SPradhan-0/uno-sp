package main.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;


public class Config {

    private static final String configFilePath = "config.ini";
    private static final Properties defaults = new Properties();
    private static final HashMap<String, String> values = new HashMap<>();
    private static final Properties p = new Properties();
    private static boolean initialized = false;

    private static void init() {
        if (initialized) return;
        initialized = true;

        defaults.put("playerCount", "2");
        defaults.put("title", "UNO");
        defaults.put("imgPath", "res/gfx/");
        defaults.put("soundPath", "res/sons/");
        defaults.put("drawDiscardOffset", "50");

        defaults.put("playMusic", "true");
        defaults.put("playSounds", "true");

        defaults.put("clickSound", "cardClicked.wav");
        defaults.put("invalidClickSound", "invalidCardClicked.wav");
        defaults.put("unoSound", "uno.wav");
        defaults.put("skipSound", "passe.wav");
        defaults.put("reverseSound", "reverse.wav");
        defaults.put("plus2Sound", "+2.wav");
        defaults.put("wildSound", "wild.wav");
        defaults.put("hardLuckSound", "hardLuck.wav");
        defaults.put("noPlayableCardsSound", "noPlayableCards.wav");
        defaults.put("plus4Sound", "+4.wav");
        defaults.put("winSound", "win.wav");
        defaults.put("bgMusic", "bgMusic.wav");

        
        defaults.put("windowWidth", "800");
        defaults.put("windowHeight", "600");
    }

    private static void readConfigFile() {
        try {
            init();
            InputStream is = new FileInputStream(configFilePath);
            p.load(is);
            is.close();
        } catch (IOException e) {
            Debug.err("File not found: " + configFilePath);
        }
    }

    public static void load() {
        readConfigFile();
        values.clear();

        Enumeration<?> e = p.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            values.put(key, p.getProperty(key));
        }

        e = defaults.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if (!values.containsKey(key)) {
                values.put(key, defaults.getProperty(key));
            }
        }

        try {
            OutputStream os = new FileOutputStream(configFilePath);
            p.store(os, null);
            os.close();
        } catch (IOException ex) {
            Debug.err("Unable to write config file: " + configFilePath);
        }
    }

    public static String get(String key) {
        if (values.containsKey(key)) return values.get(key);
        init();
        return defaults.getProperty(key);
    }

}
