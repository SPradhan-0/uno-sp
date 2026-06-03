package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class ClientConfigWindow extends JDialog {

    private static final long serialVersionUID = 3481165213363744732L;
    private final JPanel      contentPanel     = new JPanel();
    private JSpinner          windowWidthSpinner;
    private JSpinner          windowHeightSpinner;
    private JCheckBox         playMusicCheckbox;
    private JCheckBox         playSoundsCheckbox;
    private JButton           saveConfigButton;

    private String            configFilePath   = "config.ini";

    
    public static void main( String[] args ) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException {
        new ClientConfigWindow();
        
        
        
    }

    
    public ClientConfigWindow() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        initComponents();
        loadConfig();
        createEvents();
    }

    private void createEvents() {
        saveConfigButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent arg0 ) {
                Properties p = new Properties();

                InputStream is;
                try {
                    
                    is = new FileInputStream( configFilePath );
                    p.load( is );
                } catch ( IOException e1 ) {
                    e1.printStackTrace();
                }

                OutputStream os = null;
                try {
                    os = new FileOutputStream( configFilePath );
                    p.setProperty( "windowWidth", String.valueOf( windowWidthSpinner.getValue() ) );
                    p.setProperty( "windowHeight", String.valueOf( windowHeightSpinner.getValue() ) );
                    p.setProperty( "playMusic", String.valueOf( playMusicCheckbox.isSelected() ) );
                    p.setProperty( "playSounds", String.valueOf( playSoundsCheckbox.isSelected() ) );
                    p.store( os, null );

                    
                    Color oldBgColor = saveConfigButton.getBackground(),
                            oldTextColor = saveConfigButton.getForeground();

                    saveConfigButton.setBackground( Color.blue );
                    saveConfigButton.setForeground( Color.blue );
                    saveConfigButton.setText( " == Saved == " );

                    
                    Timer timer = new Timer( 2000, new ActionListener() {
                        @Override
                        public void actionPerformed( ActionEvent arg0 ) {
                            saveConfigButton.setText( "Save configuration" );
                            saveConfigButton.setBackground( oldBgColor );
                            saveConfigButton.setForeground( oldTextColor );
                        }
                    } );
                    timer.setRepeats( false );
                    timer.start();

                } catch ( Exception e ) {
                    e.printStackTrace();
                } finally {
                    try {
                        os.close();
                    } catch ( IOException e ) {
                        e.printStackTrace();
                    }
                }

            }
        } );
    }

    private void loadConfig() {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream( configFilePath );
            p.load( is );
            p.list( System.out );

            int windowWidth, windowHeight;
            boolean playMusic, playSounds;
            windowWidth = Integer.parseInt( p.getProperty( "windowWidth" ) );
            windowHeight = Integer.parseInt( p.getProperty( "windowHeight" ) );
            playMusic = Boolean.parseBoolean( p.getProperty( "playMusic" ) );
            playSounds = Boolean.parseBoolean( p.getProperty( "playSounds" ) );

            
            

            windowWidthSpinner.setValue( windowWidth );
            windowHeightSpinner.setValue( windowHeight );
            playMusicCheckbox.setSelected( playMusic );
            playSoundsCheckbox.setSelected( playSounds );

        } catch ( Exception e ) {
            e.printStackTrace();
            
        } finally {
            try {
                if ( is != null ) {
                    is.close();
                }
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    private void initComponents() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        AppIcon.applyTo( this );
        setTitle( "UNO - Configuration" );
        setBounds( 100, 100, 284, 212 );
        setLocationRelativeTo( null );
        getContentPane().setLayout( new BorderLayout() );
        contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
        getContentPane().add( contentPanel, BorderLayout.CENTER );
        contentPanel.setLayout( null );

    JLabel windowWidthLabel = new JLabel( "Window Width" );
        windowWidthLabel.setBounds( 10, 35, 106, 14 );
        contentPanel.add( windowWidthLabel );

    JLabel windowHeightLabel = new JLabel( "Window Height" );
        windowHeightLabel.setBounds( 10, 66, 106, 14 );
        contentPanel.add( windowHeightLabel );

    windowWidthSpinner = new JSpinner();
    windowWidthSpinner.setModel( new SpinnerNumberModel( Integer.valueOf( 800 ), Integer.valueOf( 600 ), null, Integer.valueOf( 1 ) ) );
        windowWidthSpinner.setBounds( 136, 32, 122, 20 );
        contentPanel.add( windowWidthSpinner );

    windowHeightSpinner = new JSpinner();
    windowHeightSpinner
        .setModel( new SpinnerNumberModel( Integer.valueOf( 600 ), Integer.valueOf( 600 ), null, Integer.valueOf( 1 ) ) );
        windowHeightSpinner.setBounds( 136, 63, 122, 20 );
        contentPanel.add( windowHeightSpinner );

    playMusicCheckbox = new JCheckBox( "Play Music" );
        playMusicCheckbox.setSelected( true );
        playMusicCheckbox.setBounds( 161, 113, 97, 23 );
        contentPanel.add( playMusicCheckbox );

    playSoundsCheckbox = new JCheckBox( "Play Sounds" );
        playSoundsCheckbox.setSelected( true );
        playSoundsCheckbox.setBounds( 10, 113, 97, 23 );
        contentPanel.add( playSoundsCheckbox );

    saveConfigButton = new JButton( "Save configuration" );

        saveConfigButton.setBounds( 10, 143, 248, 23 );
        contentPanel.add( saveConfigButton );

        setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        setVisible( true );
    }
}
