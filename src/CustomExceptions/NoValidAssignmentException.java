package CustomExceptions;

public class NoValidAssignmentException extends Exception{
    public NoValidAssignmentException(String errorMesssage) {
        super(errorMesssage);
    }
}
