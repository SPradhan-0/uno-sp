package main.game;

import java.util.Random;


public class CardPile extends Hand {

    
    protected Random rand = new Random();

    
    public Card pop() {
        return cards.remove( 0 );
    }

    
    public void push( Card card ) {
        cards.add( 0, card );
    }

    
    public void shuffle() {
        for ( int i = cards.size() - 1; i > 0; i-- ) {
            
            
            int pick = rand.nextInt( i ); 
            Card randCard = cards.get( pick );
            Card lastCard = cards.get( i );
            cards.set( i, randCard );
            cards.set( pick, lastCard );
        }
    }

    
    @Override
    public String toString() {
        return super.toString();
    }

    
    public Card top() {
        if ( cards.isEmpty() ) {
            return null;
        }
        return cards.get( 0 ); 
    }

}
