package by.jwd.testsys.controller.command.front;

import by.jwd.testsys.controller.command.front.impl.*;

import java.util.HashMap;
import java.util.Map;

public final class CommandProvider {

    private final static CommandProvider instance = new CommandProvider();

    private final Map<CommandName, Command> repository = new HashMap<>();

    private CommandProvider() {
        repository.put(CommandName.SIGN_IN, new SignIn());
        repository.put(CommandName.SIGN_OUT, new SignOut());
        repository.put(CommandName.SIGN_UP, new SignUp());
        repository.put(CommandName.SHOW_TESTS_PAGE, new ShowTestsPage());
        repository.put(CommandName.SHOW_USER_ACCOUNT, new ShowUserPage());
        repository.put(CommandName.WRONG_REQUEST, new WrongRequest());
        repository.put(CommandName.CHANGE_LANGUAGE, new ChangeLocal());
        repository.put(CommandName.GET_RESULT,new GetResult());
        repository.put(CommandName.DISPLAY_STATISTIC, new DisplayStatistic());
        repository.put(CommandName.SHOW_EXE_TEST_PAGE, new ShowExeTestPage());
        repository.put(CommandName.ASSIGN_TEST, new ShowAdminAssignTest());

    }

    public static CommandProvider getInstance() {
        return instance;
    }

    public Command getCommand(String name, String security) {
        CommandName commandName;
        Command command;

        if (name == null||security!=null) {
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
