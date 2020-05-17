package by.jwd.testsys.logic.validator.factory;

        import by.jwd.testsys.logic.validator.TestValidator;
        import by.jwd.testsys.logic.validator.UserValidator;
        import by.jwd.testsys.logic.validator.impl.TestValidatorImpl;
        import by.jwd.testsys.logic.validator.impl.UserValidatorImpl;

public class ValidatorFactory {

    private static ValidatorFactory instance = new ValidatorFactory();
    private static UserValidator userValidator = new UserValidatorImpl();
    private static TestValidator testValidator = new TestValidatorImpl();

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
}
