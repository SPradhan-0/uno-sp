package main.game;

import java.util.ArrayList;


public class Hand {

    
    protected ArrayList<Card> cards;

    
    public Hand() {
        cards = new ArrayList<Card>();
    }

    
    public void add( Card card ) {
        cards.add( card );
    }

    
    public Card remove( int num ) {
        return cards.remove( num );
    }

    

    
    public ArrayList<Card> getCards() {
        return cards;
    }

    
    public int cardCount() {
        return cards.size();
    }

    @Override
    public String toString() {
        String str = "";
        for ( Card card : cards ) {
            str = str + card.toString();
        }
        return str;
    }

    
    public void remove( Card playedCard ) {
        cards.remove( playedCard );
    }

}
