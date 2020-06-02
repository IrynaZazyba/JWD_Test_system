package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class AdminServiceException extends ServiceException implements Serializable {


    private static final long serialVersionUID = 8103612181639382502L;

    public AdminServiceException() {
        super();
    }

    public AdminServiceException(String message) {
        super(message);
    }

    public AdminServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdminServiceException(Throwable cause) {
        super(cause);
    }

}
