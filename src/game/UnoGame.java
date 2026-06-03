package main.game;

import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.LWJGLException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import main.Game;
import main.AppIcon;
import main.common.Config;
import main.common.Debug;
import main.gfx.Sprite;
import main.io.Audio;
import main.io.ColorSelectionDialog;
import main.states.GameOverState;


public class UnoGame {
    
    private DrawPile               drawPile;
    
    private DiscardPile                discardPile;
    
    private int                  playerCount;
    
    private Player[]             players;
    
    private int                  direction  = -1;                 
    
    static int                   currentPlayerIndex  = 0;
    
    public static Player         currentPlayer;
    
    
    static Input                 input = null;
    
    static CountDownLatch        countDownLatch;
    
    public static CountDownLatch waitForDialogCountDownLatch;
    
    
    
    public static ColorSelectionDialog dialog;

    
    public boolean unoCalled = false;
    
    public boolean[] unoCalledByPlayer;

    
    public UnoGame() throws SlickException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        init();
    }

    
    public void start( StateBasedGame stateGame ) throws InterruptedException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SlickException {
    //handles the main game loop and rules
                Audio.playMusic();
    System.out.println( "=== Game start ===" );
        boolean actionEffectPending = false; 
        while ( true ) { 
            currentPlayer = players[currentPlayerIndex];
            resetUnoCall( currentPlayer );
            System.out.println( "Turn of " + currentPlayer.name );
            
            
            
    // action cards are handled here
            if ( actionEffectPending && discardPile.top() instanceof ActionCard ) { 
                actionEffectPending = false;
                if ( ( (ActionCard) discardPile.top() ).getSymbol() == CardSymbol.SKIP ) { 
            System.out.println( currentPlayer.name + " must skip their turn -> effect of card: "
                + discardPile.top().toString() );
                    Audio.playSound( "clickSound" );
                    advanceToNextPlayer();
                    continue;
                }
                if ( ( (ActionCard) discardPile.top() ).getSymbol() == CardSymbol.PLUS2 ) { 
                    
            System.out.println( currentPlayer.name
                + " must draw 2 cards and skip their turn -> effect of card " + discardPile.top() );
                    Audio.playSound( "clickSound" );
                    for ( int i = 0; i < 2; i++ ) {
                        currentPlayer.drawCard();
                    }
                    
                    advanceToNextPlayer();
                    continue;
                }
                if ( ( (ActionCard) discardPile.top() ).getSymbol() == CardSymbol.PLUS4 ) { 
                    
            System.out.println( currentPlayer.name
                + " must draw 4 cards and skip their turn -> effect of card " + discardPile.top() );
                    Audio.playSound( "clickSound" );
                    for ( int i = 0; i < 4; i++ ) {
                        currentPlayer.drawCard();
                    }
                    
                    advanceToNextPlayer();
                    continue;
                }
            }
            currentPlayer.playTurn();
            Debug.log( "======== End of Turn ========" );
            
            updatePlayersHands();

            if ( currentPlayer.cardCount() == 0 ) { 
                System.out.println( currentPlayer.name + " has won!" );
                Audio.playSound( "winSound" );
                break;
            }
            
            if ( currentPlayer.cardCount() == 1 ) {
                boolean called = consumeUnoCall( currentPlayer );
                if ( called ) {
                    System.out.println( currentPlayer.name + " <UNO!> (called)" );
                    Audio.playSound( "unoSound" );
                } else {
                    System.out.println( currentPlayer.name + " failed to call UNO! Drawing 2 penalty cards." );
                    Audio.playSound( "invalidClickSound" );
                    for ( int i = 0; i < 2; i++ ) {
                        currentPlayer.drawCard();
                    }
                }
            } else {
                resetUnoCall( currentPlayer );
            }
            
            
            if ( currentPlayer.playedCard != null && discardPile.top() instanceof ActionCard ) { 
                
                
                
                actionEffectPending = true; 
                if ( ( (ActionCard) discardPile.top() ).getSymbol() == CardSymbol.REVERSE ) {
                    
                    System.out.println( currentPlayer.name + " reversed the play direction" );
                    Audio.playSound( "clickSound" );
                    direction *= -1; 
                    actionEffectPending = false; 
                }
            }
            advanceToNextPlayer();
            System.out.println();
        }
    System.out.println( "=== End of game ===" );
        Audio.stopMusic();
        
        stateGame.enterState( GameOverState.stateID );
    }

    
    private void updatePlayersHands() {
        for ( Player player : players ) { 
            boolean isActive = ( player == currentPlayer );
            for ( Card card : player.hand.cards ) { 
                
                if ( isActive ) {
                    card.playable = card.isCompatibleWith( discardPile.top() );
                } else {
                    card.playable = false;
                }
            }
        }
    }

    
    private void advanceToNextPlayer() {
        
        currentPlayerIndex += direction;
        
        if ( currentPlayerIndex < 0 ) {
            currentPlayerIndex += playerCount;
            
            
        }
        if ( currentPlayerIndex > playerCount - 1 ) { 
            currentPlayerIndex -= playerCount;
            
            
        }
    }
// handles input as well as the uno buttons logic
    public void update( GameContainer container ) throws SlickException {
        input = container.getInput();
        for ( int i = 0; i < players.length; i++ ) { 
            players[i].update( container );
        }

        
        int btnW = 120, btnH = 40;
        int x = container.getWidth() - btnW - 20;
        int y = container.getHeight() - btnH - 20;
        if ( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) {
            int mx = input.getMouseX(), my = input.getMouseY();
            if ( mx >= x && mx <= x + btnW && my >= y && my <= y + btnH ) {
                
                if ( canCurrentPlayerCallUno() ) {
                    callUno( currentPlayer );
                } else {
                    
                    try {
                        Audio.playSound( "invalidClickSound" );
                    } catch ( SlickException e ) {
                        Debug.err( e.toString() );
                    }
                }
            }
        }
    }

    public boolean canCurrentPlayerCallUno() {
        return currentPlayer != null && currentPlayer.cardCount() == 2;
    }

    public synchronized boolean wasUnoCalled( int playerId ) {
        return unoCalledByPlayer != null
                && playerId >= 0
                && playerId < unoCalledByPlayer.length
                && unoCalledByPlayer[playerId];
    }

    private synchronized void callUno( Player player ) {
        if ( player == null ) {
            return;
        }
        if ( unoCalledByPlayer == null ) {
            unoCalledByPlayer = new boolean[players.length];
        }
        if ( player.id >= 0 && player.id < unoCalledByPlayer.length ) {
            unoCalledByPlayer[player.id] = true;
            System.out.println( player.name + " pressed UNO" );
        }
    }

    private synchronized boolean consumeUnoCall( Player player ) {
        if ( player == null || unoCalledByPlayer == null || player.id < 0 || player.id >= unoCalledByPlayer.length ) {
            return false;
        }
        boolean called = unoCalledByPlayer[player.id];
        unoCalledByPlayer[player.id] = false;
        return called;
    }

    private synchronized void resetUnoCall( Player player ) {
        if ( player != null && unoCalledByPlayer != null && player.id >= 0 && player.id < unoCalledByPlayer.length ) {
            unoCalledByPlayer[player.id] = false;
        }
    }

// handles drawing of new cards  and the discard pile
    public void render( Graphics g ) throws SlickException {
        changeBackgroundColorTo( g, discardPile.top().color );

        for ( int i = 0; i < players.length; i++ ) { 
            players[i].render( g );
        }

        drawPile.render( g );
        discardPile.render( g );
    }

    public static void changeBackgroundColorTo( Graphics g, CardColor color ) throws SlickException {
        
        Color called = Color.darkGray; 
        switch ( color ) {
        case BLUE:
            called = new Color( 0x66, 0x99, 0xFF );
            break;
        case GREEN:
            called = new Color( 0x66, 0xFF, 0x99 );
            break;
        case RED:
            called = new Color( 0xFF, 0x66, 0x66 );
            break;
        case YELLOW:
            called = new Color( 0xFF, 0xFF, 0x99 );
            break;
        case BLACK:
            called = new Color( 0x33, 0x33, 0x33 );
            break;
        }
        g.setColor( called );
        g.fillRect( 0, 0, Game.WIDTH, Game.HEIGHT );
    }

    public void init() throws SlickException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        
        

        dialog = new ColorSelectionDialog();
        

        Sprite.load();
        Config.load();
        drawPile = new DrawPile();
        drawPile.shuffle();
        discardPile = new DiscardPile( drawPile );

        

        playerCount = Integer.parseInt( Config.get( "playerCount" ) );
        players = new Player[playerCount];
        SeatPosition playerPositions[] = new SeatPosition[playerCount];

        switch ( playerCount ) {
        case 2:
            
            playerPositions[0] = SeatPosition.BOTTOM;
            playerPositions[1] = SeatPosition.TOP;
            break;
        case 3:
            
            playerPositions[0] = SeatPosition.BOTTOM;
            playerPositions[1] = SeatPosition.RIGHT;
            playerPositions[2] = SeatPosition.LEFT;
            break;
        case 4:
            
            
            playerPositions[0] = SeatPosition.BOTTOM;
            playerPositions[1] = SeatPosition.RIGHT;
            playerPositions[2] = SeatPosition.TOP;
            playerPositions[3] = SeatPosition.LEFT;
            break;
        default:
            break;
        }

        
        for ( int i = 0; i < playerCount; i++ ) {
            
            
            

            if ( i == 0 ) {
                
                String playerNameInput = askPlayerName();
                players[i] = new HumanPlayer( playerNameInput, drawPile, discardPile );
            } else {
                
                players[i] = new Bot( "Bot" + i, drawPile, discardPile );
            }

            players[i].position = playerPositions[i];
            players[i].id = i;
        }

        

        for ( int i = 0; i < players.length; i++ ) { 
            for ( int j = 0; j < 7; j++ ) { 
                players[i].drawCard();
            }
        }

        // prepare per-player UNO call flags
        unoCalledByPlayer = new boolean[playerCount];

    }

    private String askPlayerName() {
        JOptionPane playerNamePane = new JOptionPane( "Player name?", JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION, AppIcon.getSwingIcon() );
        playerNamePane.setWantsInput( true );

        JDialog dialog = playerNamePane.createDialog( null, "UNO" );
        AppIcon.applyTo( dialog );
        dialog.setVisible( true );
        dialog.dispose();

        Object input = playerNamePane.getInputValue();
        if ( input == JOptionPane.UNINITIALIZED_VALUE || input == null ) {
            return "Player";
        }

        String playerName = input.toString().trim();
        if ( playerName.isEmpty() ) {
            return "Player";
        }

        return playerName;
    }

}
