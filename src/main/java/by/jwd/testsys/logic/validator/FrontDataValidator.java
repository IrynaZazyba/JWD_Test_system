package by.jwd.testsys.logic.validator;

public interface FrontDataValidator {

    boolean validateId(int id);

    boolean validateTime(String time);

    boolean validateOnlyLatinLetters(String str);

    boolean validateLetters(String str);

    boolean validateStringWithSymbolsAndNumbers(String str);
    boolean validateStringWithDashes(String str);

}
