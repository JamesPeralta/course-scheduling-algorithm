package CustomExceptions;

public class RunningTooLongException extends Exception{
    public RunningTooLongException(String errorMessage) {
        super(errorMessage);
    }
}
