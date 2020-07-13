package by.jwd.testsys.controller.listener;

public class InitMailConfigRuntimeException extends RuntimeException {


    private static final long serialVersionUID = -8582895888533286381L;

    public InitMailConfigRuntimeException() {
        super();
    }

    public InitMailConfigRuntimeException(String message, Exception e) {
        super(message, e);
    }

    public InitMailConfigRuntimeException(String message) {
        super(message);
    }

    public InitMailConfigRuntimeException(Throwable cause) {
        super(cause);
    }
}