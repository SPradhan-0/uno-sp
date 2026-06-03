package main.game;


public enum CardSymbol {

    SKIP( "Skip" ), REVERSE( "Reverse" ), PLUS2( "+2" ), PLUS4( "+4" ), WILD( "Wild" );

    
    private String value;

    
    private CardSymbol( String value ) {
        this.value = value;
    }

    
    public String getDisplayName() {
        return value;
    }
}
