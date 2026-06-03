package main.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import main.game.UnoGame;


public class GameOverState extends BasicGameState {
    
    public static int stateID;

    
    public GameOverState( int stateID ) {
        GameOverState.stateID = stateID;
    }

    
    @Override
    public void init( GameContainer arg0, StateBasedGame stateGame ) throws SlickException {
        
    }

    
    @Override
    public void update( GameContainer container, StateBasedGame stateGame, int delta ) throws SlickException {
        Input input = container.getInput(); 
        if ( input.isKeyPressed( Input.KEY_ESCAPE ) | input.isKeyPressed( Input.KEY_N ) ) { 
            System.exit( 0 ); 
        }
        if ( input.isKeyPressed( Input.KEY_ENTER ) | input.isKeyPressed( Input.KEY_O ) ) { 
            
            stateGame.getState( GameState.stateID ).init( container, stateGame );
            
            stateGame.enterState( GameState.stateID );
        }
    }

    
    @Override
    public void render( GameContainer container, StateBasedGame stateGame, Graphics g ) throws SlickException {
        g.setColor( Color.white );
    String message = String.format( "Game over, %s has won!", UnoGame.currentPlayer.name );
        
        int x = container.getWidth() / 2 - g.getFont().getWidth( message ) / 2, y = container.getHeight() / 2;
        g.drawString( message, x, y );
    message = "Play again? [Y/N]";
        x = container.getWidth() / 2 - g.getFont().getWidth( message ) / 2;
        g.drawString( message, x, y + 20 );
    }

    
    @Override
    public int getID() {
        return stateID;
    }
}
