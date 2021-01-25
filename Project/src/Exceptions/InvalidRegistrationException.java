package Exceptions;

public class InvalidRegistrationException extends Exception{

    /**
     * Creates a new instance of Exceptions.InvalidRegistrationException without detail message.
     */

    public InvalidRegistrationException() {}

    /**
     * Constructs a new instance of Exceptions.InvalidRegistrationException with the specified message.
     *
     * @param msg - the detail message.
     */

    public InvalidRegistrationException(String msg) {
        super(msg);
    }
}
