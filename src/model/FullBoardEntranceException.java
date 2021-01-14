package model;

public class FullBoardEntranceException extends IllegalStateException{
    FullBoardEntranceException(){
        throw new IllegalStateException("Board entrance is full!");
    }
}
