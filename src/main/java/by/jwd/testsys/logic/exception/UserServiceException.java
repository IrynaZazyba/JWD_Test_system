package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class UserServiceException extends ServiceException implements Serializable {


    private static final long serialVersionUID = -4381972609276881659L;

    public UserServiceException() {
        super();
    }

    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserServiceException(Throwable cause) {
        super(cause);
    }

}
