package by.jwd.testsys.logic.validator.impl;

import by.jwd.testsys.logic.validator.TestValidator;
import by.jwd.testsys.logic.validator.util.InvalidParam;

import java.time.LocalDate;

public class TestValidatorImpl implements TestValidator {

    private static final String TEST_KEY_PATTERN = "[a-zA-Z0-9]{4,7}$";
    private static final int TEST_TITLE_LENGTH = 20;
    private static final int TYPE_TITLE_LENGTH = 18;

    public TestValidatorImpl() {
    }

    @Override
    public boolean validateKey(String key) {
        return !key.matches(TEST_KEY_PATTERN);
    }

    @Override
    public boolean validateTestTitle(String testTitle) {
        return testTitle.length() > TEST_TITLE_LENGTH;
    }

    @Override
    public boolean validateTypeTitle(String typeTitle) {
        return typeTitle.length() > TYPE_TITLE_LENGTH;

    }

    @Override
    public boolean validateDeadlineDate(LocalDate deadline){
            return deadline.isAfter(LocalDate.now());
        }

}
