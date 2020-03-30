package by.jwd.testsys.logic.validator.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.parameters.RequestParameterName;
import by.jwd.testsys.logic.validator.Validator;

import java.util.*;

public class UserValidatorImpl implements Validator {

    private static final String LOGIN_PATTERN = "[a-zA-Z0-9-_]{5,15}$";
    private static final String PASSWORD_PATTERN = "[a-zA-Z0-9_-]{6,18}$";
    private static final String NAME_PATTERN = "^([a-zA-Z-]|[а-яА-Я-]){2,25}$";

    private User user;
    private Locale locale;

    public UserValidatorImpl(User user, String locale) {
        this.user = user;
        if (locale != null) {
            this.locale = new Locale(locale);
        } else {
            this.locale = Locale.getDefault();
        }
    }

    @Override
    public Map<String, String> validate() {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("local/local",locale);

        Map<String, String> validationResult = new HashMap<>();


        if (user.getLogin() == null || !validateLogin(user.getLogin())) {
            validationResult.put(RequestParameterName.LOGIN_INVALID, resourceBundle.getString("message.invalid_login"));
        }

        if (user.getPassword() == null || !validatePassword(user.getPassword())) {
            validationResult.put(RequestParameterName.PASSWORD_INVALID, resourceBundle.getString("message.invalid_password"));

        }
        if (user.getFirstName() == null || !validateName(user.getFirstName())) {
            validationResult.put(RequestParameterName.FIRST_NAME_INVALID, resourceBundle.getString("message.invalid_first_name"));

        }

        if (user.getLastName() == null || !validateName(user.getLastName())) {
            validationResult.put(RequestParameterName.LAST_NAME_INVALID, resourceBundle.getString("message.invalid_last_name"));
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
