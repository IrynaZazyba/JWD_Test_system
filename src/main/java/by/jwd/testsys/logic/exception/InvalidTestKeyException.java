package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class InvalidTestKeyException extends Exception implements Serializable {


    private static final long serialVersionUID = 5508424872443883943L;

    private String invalidKey;

    public String getInvalidKey() {
        return invalidKey;
    }


    public InvalidTestKeyException() {
        super();
    }

    public InvalidTestKeyException(String message) {
        super(message);
    }

    public InvalidTestKeyException(String message, String invalidData) {
        super(message);
        this.invalidKey = invalidData;
    }

    public InvalidTestKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTestKeyException(Throwable cause) {
        super(cause);
    }

}
