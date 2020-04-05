package by.jwd.testsys.logic.validator.factory;

import by.jwd.testsys.logic.validator.UserValidator;
import by.jwd.testsys.logic.validator.impl.UserValidatorImpl;

public class ValidatorFactory {

    private static ValidatorFactory instance = new ValidatorFactory();
    private static UserValidatorImpl userValidator = new UserValidatorImpl();

    private ValidatorFactory() {
    }

    public static ValidatorFactory getInstance() {
        return instance;
    }

    public UserValidator getUserValidator() {
        return userValidator;
    }

}
