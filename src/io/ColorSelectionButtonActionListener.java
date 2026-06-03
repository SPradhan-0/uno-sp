package main.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ColorSelectionButtonActionListener implements ActionListener {

    
    String         color;
    
    ColorSelectionDialog dialog;

    
    public ColorSelectionButtonActionListener( ColorSelectionDialog dialog, String color ) {
        this.dialog = dialog;
        this.color = color;
    }

    
    @Override
    public void actionPerformed( ActionEvent e ) {
        dialog.selectedColor = color; 
        System.out.println( "SelectedColor = " + color );
        dialog.dispose(); 
    }
}
