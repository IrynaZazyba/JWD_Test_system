package by.jwd.testsys.logic.ajaxCommand;

import by.jwd.testsys.logic.ajaxCommand.impl.EditUser;
import by.jwd.testsys.logic.logicCommand.Command;
import by.jwd.testsys.logic.logicCommand.CommandName;

import java.util.HashMap;
import java.util.Map;

public final class AjaxCommandProvider {

    private final static AjaxCommandProvider instance = new AjaxCommandProvider();

    private final Map<AjaxCommandName, AjaxCommand> repository = new HashMap<>();

    private AjaxCommandProvider() {
        repository.put(AjaxCommandName.EDIT_USER, new EditUser());
    }

    public static AjaxCommandProvider getInstance() {
        return instance;
    }

    public AjaxCommand getAjaxCommand(String name) {
        AjaxCommandName ajaxCommandName;
        AjaxCommand ajaxCommand;

        ajaxCommandName = AjaxCommandName.valueOf(name.toUpperCase());
        ajaxCommand = repository.get(ajaxCommandName);

        if (ajaxCommand == null) {
            ajaxCommand = repository.get(AjaxCommandName.NO_COMMAND);
        }

        return ajaxCommand;
    }
}
