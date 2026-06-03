package main.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


public class MenuState extends BasicGameState {
    
    private String[]  menuOptions = new String[] {
        "Play",
        "Quit"
    };
    
    private int       index       = 0;
    
    public static int stateID;

    
    public MenuState( int stateID ) {
        MenuState.stateID = stateID;
    }

    
    @Override
    public void init( GameContainer arg0, StateBasedGame stateGame ) throws SlickException {
        
    }

    @Override
    
    public void update( GameContainer container, StateBasedGame stateGame, int delta ) throws SlickException {
        Input input = container.getInput(); 
        if ( input.isKeyPressed( Input.KEY_UP ) ) { 
            index--;
            if ( index == -1 ) { 
                index = menuOptions.length - 1; 
            }
        } else if ( input.isKeyPressed( Input.KEY_DOWN ) ) { 
            index++;
            if ( index == menuOptions.length ) { 
                index = 0; 
            }
        } else if ( input.isKeyPressed( Input.KEY_ENTER ) ) { 
            if ( index == 0 ) { 
                
                stateGame.enterState( GameState.stateID, new FadeOutTransition(), new FadeInTransition() );
            } else if ( index == 1 ) { 
                
                System.exit( 0 );
            }
        }
    }

    
    @Override
    public void render( GameContainer container, StateBasedGame stateGame, Graphics g ) throws SlickException {
        for ( int i = 0; i < menuOptions.length; ++i ) { 
            if ( i == index ) { 
                g.setColor( Color.red ); 
            } else { 
                g.setColor( Color.white ); 
            }
            int step = container.getHeight() / ( menuOptions.length + 1 ); 
            
            
            g.drawString(
                    menuOptions[i],
                    container.getWidth() / 2 - g.getFont().getWidth( menuOptions[i] ) / 2,
                    step * ( i + 1 ) );
        }
    }

    
    @Override
    public int getID() {
        return stateID;
    }
}
