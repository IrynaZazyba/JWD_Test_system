package by.jwd.testsys.controller.listener;

public class InitConnectionPoolRuntimeException extends RuntimeException {


    private static final long serialVersionUID = -8582895888533286381L;

    public InitConnectionPoolRuntimeException() {
        super();
    }

    public InitConnectionPoolRuntimeException(String message, Exception e) {
        super(message, e);
    }

    public InitConnectionPoolRuntimeException(String message) {
        super(message);
    }

    public InitConnectionPoolRuntimeException(Throwable cause) {
        super(cause);
    }
}