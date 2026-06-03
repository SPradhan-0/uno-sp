package main.game;


public enum CardColor {

    RED( "Red" ), YELLOW( "Yellow" ), GREEN( "Green" ), BLUE( "Blue" ), BLACK( "Black" );

    
    private String value;

    
    private CardColor( String value ) {
        this.value = value;
    }

    
    public String getDisplayName() {
        return value;
    }

    public static CardColor fromDisplayName( String selectedColor ) {
        switch ( selectedColor ) {
        case "Blue":
            return BLUE;
        case "Yellow":
            return YELLOW;
        case "Red":
            return RED;
        case "Green":
            return GREEN;
        default:
            break;
        }
        return null;
    }
}
