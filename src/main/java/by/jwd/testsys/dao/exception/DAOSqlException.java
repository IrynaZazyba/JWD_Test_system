package by.jwd.testsys.dao.exception;

public class DAOSqlException extends DAOException {

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
