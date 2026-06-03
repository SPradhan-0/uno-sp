package main.gfx;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.common.Config;
import main.common.Debug;
import main.game.CardColor;
import main.game.CardSymbol;


public class Sprite {
    
    
    
    public static HashMap<String, Image> sprites = new HashMap<>();

    
    public static Image get( CardSymbol symbol ) throws SlickException { 
        switch ( symbol ) {
        case PLUS4:
            return get( "+4" );
        case WILD:
            return get( "wild" );
        default:
            break;
        }
        Debug.err( "Image null ! " + symbol );
        return null;
    }

    
    private static Image get( String spriteName ) throws SlickException {
        if ( !sprites.containsKey( spriteName ) )
            Debug.err( "Image null ! " + spriteName );
        return sprites.get( spriteName );
    }

    
    public static Image get( CardSymbol symbol, CardColor color ) throws SlickException { 
        String colorName = null;
        switch ( color ) {
        case RED:
            colorName = "red";
            break;
        case YELLOW:
            colorName = "yellow";
            break;
        case GREEN:
            colorName = "green";
            break;
        case BLUE:
            colorName = "blue";
            break;
        case BLACK:
            
            break;
        }
        switch ( symbol ) {
        case REVERSE:
            return get( "reverse-" + colorName );
        case WILD:
            return get( symbol );
        case SKIP:
            return get( "skip-" + colorName );
        case PLUS2:
            return get( "+2-" + colorName );
        case PLUS4:
            return get( symbol );
        default:
            break;
        }
        return null;
    }

    
    public static Image get( int value, CardColor color ) throws SlickException { 
        String colorName = null;
        switch ( color ) {
        case RED:
            colorName = "red";
            break;
        case YELLOW:
            colorName = "yellow";
            break;
        case GREEN:
            colorName = "green";
            break;
        case BLUE:
            colorName = "blue";
            break;
        case BLACK:
            
            break;
        }
        return get( value + "-" + colorName );
    }

    
    public static Image getInactiveCard() throws SlickException {
        if ( !sprites.containsKey( "inactiveCard" ) )
            Debug.err( "getInactiveCard() null !" );
        return sprites.get( "inactiveCard" );
    }

    
    public static Image getHiddenCard() throws SlickException {
        if ( !sprites.containsKey( "hidden" ) )
            Debug.err( "getHiddenCard() null !" );
        return sprites.get( "hidden" );
    }

    
    public static Image getBackground( CardColor color ) throws SlickException {
        String colorName = null;
        switch ( color ) {
        case RED:
            colorName = "red";
            break;
        case YELLOW:
            colorName = "yellow";
            break;
        case GREEN:
            colorName = "green";
            break;
        case BLUE:
            colorName = "blue";
            break;
        case BLACK:
            
            break;
        }
        return get( "bg-" + colorName );
    }

    
    public static void load() throws SlickException {
        String colorNames[] = { "blue", "green", "red", "yellow" };
        String imageFileName;
        for ( String colorName : colorNames ) {
            
            for ( int i = 0; i <= 9; i++ ) {
                imageFileName = String.format( "%d-%s", i, colorName );
                sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            }
            
            imageFileName = String.format( "+2-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            
            imageFileName = String.format( "skip-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            
            imageFileName = String.format( "reverse-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            
            imageFileName = String.format( "bg-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        }
        
        imageFileName = "wild";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        
        imageFileName = "+4";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        
        imageFileName = "hidden";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        
        imageFileName = "inactiveCard";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
    }
}
