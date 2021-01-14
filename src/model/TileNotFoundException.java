package model;

public class TileNotFoundException extends IllegalArgumentException{
    TileNotFoundException(){
        throw new IllegalArgumentException("Tile not found on board!");
    }
}
