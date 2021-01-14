package model;


public abstract class Character {

    /**
     * Boolean var that is true only if the character has used his special card.
     */
    private boolean usedAbility;

    /**
     * Color that shows card's color.
     */
    private final CharacterColor color;
    /**
     * Total amount of different characters.
     */
    public final static int TOTAL_CHARACTERS = 4;
    public final String name;
    /**
     * Constructor for a Character card.
     * @pre color must be unique for that character type.
     * @param color character's color.
     */
    Character(CharacterColor color, String name){
        this.color = color;
        this.name = name;
        usedAbility = false;
    }
    /**
     * Uses this player's ability card.
     * @pre usedAbility == false.
     * @post change usedAbility into true.
     * @throws IllegalActionException if it has been called before.
     */
    public void useAbility(){
        if(usedAbility){
            throw new IllegalActionException("Ability Already Used.");
        }
        usedAbility = true;
    }

    /**
     * Getter for this card's color.
     * @return CharacterColor enum object color.
     */
    public CharacterColor getColor() {
        return this.color;
    }
    /**
     * Getter for usedAbility
     * @return usedAbility value
     */
    public boolean getUsedAbility(){
        return usedAbility;
    }
}

class Professor extends Character{
    /**
     * Professor's special power-up/card
     */
    public static final String description = "Take 1 tile from each other sorting area";

    /**
     * Constructor for a professor instance
     * @param color enum object for current card color.
     */
    Professor(CharacterColor color){
        super(color, "Professor");
    }
}

class Digger extends Character{
    /**
     * Professor's special power-up/card
     */
    public static final String description = "Take 2 tiles from the same sorting area";

    /**
     * Constructor for a professor instance
     * @param color enum object for current card color.
     */
    Digger(CharacterColor color){
        super(color, "Digger");
    }
}

class Archaeologist extends Character{
    /**
     * Professor's special power-up/card
     */
    public static final String description = "Take 2 tiles from any other sorting area";

    /**
     * Constructor for a professor instance
     * @param color enum object for current card color.
     */
    Archaeologist(CharacterColor color){
        super(color, "Archaeologist");
    }
}

class Assistant extends Character{
    /**
     * Professor's special power-up/card
     */
    public static final String description = "Take 1 tile from any sorting area";

    /**
     * Constructor for a professor instance
     * @param color enum object for current card color.
     */
    Assistant(CharacterColor color){
        super(color, "Assistant");
    }
}