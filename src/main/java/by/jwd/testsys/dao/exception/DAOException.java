package by.jwd.testsys.dao.exception;

public class DAOException extends Exception {

    private static final long serialVersionUID = 2220592095980878035L;

    public DAOException() {
        super();
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }

}
