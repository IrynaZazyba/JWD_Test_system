package by.jwd.testsys.dao.exception;

public class DAOFactoryException extends DAOException {


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
