package by.jwd.testsys.logic.logicCommand;

import java.io.Serializable;

public class CommandException extends Exception implements Serializable {

    private static final long serialVersionUID = -5594668304170524296L;

    public CommandException() {
        super();
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }
}
