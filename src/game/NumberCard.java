package main.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import main.gfx.Sprite;


public class NumberCard extends Card { //represents a card with a number (0-9) and a color (red, green, blue, yellow)

    
    private int value;

    
    public NumberCard( CardColor color, int value ) throws SlickException {
        super( color );
        this.value = value;
        image = Sprite.get( value, color );
    }

    public int getValue() {
        return value;
    }

    
    @Override
    public String toString() {
        return "(" + color.getDisplayName() + "," + value + ")";
    }

    
    @Override //a NumberCard can be played on top of another card if they have the same color or the same value
    public boolean isCompatibleWith( Card card ) {
        if ( card instanceof NumberCard ) { 
            
            return ( card.color == color ) || ( ( (NumberCard) card ).value == value ); 
        } else { 
            
            return card.color == color; 
        }
    }

    @Override
    public void update( GameContainer container ) throws SlickException {
        

    }

}
