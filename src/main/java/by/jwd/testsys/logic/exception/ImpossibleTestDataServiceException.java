package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class ImpossibleTestDataServiceException extends ServiceException implements Serializable {


    private static final long serialVersionUID = -8514441611971265195L;

    public ImpossibleTestDataServiceException() {
        super();
    }

    public ImpossibleTestDataServiceException(String message) {
        super(message);
    }

    public ImpossibleTestDataServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImpossibleTestDataServiceException(Throwable cause) {
        super(cause);
    }

}
