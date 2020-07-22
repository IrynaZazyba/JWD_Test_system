package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class ExistsEmailException extends Exception implements Serializable {


    private static final long serialVersionUID = -6653516456097882516L;

    public ExistsEmailException() {
        super();
    }

    public ExistsEmailException(String message) {
        super(message);
    }

    public ExistsEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistsEmailException(Throwable cause) {
        super(cause);
    }

}
