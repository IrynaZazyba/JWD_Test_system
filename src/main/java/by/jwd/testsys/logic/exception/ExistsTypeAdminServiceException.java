package by.jwd.testsys.logic.exception;

import java.io.Serializable;

public class ExistsTypeAdminServiceException extends Exception implements Serializable {


    private static final long serialVersionUID = -9043186455832118954L;

    public ExistsTypeAdminServiceException() {
        super();
    }

    public ExistsTypeAdminServiceException(String message) {
        super(message);
    }

    public ExistsTypeAdminServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistsTypeAdminServiceException(Throwable cause) {
        super(cause);
    }

}
