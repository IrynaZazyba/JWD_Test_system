package by.jwd.testsys.logic.exception;

import java.io.Serializable;
import java.util.Set;

public class InvalidKeyException extends Exception implements Serializable {


    private static final long serialVersionUID = -7696306493587848295L;

    public InvalidKeyException() {
    }

    public InvalidKeyException(String message) {
        super(message);
    }

    public InvalidKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
