package by.jwd.testsys.logic.validator.factory;

import by.jwd.testsys.logic.validator.FrontDataValidator;
import by.jwd.testsys.logic.validator.TestValidator;
import by.jwd.testsys.logic.validator.UserValidator;
import by.jwd.testsys.logic.validator.impl.FrontDataValidatorImpl;
import by.jwd.testsys.logic.validator.impl.TestValidatorImpl;
import by.jwd.testsys.logic.validator.impl.UserValidatorImpl;

public class ValidatorFactory {

    private static ValidatorFactory instance = new ValidatorFactory();
    private static UserValidator userValidator = new UserValidatorImpl();
    private static TestValidator testValidator = new TestValidatorImpl();
    private static FrontDataValidator frontDataValidator = new FrontDataValidatorImpl();

    private ValidatorFactory() {
    }

    public static ValidatorFactory getInstance() {
        return instance;
    }

    public UserValidator getUserValidator() {
        return userValidator;
    }

    public TestValidator getTestValidator() {
        return testValidator;
    }

    public FrontDataValidator getFrontDataValidator() {
        return frontDataValidator;
    }
}
