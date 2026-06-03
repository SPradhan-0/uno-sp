package main.io;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import main.AppIcon;
import main.game.UnoGame;


public class ColorSelectionDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JButton btnBlue, btnYellow, btnRed, btnGreen;
    public String selectedColor;

    public ColorSelectionDialog() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        initComponents();
        createEvents();
    }

    private void createEvents() {
        btnBlue.addActionListener(e -> { selectedColor = "Blue"; dispose(); });
        btnYellow.addActionListener(e -> { selectedColor = "Yellow"; dispose(); });
        btnRed.addActionListener(e -> { selectedColor = "Red"; dispose(); });
        btnGreen.addActionListener(e -> { selectedColor = "Green"; dispose(); });
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                
                try {
                    UnoGame.waitForDialogCountDownLatch.countDown();
                } catch (Exception ex) {
                    
                }
            }
        });

        setTitle("Choose a color");
        AppIcon.applyTo(this);
        setResizable(false);
        setBounds(100, 100, 320, 90);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        btnBlue = new JButton("Blue");
        btnBlue.setForeground(Color.BLACK);
        btnBlue.setBackground(Color.BLUE);

        btnYellow = new JButton("Yellow");
        btnYellow.setBackground(Color.YELLOW);
        btnYellow.setForeground(Color.BLACK);

        btnRed = new JButton("Red");
        btnRed.setBackground(Color.RED);
        btnRed.setForeground(Color.BLACK);

        btnGreen = new JButton("Green");
        btnGreen.setForeground(Color.BLACK);
        btnGreen.setBackground(Color.GREEN);

        GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
        gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addComponent(btnBlue)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(btnYellow)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(btnRed)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(btnGreen).addContainerGap(20,
                                Short.MAX_VALUE)));
        gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(gl_contentPanel.createSequentialGroup().addContainerGap()
                        .addGroup(gl_contentPanel.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btnBlue)
                                .addComponent(btnYellow).addComponent(btnRed).addComponent(btnGreen))
                        .addContainerGap(10, Short.MAX_VALUE)));
        contentPanel.setLayout(gl_contentPanel);
    }

}
