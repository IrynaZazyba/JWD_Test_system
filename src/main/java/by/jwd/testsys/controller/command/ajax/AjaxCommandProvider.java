package by.jwd.testsys.controller.command.ajax;

import by.jwd.testsys.controller.command.ajax.impl.EditUser;
import by.jwd.testsys.controller.command.ajax.impl.SaveAnswer;
import by.jwd.testsys.controller.command.ajax.impl.ShowQuestion;

import java.util.HashMap;
import java.util.Map;

public final class AjaxCommandProvider {

    private final static AjaxCommandProvider instance = new AjaxCommandProvider();

    private final Map<AjaxCommandName, AjaxCommand> repository = new HashMap<>();

    private AjaxCommandProvider() {
        repository.put(AjaxCommandName.EDIT_USER, new EditUser());
        repository.put(AjaxCommandName.SHOW_QUESTION, new ShowQuestion());
        repository.put(AjaxCommandName.SAVE_ANSWER, new SaveAnswer());

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
