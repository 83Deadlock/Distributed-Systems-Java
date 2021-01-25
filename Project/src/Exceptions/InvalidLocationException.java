package Exceptions;

public class InvalidLocationException extends Exception {
    public InvalidLocationException(){}

    public InvalidLocationException(String msg){
        super(msg);
    }
}