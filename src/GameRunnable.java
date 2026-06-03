package main;

import org.newdawn.slick.state.StateBasedGame;

import main.game.UnoGame;

public class GameRunnable implements Runnable {
    UnoGame            game = null;
    StateBasedGame stateGame = null;

    public GameRunnable( UnoGame game, StateBasedGame stateGame ) {
        this.game = game;
        this.stateGame = stateGame;
    }

    @Override
    public void run() {
        try {
            game.start( stateGame );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
