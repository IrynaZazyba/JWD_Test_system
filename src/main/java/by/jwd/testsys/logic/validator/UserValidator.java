package by.jwd.testsys.logic.validator;

import by.jwd.testsys.bean.User;

import java.util.Set;

public interface UserValidator {

    Set<String> validate(User user);

    Set<String> validate(String login, String firstName, String lastName);

    boolean validatePassword(String password);
}
