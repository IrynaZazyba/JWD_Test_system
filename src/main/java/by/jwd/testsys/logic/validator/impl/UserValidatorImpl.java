package by.jwd.testsys.logic.validator.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.validator.UserValidator;
import by.jwd.testsys.logic.validator.util.InvalidParam;

import java.util.*;

public class UserValidatorImpl implements UserValidator {

    private static final String LOGIN_PATTERN = "[a-zA-Z0-9-_]{5,15}$";
    private static final String PASSWORD_PATTERN = "[a-zA-Z0-9_-]{6,18}$";
    private static final String NAME_PATTERN = "^([a-zA-Z-]|[а-яА-Я-]){2,25}$";

    public UserValidatorImpl() {
    }
    @Override
    public Set<String> validate(User user) {

        Set<String> validationResult = new HashSet<>();


        if (user.getLogin() == null || !validateLogin(user.getLogin())) {
            validationResult.add(InvalidParam.INVALID_LOGIN.toString());
        }

        if (user.getPassword() == null || !validatePassword(user.getPassword())) {
            validationResult.add(InvalidParam.INVALID_PASSWORD.toString());

        }
        if (user.getFirstName() == null || !validateName(user.getFirstName())) {
            validationResult.add(InvalidParam.INVALID_FIRST_NAME.toString());

        }

        if (user.getLastName() == null || !validateName(user.getLastName())) {
            validationResult.add(InvalidParam.INVALID_LAST_NAME.toString());
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
