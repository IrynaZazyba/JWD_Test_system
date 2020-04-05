package by.jwd.testsys.controller.command.front;

import java.io.Serializable;

public class ForwardCommandException extends Exception implements Serializable {


    private static final long serialVersionUID = 2342374914398062642L;

    public ForwardCommandException() {
        super();
    }

    public ForwardCommandException(String message) {
        super(message);
    }

    public ForwardCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForwardCommandException(Throwable cause) {
        super(cause);
    }
}
