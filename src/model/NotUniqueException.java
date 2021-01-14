package model;

public class NotUniqueException extends IllegalArgumentException{
    NotUniqueException(String Type){
        throw new IllegalArgumentException("This " + Type + " is not unique.");
    }
}
