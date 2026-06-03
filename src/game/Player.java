package main.game;

import java.util.ArrayList;

import javax.swing.UnsupportedLookAndFeelException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.Game;
import main.gfx.Sprite;
import main.io.Audio;


public abstract class Player extends GameObject {
 //player is an abstract supercalass for HumanPlayer and AIPlayer, it has a hand of cards, a name, a draw pile and a discard pile to interact with, and a seat position at the table (top, bottom, left, right)
    
    public Hand     hand;
    
    public String   name;
    
    public DrawPile   drawPile;
    
    public DiscardPile    discardPile;
    
    public SeatPosition position;
    
    public int      id;
    
    public Card    playedCard;

    
    public Player( String name, DrawPile drawPile, DiscardPile discardPile ) {
        this.name = name;
        this.drawPile = drawPile;
        this.discardPile = discardPile;
        hand = new Hand();
    }

    
    public Card drawCard() { //shared logic for all players
    if ( drawPile.cardCount() == 0 ) { 
            
            System.out.println( "The draw pile is empty!" );
            
            Card discardTopCard = discardPile.pop();
            while ( discardPile.cardCount() != 0 ) { 
                drawPile.push( discardPile.pop() );
            }
            drawPile.shuffle();
            discardPile.push( discardTopCard ); 
        }
        
        Card card = drawPile.pop();
        hand.add( card );
        return card;
    }

    
    public void showHand( Graphics g ) throws SlickException {
        ArrayList<Card> cards = hand.getCards();
        int middleCardIndex = cards.size() / 2;

        
        

        float angleCoefficient;
        Card card;

        float initialX = PlayerHandPainter.getStartPosX( position ), initialY = PlayerHandPainter.getStartPosY( position );
        
        float angle = 10;
        float offset = 30;
        if ( cards.size() < 10 ) {
            angle = 10;
            offset = 30;
        } else {
            angle = 5;
            offset = 20;
        }

        
        Image hiddenCardImg = Sprite.getHiddenCard();

        if ( cards.size() % 2 == 0 ) { 

            int x = middleCardIndex - 1;
            angleCoefficient = -( 1 / 2 + x );
            

            
            for ( int i = 0; i < cards.size(); i++ ) {
                
                

                card = cards.get( i ); 
                if ( card == null ) {
                    continue;
                } 
                if ( PlayerHandPainter.getOffsetAxis( position ) == 'x' ) {
                    card.x = initialX + angleCoefficient * offset;
                    card.y = initialY;
                } else { 
                    card.x = initialX;
                    card.y = initialY + angleCoefficient * offset;
                }

                card.playable = card.isCompatibleWith( discardPile.top() );

                if ( this instanceof HumanPlayer ) {
                    

                    card.rotate( PlayerHandPainter.getAngleCorrection( position ) * angleCoefficient * angle
                            + PlayerHandPainter.getAdditionalRotation( position ) );
                    card.render( g );

                } else {

                    hiddenCardImg.setRotation( PlayerHandPainter.getAngleCorrection( position ) * angleCoefficient * angle
                            + PlayerHandPainter.getAdditionalRotation( position ) );
                    hiddenCardImg.draw( card.x, card.y );

                }

                x--;
                angleCoefficient = -( 1 / 2 + x );
            }

        } else { 

            angleCoefficient = -middleCardIndex;

            
            for ( int i = 0; i < cards.size(); i++ ) {

                card = cards.get( i );
                if ( PlayerHandPainter.getOffsetAxis( position ) == 'x' ) {
                    card.x = initialX + angleCoefficient * offset;
                    card.y = initialY;
                } else { 
                    card.x = initialX;
                    card.y = initialY + angleCoefficient * offset;
                }

                if ( this.id == UnoGame.currentPlayerIndex ) {
                    card.playable = card.isCompatibleWith( discardPile.top() );
                } else {
                    card.playable = false;
                }

                if ( this instanceof HumanPlayer ) {
                    card.rotate( PlayerHandPainter.getAngleCorrection( position ) * angleCoefficient * angle
                            + PlayerHandPainter.getAdditionalRotation( position ) );
                    card.render( g );
                } else {
                    hiddenCardImg.setRotation( PlayerHandPainter.getAngleCorrection( position ) * angleCoefficient * angle
                            + PlayerHandPainter.getAdditionalRotation( position ) );
                    hiddenCardImg.draw( card.x, card.y );
                }

                
                

                
                
                
                
                

                
                
                
                
                
                
                

                
                

                
                

                angleCoefficient++;
            }

            

        }
    }

    
    public int cardCount() {
        return hand.cardCount();
    }

    
    public void showHand() {
        String str = "";
        ArrayList<Card> cards = hand.getCards();
        if ( cards.isEmpty() ) {
            str = "[EMPTY]";
        }
        for ( int i = 0; i < cards.size(); ++i ) {
            Card card = cards.get( i );
            str = str + i + ") " + card.toString();
            if ( card.isCompatibleWith( discardPile.top() ) ) {
                str = str + " [Playable]";
            }
            if ( i != cards.size() - 1 ) { 
                str = str + "\n"; 
            }
        }
        System.out.println( str );
    }

    
    protected int playableCardCount() {
        int n = 0;
        ArrayList<Card> cards = hand.getCards();
        if ( cards.isEmpty() ) {
            return 0;
        }
        Card discardTopCard = discardPile.top();
        for ( int i = 0; i < cards.size(); ++i ) {
            Card card = cards.get( i );
            if ( card.isCompatibleWith( discardTopCard ) ) {
                n++;
            }
        }
        return n;
    }

    
    private void pause() {
        try {
            Thread.sleep( 3000 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    
    public void playTurn() throws InterruptedException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException, SlickException { 
        playedCard = null;
        discardPile.showTopCard();
        showHand(); 
        
        ArrayList<Card> cards = hand.getCards();
        ArrayList<Card> playable = new ArrayList<>();
        Card top = discardPile.top();
        for ( Card card : cards ) {
            if ( card.isCompatibleWith( top ) ) {
                playable.add( card );
            }
        }

        
        if ( playable.isEmpty() ) {
            System.out.println( "You have no playable cards! you must draw!" );
            pause();
            Card called = drawCard();
            System.out.println( "The drawn card is: " + called );
            if ( !called.isCompatibleWith( top ) ) { 
                System.out.println( "No luck! you still have no playable cards, you must skip your turn" );
                System.out.println( "----------------------------------" );
                return; 
            } else {
                showHand();
            }
        }

        
        playCard();
        System.out.println( "----------------------------------" );
    }

    public abstract void playCard() throws InterruptedException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException, SlickException;

    @Override
    public void render( Graphics g ) throws SlickException {
        this.showHand( g );
    
    final int topBottomOffset = 50, sideOffset = 50;
        String playerName = this.name + " (" + cardCount() + ")";

        Color oldColor = g.getColor();
        int currentPlayerIndex = UnoGame.currentPlayerIndex; 

    
    int xPos = 0, yPos = 0, rotationAngle = 0;
        switch ( position ) {
        case BOTTOM:
            xPos = Game.WIDTH / 2 - g.getFont().getWidth( playerName ) / 2;
            yPos = Game.HEIGHT - Card.HEIGHT - topBottomOffset;
            break;
        case LEFT:
            xPos = Card.HEIGHT + sideOffset;
            yPos = Game.HEIGHT / 2 - g.getFont().getWidth( playerName ) / 2;
            rotationAngle = 90;

            g.rotate( xPos, yPos, rotationAngle ); 
            g.drawString( playerName,
                    xPos,
                    yPos );
            g.rotate( xPos, yPos, -rotationAngle ); 
            break;
        case TOP:
            xPos = Game.WIDTH / 2 + g.getFont().getWidth( playerName ) / 2;
            yPos = Card.HEIGHT + topBottomOffset;
            rotationAngle = 180;
            g.rotate( xPos, yPos, rotationAngle ); 
            g.drawString( playerName,
                    xPos,
                    yPos );
            g.rotate( xPos, yPos, -rotationAngle ); 
            break;
        case RIGHT:
            xPos = Game.WIDTH - Card.HEIGHT - sideOffset;
            yPos = Game.HEIGHT / 2 + g.getFont().getWidth( playerName ) / 2; 
            rotationAngle = 270;
            g.rotate( xPos, yPos, rotationAngle ); 
            g.drawString( playerName,
                    xPos,
                    yPos );
            g.rotate( xPos, yPos, -rotationAngle ); 
            break;
        default:
            break;
        }
        
        if ( id == currentPlayerIndex ) {
            
            Color highlight = new Color( 255, 220, 0, 120 );
            g.setColor( highlight );
            int padX = 8, padY = 4;
            g.fillRect( xPos - padX, yPos - padY, g.getFont().getWidth( playerName ) + padX * 2,
                    g.getFont().getLineHeight() + padY * 2 );
            
            g.setColor( Color.black );
            g.drawString( playerName, xPos, yPos );
            
            g.setColor( oldColor );
        } else {
            
            g.setColor( oldColor );
            g.drawString( playerName, xPos, yPos );
        }
    }

}


class PlayerHandPainter {

    public static final int topBottomOffset = 10, sideOffset = 10;

    public static float getStartPosX( SeatPosition position ) {
        switch ( position ) {
        case BOTTOM:
            return Game.WIDTH / 2 - Card.WIDTH / 2;
        case RIGHT:
            return Game.WIDTH - Card.HEIGHT + sideOffset;
        case TOP:
            return Game.WIDTH / 2 - Card.WIDTH / 2;
        case LEFT:
            return 25 + sideOffset; 
        }
        return 0;
    }

    public static float getStartPosY( SeatPosition position ) {
        switch ( position ) {
        case BOTTOM:
            return Game.HEIGHT - Card.HEIGHT - topBottomOffset;
        case RIGHT:
            return Game.HEIGHT / 2 - Card.WIDTH / 2;
        case TOP:
            
            return topBottomOffset;
        case LEFT:
            return Game.HEIGHT / 2 - Card.WIDTH / 2;
        }
        return 0;
    }

    public static float getAdditionalRotation( SeatPosition position ) {
        switch ( position ) {
        case BOTTOM:
            return 0;
        case RIGHT:
            return -90;
        case TOP:
            return 180;
        case LEFT:
            return 90;
        }
        return 0;
    }

    public static char getOffsetAxis( SeatPosition position ) {
        switch ( position ) {
        case BOTTOM:
        case TOP:
            return 'x';
        case RIGHT:
        case LEFT:
            return 'y';
        }
        return 0;
    }

    public static int getAngleCorrection( SeatPosition position ) {
        switch ( position ) {
        case BOTTOM:
        case LEFT:
            return 1;
        case TOP:
        case RIGHT:
            return -1;
        }
        return 0;
    }
}
