package by.jwd.testsys.dao.exception;

import java.io.Serializable;

public class DAOSqlException extends DAOException implements Serializable {

    private static final long serialVersionUID = -2208227899151192160L;

    public DAOSqlException() {
        super();
    }

    public DAOSqlException(String message) {
        super(message);
    }

    public DAOSqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOSqlException(Throwable cause) {
        super(cause);
    }

}
