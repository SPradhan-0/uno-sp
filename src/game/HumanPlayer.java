package main.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import main.common.Debug;
import main.io.Audio;


public class HumanPlayer extends Player { //extends player

    public HumanPlayer( String name, DrawPile drawPile, DiscardPile discardPile ) {
        super( name, drawPile, discardPile );
    }

    
    public String getName() { //getter human player specific
        return name;
    }

    
    @Override
    public String toString() {
        return "[HumanPlayer] : " + getName();
    }

    @Override
    public void render( Graphics g ) throws SlickException {
        
        super.render( g );
    }

    
    private CardColor chooseColor() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException, InterruptedException, SlickException {
        
        UnoGame.waitForDialogCountDownLatch = new CountDownLatch( 1 ); 

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        

        UnoGame.dialog.selectedColor = null; 
        UnoGame.dialog.setVisible( true ); 

        
        Debug.log( "Waiting for dialog..." );
        UnoGame.waitForDialogCountDownLatch.await(); 

        

        Debug.log( "Dialog is done..." );

        
        
        
        
        
        

        Debug.log( "color chosen : " + UnoGame.dialog.selectedColor );
        
        CardColor color = CardColor.fromDisplayName( UnoGame.dialog.selectedColor );
        return color;
    }

    
    @Override
    public void playCard() throws InterruptedException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException, SlickException {
        
        
        

    System.out.println( "You have " + playableCardCount() + " playable cards" );

        
        Debug.log( "Waiting for a click ..." );

        UnoGame.countDownLatch = new CountDownLatch( 1 ); 
        UnoGame.countDownLatch.await(); 
        
        
        
        
        

        Debug.log( "Click received ! ..." );
        
        

        
        
        
        
        
        
        
        
        

        
        

        
        
        
        
        if ( playedCard instanceof NumberCard ) {
            playMatchingNumberCards( (NumberCard) playedCard );
            return;
        }
        
        if ( hand.cardCount() > 1 && playedCard.color == CardColor.BLACK ) { 
            
            System.out.println( "You must choose a color" );
            CardColor color = chooseColor();
            
            ( (ActionCard) playedCard ).setColor( color );
            
            
            
            if ( ( (ActionCard) playedCard ).symbol == CardSymbol.WILD ) {
                Audio.playSound( "wildSound" );
            }
        }
        hand.remove( playedCard ); 
        discardPile.push( playedCard ); 
        System.out.println( name + " played " + playedCard );
    }

    private void playMatchingNumberCards( NumberCard clickedCard ) {
        ArrayList<NumberCard> matchingNumberCards = getMatchingNumberCards( clickedCard );
        NumberCard topCard = clickedCard;

        if ( matchingNumberCards.size() > 1 ) {
            CardColor topColor = chooseTopColor( matchingNumberCards );
            for ( NumberCard card : matchingNumberCards ) {
                if ( card.color == topColor ) {
                    topCard = card;
                    break;
                }
            }
        }

        for ( NumberCard card : matchingNumberCards ) {
            if ( card != topCard ) {
                hand.remove( card );
                discardPile.push( card );
                System.out.println( name + " played " + card );
            }
        }

        hand.remove( topCard );
        discardPile.push( topCard );
        playedCard = topCard;
        System.out.println( name + " played " + topCard + " on top" );
    }

    private ArrayList<NumberCard> getMatchingNumberCards( NumberCard referenceCard ) {
        ArrayList<NumberCard> matchingNumberCards = new ArrayList<NumberCard>();
        for ( Card card : hand.cards ) {
            if ( card instanceof NumberCard ) {
                NumberCard numberCard = (NumberCard) card;
                if ( numberCard.getValue() == referenceCard.getValue() ) {
                    matchingNumberCards.add( numberCard );
                }
            }
        }
        return matchingNumberCards;
    }

    private CardColor chooseTopColor( ArrayList<NumberCard> matchingNumberCards ) {
        ArrayList<CardColor> colors = new ArrayList<CardColor>();
        for ( NumberCard card : matchingNumberCards ) {
            if ( !colors.contains( card.color ) ) {
                colors.add( card.color );
            }
        }

        if ( colors.size() == 1 ) {
            return colors.get( 0 );
        }

        String[] options = new String[colors.size()];
        for ( int i = 0; i < colors.size(); i++ ) {
            options[i] = colors.get( i ).getDisplayName();
        }

        Object choice = JOptionPane.showInputDialog( null, "Choose the color to leave on top", "Stack same number",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0] );
        if ( choice == null ) {
            return colors.get( 0 );
        }
        return CardColor.fromDisplayName( choice.toString() );
    }

    @Override
    public void update( GameContainer container ) throws SlickException {
        
        
        
        if ( id != UnoGame.currentPlayerIndex ) {
            return; 
        }
        
        if ( UnoGame.input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) { 
            Debug.log( "update() / id = " + this.id );
            int mx = UnoGame.input.getMouseX(), my = UnoGame.input.getMouseY();
            for ( int i = hand.cards.size() - 1; i >= 0; --i ) {
                Card card = hand.cards.get( i );
                if ( card.isClicked( new Point( mx, my ) ) ) { 
                    if ( card.playable ) { 
                        
                        System.out.println( "card clicked !" );
                        
                        Audio.playSound( "clickSound" );
                        this.playedCard = card; 

                        Debug.log( "Waiting for a card click..." );
                        UnoGame.countDownLatch.countDown(); 
                        Debug.log( "Card click detected!" );
                        

                        

                        
                        
                        
                        
                        
                        
                        System.out.println( card );
                        break; 
                    } else {
                        Debug.log( "This card is not playable !" );
                        Audio.playSound( "invalidClickSound" );
                        break; 
                    }
                }
            }
            Debug.log( "==================================================" );
        }
    }

}
