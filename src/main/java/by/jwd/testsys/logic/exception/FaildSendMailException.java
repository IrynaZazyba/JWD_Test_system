package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class FaildSendMailException extends ServiceException implements Serializable {


    private static final long serialVersionUID = 8103612181639382502L;

    public FaildSendMailException() {
        super();
    }

    public FaildSendMailException(String message) {
        super(message);
    }

    public FaildSendMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaildSendMailException(Throwable cause) {
        super(cause);
    }

}
