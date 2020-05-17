package by.jwd.testsys.logic.validator.impl;

import by.jwd.testsys.logic.validator.TestValidator;
import by.jwd.testsys.logic.validator.util.InvalidParam;

public class TestValidatorImpl implements TestValidator {

    private static final String TEST_KEY_PATTERN = "[a-zA-Z0-9]{4,7}$";

    public TestValidatorImpl() {
    }

    @Override
    public String validate(String key) {

        if (!key.matches(TEST_KEY_PATTERN)) {
            return InvalidParam.INVALID_TEST_KEY.toString();
        }

        return null;
    }


}
