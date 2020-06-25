package by.jwd.testsys.logic.exception;

import java.io.Serializable;
import java.util.Set;

public class InvalidUserDataException extends ServiceException implements Serializable {


    private static final long serialVersionUID = 1075319083581008765L;

    private Set<String> invalidData;

    public Set<String> getInvalidData() {
        return invalidData;
    }


    public InvalidUserDataException() {
        super();
    }

    public InvalidUserDataException(String message) {
        super(message);
    }

    public InvalidUserDataException(String message, Set<String> invalidData) {
        super(message);
        this.invalidData = invalidData;
    }

    public InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserDataException(Throwable cause) {
        super(cause);
    }

}
