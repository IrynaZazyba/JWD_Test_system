package by.jwd.testsys.controller.command.ajax;

import by.jwd.testsys.controller.command.ajax.impl.*;

import java.util.HashMap;
import java.util.Map;

public final class AjaxCommandProvider {

    private final static AjaxCommandProvider instance = new AjaxCommandProvider();

    private final Map<AjaxCommandName, AjaxCommand> repository = new HashMap<>();

    private AjaxCommandProvider() {
        repository.put(AjaxCommandName.EDIT_USER, new EditUser());
        repository.put(AjaxCommandName.SHOW_QUESTION, new ShowQuestion());
        repository.put(AjaxCommandName.SAVE_ANSWER, new SaveAnswer());
        repository.put(AjaxCommandName.GET_TESTS, new GetTests());
        repository.put(AjaxCommandName.ASSIGN_TEST, new AssignTest());
        repository.put(AjaxCommandName.GET_ASSIGNED_USERS, new GetAssignedUsers());
        repository.put(AjaxCommandName.DELETE_ASSIGNMENT, new DeleteAssignment());
        repository.put(AjaxCommandName.SHOW_RESULT_DATA, new ShowResultData());
        repository.put(AjaxCommandName.DELETE_TEST, new DeleteTest());
        repository.put(AjaxCommandName.CREATE_TEST, new CreateTest());
        repository.put(AjaxCommandName.CREATE_QUESTION_ANSWER, new CreateQuestionAnswer());
    }

    public static AjaxCommandProvider getInstance() {
        return instance;
    }

    public AjaxCommand getAjaxCommand(String name) {
        AjaxCommandName ajaxCommandName;
        AjaxCommand ajaxCommand;

        if (name != null) {
            ajaxCommandName = AjaxCommandName.valueOf(name.toUpperCase());
            ajaxCommand = repository.get(ajaxCommandName);

            if (ajaxCommand == null) {
                ajaxCommand = repository.get(AjaxCommandName.NO_COMMAND);
            }
        } else {
            ajaxCommand = repository.get(AjaxCommandName.NO_COMMAND);

        }
        return ajaxCommand;
    }
}
