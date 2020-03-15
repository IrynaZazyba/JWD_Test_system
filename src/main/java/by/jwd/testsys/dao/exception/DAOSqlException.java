package by.jwd.testsys.dao.exception;

public class DAOSqlException extends DAOException {

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
