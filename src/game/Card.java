package main.game;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.common.Debug;
import main.gfx.Sprite;


public abstract class Card extends GameObject { //abstract superclass for NumberCard and ActionCard

    
    public static final int WIDTH = 86, HEIGHT = 129;

    
    public CardColor          color;

    
    public Image            image = null;

    
    public boolean          playable;

    
    public float            angle;
    
    public Shape            bounds;

    
    public Card( CardColor color ) {
        this.color = color;
        
    }

    
    public abstract boolean isCompatibleWith( Card card ); //abstract method to check if this card can be played on top of the given card

    
    @Override
    public void render( Graphics g ) throws SlickException {
        

        
        if ( image == null )
            Debug.err( this );

        
        

        this.rotate( angle ); 
        
        image.draw( x, y );

        
        

        if ( !playable ) { 
            Image inactiveCardImg = Sprite.getInactiveCard();
            inactiveCardImg.setRotation( angle );
            inactiveCardImg.draw( x, y );
        }

        
    }

    public void rotate( float degree ) {
        this.angle = degree;
        image.setRotation( degree ); 
        updateBounds();

        
    }

    public boolean isClicked( Point2D point ) {
        return bounds.contains( point );
    }

    public void updateBounds() {
        int rectX = (int) x, rectY = (int) y, rectWidth = WIDTH, rectHeight = HEIGHT;
        Shape rect = new Rectangle( rectX, rectY, rectWidth, rectHeight ); 
        
        
        
        
        
        
        if ( angle == 0 ) {
            this.bounds = rect;
            return;
        }
        
        AffineTransform transform = new AffineTransform();
        
        
        transform.rotate( Math.toRadians( angle ), rectX + rectWidth / 2, rectY + rectHeight / 2 ); 
        rect = transform.createTransformedShape( rect );
        this.bounds = rect;
    }

}
