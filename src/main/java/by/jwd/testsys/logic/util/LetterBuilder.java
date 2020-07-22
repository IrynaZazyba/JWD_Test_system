package by.jwd.testsys.logic.util;

import by.jwd.testsys.bean.User;

import java.time.LocalDate;

public class LetterBuilder {

    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_TEST = "you have been assigned to the test ";
    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_KEY = "Key: ";
    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_DEADLINE = "Deadline: ";
    private final static String EMAIL_REGARDS = "Best regards,\n \"Bee testing\" team.";
    private static final String COMMA = ",";
    private static final String QUOTATION_MARK = "\"";
    private static final String NEW_LINE = "\n";
    private static final String DOT = ".";

    private final static String EMAIL_ACTIVATION_FOLLOW_LINK = "Follow the link to activate your account:";
    private final static String EMAIL_ACTIVATION_REGISTRATION = "you have registered on the BeeTesting portal.";



    public static String buildActivationLetter(String link, User user) {
        StringBuilder message = new StringBuilder();
        message.append(user.getFirstName())
                .append(COMMA)
                .append(EMAIL_ACTIVATION_REGISTRATION).append(NEW_LINE)
                .append(EMAIL_ACTIVATION_FOLLOW_LINK).append(NEW_LINE)
                .append(link).append(NEW_LINE);
        message.append(EMAIL_REGARDS);
        return message.toString();
    }


    public static String buildAssignedTestMessage(String userName, String testName, String key, LocalDate deadline) {
        StringBuilder message = new StringBuilder();
        message.append(userName)
                .append(COMMA)
                .append(EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_TEST)
                .append(QUOTATION_MARK).append(testName).append(QUOTATION_MARK).append(DOT + NEW_LINE);
        message.append(EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_DEADLINE).append(deadline).append(DOT + NEW_LINE);
        if (key != null) {
            message.append(EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_KEY).append(key).append(DOT + NEW_LINE);
        }
        message.append(EMAIL_REGARDS);
        return message.toString();
    }
}
