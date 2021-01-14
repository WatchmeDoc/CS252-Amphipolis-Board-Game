package model;

public class IllegalActionException extends IllegalStateException{
    public IllegalActionException(String Message) throws IllegalStateException{
        throw new IllegalStateException(Message);
    }
}
