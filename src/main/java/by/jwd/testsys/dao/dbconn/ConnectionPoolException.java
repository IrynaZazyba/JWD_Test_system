package by.jwd.testsys.dao.dbconn;

public class ConnectionPoolException extends Exception {


    private static final long serialVersionUID = -1383561967708417250L;

    public ConnectionPoolException() {
        super();
    }

    public ConnectionPoolException(String message, Exception e) {
        super(message, e);
    }

    public ConnectionPoolException(String message) {
        super(message);
    }

    public ConnectionPoolException(Throwable cause) {
        super(cause);
    }
}