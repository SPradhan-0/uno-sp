package main.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.Game;
import main.common.Config;
import main.gfx.Sprite;


public class DrawPile extends CardPile {
    
    

    
    public DrawPile() throws SlickException {
        for ( CardColor color : CardColor.values() ) {
            if ( color == CardColor.BLACK ) {
                for ( int i = 0; i < 4; i++ ) { 
                    add( new ActionCard( CardColor.BLACK, CardSymbol.WILD ) );
                    add( new ActionCard( CardColor.BLACK, CardSymbol.PLUS4 ) );
                }
                continue; 
            }
            
            add( new NumberCard( color, 0 ) );
            
            for ( int i = 1; i <= 9; i++ ) {
                add( new NumberCard( color, i ) );
                add( new NumberCard( color, i ) );
            }
            
            for ( int i = 0; i < 2; i++ ) {
                add( new ActionCard( color, CardSymbol.SKIP ) );
                add( new ActionCard( color, CardSymbol.REVERSE ) );
                add( new ActionCard( color, CardSymbol.PLUS2 ) );
            }
        }

        
    }

    
    private void retournerCarte( Card card ) {
        int i = rand.nextInt( cards.size() ); 
        cards.add( i, card );
    }

    
    public Card firstDiscardCard() {
        Card card;
        while ( true ) {
            card = pop(); 
            
            if ( card instanceof ActionCard ) { 
                
                retournerCarte( card );
                
                
            } else {
                return card;
            }
        }
    }

    
    public void render( Graphics g ) throws SlickException {
        
        Card topCard = this.top();
        if ( topCard == null ) {
            return; 
        }

        Image image = Sprite.getHiddenCard();
        image.setRotation( 0 );

        topCard.x = Game.WIDTH / 2 - Card.WIDTH / 2 + Integer.parseInt( Config.get( "drawDiscardOffset" ) );
        topCard.y = Game.HEIGHT / 2 - Card.HEIGHT / 2;
        topCard.angle = 0;
        topCard.updateBounds();

        g.drawImage( image, topCard.x, topCard.y );

        
        int offset = 20;
        String str = String.valueOf( this.cards.size() );
        g.drawString( str,
                
                Game.WIDTH / 2 + Integer.parseInt( Config.get( "drawDiscardOffset" ) )
                        - g.getFont().getWidth( str ) / 2,
                
                topCard.y - offset 
        
        );
    }

    public void update( GameContainer container ) throws SlickException {
        

        
        
        
        
        
        
        
        
        
        
        
    }

    
    
    
    
    
    

}
