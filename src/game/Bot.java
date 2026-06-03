package main.game;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import main.common.Debug;
import main.io.Audio;


public class Bot extends Player { //extends player

    Random random = new Random(); 

    public Bot( String name, DrawPile drawPile, DiscardPile discardPile ) {
        super( name, drawPile, discardPile );
        
    }

    @Override
    public void render( Graphics g ) throws SlickException {
        super.render( g );
    }

    @Override
    public void update( GameContainer container ) throws SlickException {
        
    }

    @Override // bot specific to auto select a card
    public void playCard() throws SlickException {
        
        System.out.println( "The bot has " + playableCardCount() + " playable cards" );
        Debug.log( "Waiting for the bot's move..." );
        try {
            Thread.sleep( 2000 ); 
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        Card card = null;
        int remainingAttempts = 100;
        int cardIndex = 0;
        do {
            if ( remainingAttempts > 0 ) {
                remainingAttempts--;
                cardIndex = random.nextInt( hand.cards.size() );
            } else {
                cardIndex = ( cardIndex + 1 ) % hand.cards.size();
            }
            card = hand.cards.get( cardIndex );
            if ( !card.playable ) {
                Debug.log( "card " + card + " is not playable" );
            }
        } while ( !card.playable );
        
        Audio.playSound( "clickSound" );
        this.playedCard = card; 

        if ( playedCard.color == CardColor.BLACK ) {
            
            Debug.log( "Bot must choose a color..." );
            CardColor color = chooseColor();
            
            ( (ActionCard) playedCard ).setColor( color );
            
            
            
            
        }

        hand.remove( playedCard ); 
        discardPile.push( playedCard ); 
        Debug.log( name + " played " + playedCard );
    }

    
    @Override
    public String toString() {
        return "[Bot] : " + name;
    }

    private CardColor chooseColor() {
        Debug.log( "Waiting for bot's choice of color..." );
        String[] colorNames = { "Yellow", "Green", "Blue", "Red" };
        String selectedColor = null;
        try {
            Thread.sleep( 2000 ); 
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        selectedColor = colorNames[random.nextInt( colorNames.length )];
        Debug.log( "Bot selected the color : " + selectedColor );
        return CardColor.fromDisplayName( selectedColor );
    }

}
