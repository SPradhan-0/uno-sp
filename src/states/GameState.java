package main.states;

import javax.swing.UnsupportedLookAndFeelException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import main.GameRunnable;
import main.game.UnoGame;


public class GameState extends BasicGameState {
    
    public static int stateID;
    
    UnoGame               game = null;

    
    public GameState( int stateID ) {
        GameState.stateID = stateID;
    }

    
    @Override
    public void init( GameContainer container, StateBasedGame stateGame ) throws SlickException {
        try {
            game = new UnoGame(); 
        } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e ) {
            e.printStackTrace();
        }
        
        
        
        Thread thread = new Thread( new GameRunnable( game, stateGame ) );
        thread.start();
    }

    
    @Override
    public void update( GameContainer container, StateBasedGame stateGame, int delta ) throws SlickException {
        game.update( container ); 
    }

    
    @Override
    public void render( GameContainer container, StateBasedGame stateGame, Graphics g ) throws SlickException {
        game.render( g );
        
        int btnW = 120, btnH = 40;
        int x = container.getWidth() - btnW - 20;
        int y = container.getHeight() - btnH - 20;
        
        g.setColor( org.newdawn.slick.Color.darkGray );
        g.fillRect( x, y, btnW, btnH );

        
        boolean enabled = false;
        if ( game != null ) {
            enabled = game.canCurrentPlayerCallUno();
        }

        if ( enabled ) {
            g.setColor( org.newdawn.slick.Color.white );
        } else {
            g.setColor( org.newdawn.slick.Color.lightGray );
        }
        g.drawString( "UNO", x + btnW / 2 - g.getFont().getWidth( "UNO" ) / 2, y + btnH / 2 - g.getFont().getLineHeight() / 2 );

        
        if ( game != null && game.unoCalledByPlayer != null ) {
            int dotX = 20;
            int dotY = 10;
            for ( int i = 0; i < game.unoCalledByPlayer.length; i++ ) {
                boolean called = game.wasUnoCalled( i );
                g.setColor( called ? org.newdawn.slick.Color.green : org.newdawn.slick.Color.red );
                g.fillOval( dotX + i * 14, dotY, 10, 10 );
            }
        }
    }

    
    @Override
    public int getID() {
        return stateID;
    }
}
