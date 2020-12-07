package CustomExceptions;

public class InvalidChildException extends Exception {
    public InvalidChildException(String errorMessage) {
        super(errorMessage);
    }
}
