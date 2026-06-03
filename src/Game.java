package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import main.common.Config;
import main.common.Debug;
import main.io.Audio;
import main.states.GameOverState;
import main.states.GameState;

public class Game extends StateBasedGame {
    public static int    WIDTH, HEIGHT;
    public static String TITLE;

    public Game( String title ) {
        super( title );
    }

    public static void main( String[] args ) throws SlickException {
        Config.load();

        WIDTH = Integer.parseInt( Config.get( "windowWidth" ) );
        HEIGHT = Integer.parseInt( Config.get( "windowHeight" ) );
        Audio.musicEnabled = Boolean.parseBoolean( Config.get( "playMusic" ) );
        Audio.soundEnabled = Boolean.parseBoolean( Config.get( "playSounds" ) );

        Audio.load();

        TITLE = Config.get( "title" );
        AppIcon.applyToTaskbar();

        AppGameContainer app = new AppGameContainer( new Game( TITLE ) );
        app.setIcons( new String[] { AppIcon.PATH } );
        app.setDisplayMode( WIDTH, HEIGHT, false );
        Debug.log( "WIDTH = " + WIDTH + ", HEIGHT = " + HEIGHT );
        app.setShowFPS( false );
        app.setTargetFrameRate( 60 );
        app.start();
    }

    @Override
    public void initStatesList( GameContainer container ) throws SlickException {
        this.addState( new GameState( 1 ) );
        this.addState( new GameOverState( 2 ) );
    }

}
