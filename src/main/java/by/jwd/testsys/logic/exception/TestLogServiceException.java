package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class TestLogServiceException extends ServiceException implements Serializable {


    private static final long serialVersionUID = 6101439236202403924L;

    public TestLogServiceException() {
        super();
    }

    public TestLogServiceException(String message) {
        super(message);
    }

    public TestLogServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestLogServiceException(Throwable cause) {
        super(cause);
    }

}
