package main.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.Game;
import main.common.Config;


public class DiscardPile extends CardPile {

    
    public DiscardPile( DrawPile drawPile ) {
        ajouterPremiereCarte( drawPile );
    }

    
    private void ajouterPremiereCarte( DrawPile drawPile ) {
        push( drawPile.firstDiscardCard() );
        
    }

    
    @Override
    public String toString() {
        String str = "";
        if ( cards.isEmpty() ) {
            str = "[EMPTY]";
        }
        for ( int i = 0; i < cards.size(); ++i ) {
            Card card = cards.get( i );
            str = str + i + ") " + card.toString();
            if ( i != cards.size() - 1 ) { 
                str = str + "\n"; 
            }
        }
        return str;
    }

    
    public void show() {
        System.out.println( this );
    }

    
    public void showTopCard() {
    System.out.println( "Top of discard pile: " + top().toString() );
    }

    
    public void render( Graphics g ) throws SlickException {
        Card topCard = this.top();

        Image image = this.top().image;
        image.setRotation( 0 );

        topCard.x = Game.WIDTH / 2 - Card.WIDTH / 2 - Integer.parseInt( Config.get( "drawDiscardOffset" ) );
        topCard.y = Game.HEIGHT / 2 - Card.HEIGHT / 2;
        topCard.angle = 0;
        topCard.updateBounds();

        g.drawImage( image, topCard.x, topCard.y );

        
        int offset = 20;
        String str = String.valueOf( this.cards.size() );
        g.drawString( str,
                
                Game.WIDTH / 2 - Integer.parseInt( Config.get( "drawDiscardOffset" ) )
                        - g.getFont().getWidth( str ) / 2,
                
                topCard.y - offset 
        
        );
    }

    
    
    
    
    
    
    
    
    
    
    
    
    

}
