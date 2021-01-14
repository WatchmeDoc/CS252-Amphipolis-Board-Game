package controller;

public class InvalidNumberOfPlayersException extends IllegalArgumentException {
    InvalidNumberOfPlayersException(){
        throw new IllegalArgumentException("Number of players must be 1-4");
    }
}
