package by.jwd.testsys.logic.service.exception;

import java.io.Serializable;

public class ServiceException extends Exception implements Serializable {

    private static final long serialVersionUID = 6081026820292151098L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

}
