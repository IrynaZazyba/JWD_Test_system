package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class TestServiceException extends ServiceException implements Serializable {


    private static final long serialVersionUID = -4381972609276881659L;

    public TestServiceException() {
        super();
    }

    public TestServiceException(String message) {
        super(message);
    }

    public TestServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestServiceException(Throwable cause) {
        super(cause);
    }

}
