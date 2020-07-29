package by.jwd.testsys.logic.validator.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.validator.UserValidator;
import by.jwd.testsys.logic.validator.util.InvalidParam;

import java.util.*;
import java.util.regex.Pattern;

public class UserValidatorImpl implements UserValidator {

    private static final String LOGIN_PATTERN = "[a-zA-Z0-9-_]{5,15}$";
    private static final String PASSWORD_PATTERN = "[a-zA-Z0-9_-]{6,18}$";
    private static final String NAME_PATTERN = "^([a-zA-Z-]|[а-яА-Я-]){2,25}$";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

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

        if (user.getEmail() == null || !validateEmail(user.getEmail())) {
            validationResult.add(InvalidParam.INVALID_EMAIL.toString());
        }
        return validationResult;
    }


    @Override
    public Set<String> validate(String login, String firstName, String lastName) {

        Set<String> validationResult = new HashSet<>();


        if (login == null || !validateLogin(login)) {
            validationResult.add(InvalidParam.INVALID_LOGIN.toString());
        }

        if (firstName == null || !validateName(firstName)) {
            validationResult.add(InvalidParam.INVALID_FIRST_NAME.toString());
        }

        if (lastName == null || !validateName(lastName)) {
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
    @Override
    public boolean validatePassword(String password) {
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


    private boolean validateEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }
}
