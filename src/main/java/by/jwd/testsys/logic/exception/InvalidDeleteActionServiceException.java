package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class InvalidDeleteActionServiceException extends ServiceException implements Serializable {


    private static final long serialVersionUID = 7544535239836925055L;

    public InvalidDeleteActionServiceException() {
        super();
    }

    public InvalidDeleteActionServiceException(String message) {
        super(message);
    }

    public InvalidDeleteActionServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDeleteActionServiceException(Throwable cause) {
        super(cause);
    }

}
