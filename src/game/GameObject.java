package main.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public abstract class GameObject {

    
    public float x    = 0, y = 0;
    
    public float velX = 0, velY = 0; 

    
    public abstract void update( GameContainer container ) throws SlickException;

    
    public abstract void render( Graphics g ) throws SlickException;

}
