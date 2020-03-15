package by.jwd.testsys.dao.exception;

public class DAOConnectionPoolException extends DAOException {

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
