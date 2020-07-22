package by.jwd.testsys.controller.command;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.command.ajax.AjaxCommandName;
import by.jwd.testsys.controller.command.ajax.impl.*;
import by.jwd.testsys.controller.command.ajax.impl.edit.*;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.CommandName;
import by.jwd.testsys.controller.command.front.impl.*;
import by.jwd.testsys.controller.command.front.impl.edit.AddTestPage;
import by.jwd.testsys.controller.command.front.impl.edit.ShowAdminPanel;
import by.jwd.testsys.controller.command.front.impl.edit.ShowPreviewTestPage;

import java.util.HashMap;
import java.util.Map;

public final class CommandProvider {

    private final static CommandProvider instance = new CommandProvider();

    private final Map<CommandName, Command> frontRepository = new HashMap<>();
    private final Map<AjaxCommandName, AjaxCommand> ajaxRepository = new HashMap<>();

    private CommandProvider() {
        frontRepository.put(CommandName.SIGN_IN, new SignIn());
        frontRepository.put(CommandName.SIGN_OUT, new SignOut());
        frontRepository.put(CommandName.SIGN_UP, new SignUp());
        frontRepository.put(CommandName.SHOW_TESTS_PAGE, new ShowTestsPage());
        frontRepository.put(CommandName.SHOW_USER_ACCOUNT, new ShowUserPage());
        frontRepository.put(CommandName.WRONG_REQUEST, new WrongRequest());
        frontRepository.put(CommandName.CHANGE_LANGUAGE, new ChangeLocal());
        frontRepository.put(CommandName.GET_RESULT, new GetResult());
        frontRepository.put(CommandName.DISPLAY_STATISTIC, new DisplayStatistic());
        frontRepository.put(CommandName.SHOW_EXE_TEST_PAGE, new ShowExeTestPage());
        frontRepository.put(CommandName.ASSIGN_TEST, new ShowAdminAssignTest());
        frontRepository.put(CommandName.TESTS_RESULTS, new TestsResults());
        frontRepository.put(CommandName.SHOW_ADMIN_PANEL, new ShowAdminPanel());
        frontRepository.put(CommandName.GET_EDIT_TEST_PAGE, new ShowPreviewTestPage());
        frontRepository.put(CommandName.ADD_TEST, new AddTestPage());
        frontRepository.put(CommandName.PREVIEW_TEST, new ShowPreviewTestPage());
        frontRepository.put(CommandName.SHOW_ASSIGNED_TESTS_PAGE, new ShowAssignedTestsPage());
        frontRepository.put(CommandName.MAIN_PAGE, new MainPage());
        frontRepository.put(CommandName.ABOUT_US, new AboutPage());
        frontRepository.put(CommandName.CONFIRM_EMAIL, new ConfirmEmail());


        ajaxRepository.put(AjaxCommandName.EDIT_USER, new EditUser());
        ajaxRepository.put(AjaxCommandName.SHOW_QUESTION, new ShowQuestion());
        ajaxRepository.put(AjaxCommandName.SAVE_ANSWER, new SaveAnswer());
        ajaxRepository.put(AjaxCommandName.GET_TESTS, new GetTests());
        ajaxRepository.put(AjaxCommandName.ASSIGN_TEST, new AssignTest());
        ajaxRepository.put(AjaxCommandName.GET_ASSIGNED_USERS, new GetAssignedUsers());
        ajaxRepository.put(AjaxCommandName.DELETE_ASSIGNMENT, new DeleteAssignment());
        ajaxRepository.put(AjaxCommandName.SHOW_RESULT_DATA, new ShowResultData());
        ajaxRepository.put(AjaxCommandName.DELETE_TEST, new DeleteTest());
        ajaxRepository.put(AjaxCommandName.CREATE_TEST, new CreateTest());
        ajaxRepository.put(AjaxCommandName.CREATE_QUESTION_ANSWER, new CreateQuestionAnswer());
        ajaxRepository.put(AjaxCommandName.UPDATE_QUESTION, new UpdateQuestion());
        ajaxRepository.put(AjaxCommandName.COMPLETE_TEST, new CompleteTestCreation());
        ajaxRepository.put(AjaxCommandName.DELETE_QUESTION, new DeleteQuestion());
        ajaxRepository.put(AjaxCommandName.UPDATE_TEST_INFO, new UpdateTestInfo());
        ajaxRepository.put(AjaxCommandName.ADD_TEST_TYPE, new AddTestType());
        ajaxRepository.put(AjaxCommandName.CHANGE_PASSWORD, new ChangePassword());


    }

    public static CommandProvider getInstance() {
        return instance;
    }

    public Command getFrontCommand(String name) {
        CommandName commandName=CommandName.valueOf(name.toUpperCase());
        Command command=frontRepository.get(commandName);

        if (command == null) {
            command = frontRepository.get(CommandName.WRONG_REQUEST);
        }

        return command;
    }

    public AjaxCommand getAjaxCommand(String name) {
        AjaxCommandName ajaxCommandName;
        AjaxCommand ajaxCommand;

        if (name != null) {
            ajaxCommandName = AjaxCommandName.valueOf(name.toUpperCase());
            ajaxCommand = ajaxRepository.get(ajaxCommandName);

            if (ajaxCommand == null) {
                ajaxCommand = ajaxRepository.get(AjaxCommandName.NO_COMMAND);
            }
        } else {
            ajaxCommand = ajaxRepository.get(AjaxCommandName.NO_COMMAND);

        }
        return ajaxCommand;
    }
}
