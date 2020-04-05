package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class ExistsUserException extends Exception implements Serializable {


    private static final long serialVersionUID = -6653516456097882516L;

    public ExistsUserException() {
        super();
    }

    public ExistsUserException(String message) {
        super(message);
    }

    public ExistsUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistsUserException(Throwable cause) {
        super(cause);
    }

}
