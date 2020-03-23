package by.jwd.testsys.logic.validator.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.RequestParameterName;
import by.jwd.testsys.logic.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UserValidatorImpl implements Validator {

    private static final String LOGIN_PATTERN = "[a-zA-Z0-9-_]{5,15}$";
    private static final String PASSWORD_PATTERN = "[a-zA-Z0-9_-]{6,18}$";
    private static final String NAME_PATTERN = "^([a-zA-Z-]|[а-яА-Я-]){2,25}$";

    private static final String INVALID_LOGIN_MESSAGE = "Your login is invalid! The login must contain 5-15" +
            " characters: upper and lower case letters, numbers, dashes and underscores!";
    private static final String INVALID_PASSWORD_MESSAGE = "Your password is invalid. The password must contain 6-18" +
            " characters: upper and lower case letters, numbers, dashes and underscores!";
    private static final String INVALID_FIRST_NAME_MESSAGE = "First name is too long or contains numbers.";
    private static final String INVALID_LAST_NAME_MESSAGE = "Last name is too long or contains numbers or symbols.";


    private User user;

    public UserValidatorImpl(User user) {
        this.user = user;
    }

    @Override
    public Map<String, String> validate() {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("text");
        Map<String, String> validationResult = new HashMap<>();


        if (user.getLogin() == null || !validateLogin(user.getLogin())) {
            validationResult.put(RequestParameterName.LOGIN_INVALID, INVALID_LOGIN_MESSAGE);
        }

        if (user.getPassword() == null || !validatePassword(user.getPassword())) {
            validationResult.put(RequestParameterName.PASSWORD_INVALID, INVALID_PASSWORD_MESSAGE);
        }
        if (user.getFirstName() == null || !validateName(user.getFirstName())) {
            validationResult.put(RequestParameterName.FIRST_NAME_INVALID, INVALID_FIRST_NAME_MESSAGE);
        }

        if (user.getLastName() == null || !validateName(user.getLastName())) {
            validationResult.put(RequestParameterName.LAST_NAME_INVALID, INVALID_LAST_NAME_MESSAGE);
        }

        return validationResult;
    }

    /**
     * login c ограничением 5-15 символов,
     * которыми могут быть буквы и цифры,
     * символ подчеркивания, дефис.
     */

    private boolean validateLogin(String login) {
        return login.matches(LOGIN_PATTERN);
    }

    /**
     * password с ограничением 6-18 символов,
     * которыми могут быть буквы и цифры,
     * символ подчеркивания, дефис.
     */
    private boolean validatePassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    /**
     * name с ограничением 2-25 символов,
     * которыми могут быть только буквы и
     * дефис.
     */
    private boolean validateName(String name) {
        return name.matches(NAME_PATTERN);
    }


}
