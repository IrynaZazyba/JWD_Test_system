package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class TimeIsOverServiceException extends ServiceException implements Serializable {


    private static final long serialVersionUID = -784893494892497483L;

    public TimeIsOverServiceException() {
        super();
    }

    public TimeIsOverServiceException(String message) {
        super(message);
    }

    public TimeIsOverServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeIsOverServiceException(Throwable cause) {
        super(cause);
    }

}
