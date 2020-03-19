package by.jwd.testsys.dao.exception;

import java.io.Serializable;

public class DAOConnectionPoolException extends DAOException implements Serializable {

    private static final long serialVersionUID = -5157495278469516335L;

    public DAOConnectionPoolException() {
        super();
    }

    public DAOConnectionPoolException(String message) {
        super(message);
    }

    public DAOConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOConnectionPoolException(Throwable cause) {
        super(cause);
    }

}
