package by.jwd.testsys.logic.logicCommand;

import by.jwd.testsys.logic.logicCommand.impl.*;

import java.util.HashMap;
import java.util.Map;

public final class CommandProvider {

    private final static CommandProvider instance = new CommandProvider();

    private final Map<CommandName, Command> repository = new HashMap<>();

    private CommandProvider() {
        repository.put(CommandName.SIGN_IN, new SignIn());
        repository.put(CommandName.SIGN_OUT, new SignOut());
        repository.put(CommandName.SIGN_UP, new SignUp());
        repository.put(CommandName.SHOW_MAIN_PAGE, new ShowMainPage());
        repository.put(CommandName.SHOW_USER_ACCOUNT, new ShowUserPage());
        repository.put(CommandName.WRONG_REQUEST, new WrongRequest());
        repository.put(CommandName.CHANGE_LANGUAGE, new ChangeLocal());

    }

    public static CommandProvider getInstance() {
        return instance;
    }

    public Command getCommand(String name) {
        CommandName commandName;
        Command command;

        if (name == null) {
            command = repository.get(CommandName.WRONG_REQUEST);
        } else {

            commandName = CommandName.valueOf(name.toUpperCase());
            command = repository.get(commandName);

            if (command == null) {
                command = repository.get(CommandName.WRONG_REQUEST);
            }
        }
        return command;
    }
}
