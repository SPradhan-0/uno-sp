package main;

import java.awt.Image;
import java.awt.Taskbar;
import java.awt.Window;

import javax.swing.ImageIcon;

public final class AppIcon {
    public static final String PATH = "res/gfx/hidden.png";

    private static final ImageIcon ICON = new ImageIcon( PATH );

    private AppIcon() {
    }

    public static Image getImage() {
        return ICON.getImage();
    }

    public static ImageIcon getSwingIcon() {
        return ICON;
    }

    public static void applyTo( Window window ) {
        window.setIconImage( getImage() );
    }

    public static void applyToTaskbar() {
        if ( !Taskbar.isTaskbarSupported() ) {
            return;
        }

        Taskbar taskbar = Taskbar.getTaskbar();
        if ( taskbar.isSupported( Taskbar.Feature.ICON_IMAGE ) ) {
            taskbar.setIconImage( getImage() );
        }
    }
}
