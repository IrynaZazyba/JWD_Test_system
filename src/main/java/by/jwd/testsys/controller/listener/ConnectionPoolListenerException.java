package by.jwd.testsys.controller.listener;

public class ConnectionPoolListenerException extends Exception{

    private static final long serialVersionUID = 2409278824278412949L;

    public ConnectionPoolListenerException() {
        super();
    }

    public ConnectionPoolListenerException(String message) {
        super(message);
    }

    public ConnectionPoolListenerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionPoolListenerException(Throwable cause) {
        super(cause);
    }
}
