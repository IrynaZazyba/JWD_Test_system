package by.jwd.testsys.logic.validator;

import java.time.LocalDate;

public interface TestValidator {

    boolean validateKey(String key);

    boolean validateTestTitle(String testTitle);

    boolean validateTypeTitle(String typeTitle);

    boolean validateDeadlineDate(LocalDate deadline);
}
