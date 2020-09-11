package by.jwd.testsys.logic.validator.impl;

import by.jwd.testsys.logic.validator.FrontDataValidator;

public class FrontDataValidatorImpl implements FrontDataValidator {

    private static final String LETTERS_WITH_DASHES = "[a-zA-Zа-яА-Я-]";
    private static final String ONLY_LATIN_LETTER = "[a-zA-Z]";
    private static final String ONLY_LETTER = "[a-zA-Zа-яА-Я]";
    private static final String ANY_SYMBOLS = ".+";
    private static final String TIME_PATTERN = "\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}";

    public FrontDataValidatorImpl() {
    }

    @Override
    public boolean validateId(int id) {
        return id>=0;
    }

    @Override
    public boolean validatePositiveNumber(int id) {
        return id>0;
    }


    @Override
    public boolean validateTime(String time) {
        return time.matches(TIME_PATTERN);
    }

    @Override
    public boolean validateOnlyLatinLetters(String str) {
        return str.matches(ONLY_LATIN_LETTER);
    }

    @Override
    public boolean validateLetters(String str) {
        return str.matches(ONLY_LETTER);
    }

    @Override
    public boolean validateStringWithSymbolsAndNumbers(String str) {
        return str.matches(ANY_SYMBOLS);
    }

    @Override
    public boolean validateStringWithDashes(String str) {
        return str.matches(LETTERS_WITH_DASHES);
    }


}
