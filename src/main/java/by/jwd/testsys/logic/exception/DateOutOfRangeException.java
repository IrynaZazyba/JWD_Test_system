package by.jwd.testsys.logic.exception;

public class DateOutOfRangeException extends Exception {

    private static final long serialVersionUID = 3040716395876410212L;

    public DateOutOfRangeException() {
    }

    public DateOutOfRangeException(String message) {
        super(message);
    }

    public DateOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateOutOfRangeException(Throwable cause) {
        super(cause);
    }
}
