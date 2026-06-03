package main.io;

import java.util.HashMap;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import main.common.Config;

public class Audio {

    private static Music                  music;
    private static HashMap<String, Sound> sounds = new HashMap<>();

    public static void load() throws SlickException {
        if ( soundEnabled ) {
            String soundNames[] = { "clickSound", "invalidClickSound", "unoSound", "winSound" };
            for ( String soundName : soundNames ) {
                sounds.put( soundName, new Sound( Config.get( "soundPath" ) + Config.get( soundName ) ) );
            }
            music = new Music( Config.get( "soundPath" ) + Config.get( "bgMusic" ) );
        }
    }

    public static boolean musicEnabled = true;
    public static boolean soundEnabled = true;

    public static void playSound( String soundName ) throws SlickException {
        if ( soundEnabled ) {
            Sound s = sounds.get( soundName );
            if ( s != null )
                s.play();
        }
    }

    public static void playMusic() throws SlickException {
        if ( musicEnabled && music != null ) {
            music.loop();
        }
    }

    public static void stopMusic() throws SlickException {
        if ( musicEnabled && music != null ) {
            music.stop();
        }
    }

}
