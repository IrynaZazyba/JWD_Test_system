package by.jwd.testsys.dao.exception;

import java.io.Serializable;

public class DAOFactoryException extends DAOException implements Serializable {


    private static final long serialVersionUID = -8499271093648949380L;

    public DAOFactoryException() {
        super();
    }

    public DAOFactoryException(String message) {
        super(message);
    }

    public DAOFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOFactoryException(Throwable cause) {
        super(cause);
    }

}
