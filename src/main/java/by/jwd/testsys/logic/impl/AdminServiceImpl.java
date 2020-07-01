package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.QuestionAnswerDAO;
import by.jwd.testsys.dao.TestDAO;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.*;
import by.jwd.testsys.logic.util.SslSender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class AdminServiceImpl implements AdminService {

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestDAO testDAO = daoFactory.getTestDao();
    private UserDAO userDAO=daoFactory.getUserDao();
    private QuestionAnswerDAO questionAnswerDAO = daoFactory.getQuestionAnswerDao();

    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_SUBJECT = "BeeTesting test assignment";
    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_TEST = "you have been assigned to the test ";
    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_KEY = "Key: ";
    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_DEADLINE = "Deadline: ";
    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_REGARDS = "Best regards,\n \"Bee testing\" team.";
    private static final String COMMA=",";
    private static final String QUOTATION_MARK="\"";
    private static final String NEW_LINE="\n";
    private static final String DOT=".";


    @Override
    public void deleteTest(int testId) throws AdminServiceException, InvalidDeleteActionServiceException {
        try {
            int countIncompleteTestAssignment = testDAO.getCountTestAssignment(testId,false);
            if (countIncompleteTestAssignment != 0) {
                throw new InvalidDeleteActionServiceException("Impossible to delete current test. Assignment exists.");
            }

            testDAO.deleteTestById(testId, LocalDateTime.now());
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl deleteTest() method", e);
        }
    }

    @Override
    public int createTest(int typeId, String title, String key, LocalTime duration) throws AdminServiceException {
        int generatedTestId;
        if (key.equals("")) {
            key = null;
        }
        Test test = new Test.Builder().withTitle(title).withKey(key).withDuration(duration).withEdited(true).build();
        try {
            generatedTestId = testDAO.saveTest(test, typeId);
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl createTest() method", e);
        }
        return generatedTestId;
    }

    @Override
    public void updateTestData(int testId, int typeId, String title, String key, LocalTime duration) throws AdminServiceException {
        Test test = new Test.Builder()
                .withId(testId).withTitle(title).withKey(key).withDuration(duration).withEdited(true).build();
        try {
            testDAO.updateTest(test, typeId);
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl updateTestData() method", e);
        }
    }

    @Override
    public void createQuestionAnswer(String question, Map<Integer, String> answers, List<Integer> rightAnswers, int testId) throws AdminServiceException {

        Question createdQuestion = new Question.Builder().withQuestion(question).build();
        Set<Answer> createdAnswers = new HashSet<>();
        try {

            answers.forEach((k, v) -> {
                Answer answer = new Answer.Builder().withAnswer(v).withResult(false).build();
                rightAnswers.forEach(rightAnswer ->
                {
                    if (k.equals(rightAnswer)) {
                        answer.setResult(true);
                    }
                });
                createdAnswers.add(answer);
            });
            createdQuestion.setAnswers(createdAnswers);
            questionAnswerDAO.saveQuestionWithAnswers(createdQuestion, testId);
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl createQuestionAnswer() method", e);
        }


    }

    @Override
    public Test receiveTestWithQuestionsAndAnswers(int testId) throws AdminServiceException {
        Test testData;
        try {
            testData = testDAO.getTestInfo(testId);
            Set<Question> questions = questionAnswerDAO.getQuestionsWithAnswersByTestId(testId);
            testData.setQuestions(questions);
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl receiveTestWithQuestionsAndAnswers() method", e);
        }

        return testData;
    }

    @Override
    public void changeTestIsEdited(int testId, boolean isEdited) throws AdminServiceException {
        try {
            testDAO.updateTestIsEdited(testId, isEdited);
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl changeTestIsEdited() method", e);
        }
    }


    @Override
    public void updateQuestionWithAnswers(int questionId,
                                          String question,
                                          String deletedAnswers,
                                          Map<Integer, String> answers,
                                          Map<Integer, String> addedAnswers,
                                          List<Integer> rightAnswersId,
                                          List<Integer> rightAddedAnswersId) throws AdminServiceException {

        Question updatedQuestion = new Question.Builder().withId(questionId).withQuestion(question).build();
        Set<Answer> answerToUpdate = new HashSet<>();
        Set<Answer> answerToAdd = new HashSet<>();

        List<Integer> answerToDelete = new ArrayList<>();

        if (!deletedAnswers.equals("")) {
            String[] answersIdToDelete = deletedAnswers.split(",");
            for (String s : answersIdToDelete) {
                answerToDelete.add(Integer.parseInt(s));
            }
        }

        answers.forEach((k, v) -> {
            Answer answer = new Answer.Builder().withId(k).withAnswer(v).build();
            rightAnswersId.forEach(value -> {
                if (value.equals(k)) {
                    answer.setResult(true);
                }
            });
            answerToUpdate.add(answer);
        });

        addedAnswers.forEach((k, v) -> {
            Answer answer = new Answer.Builder().withAnswer(v).build();
            rightAddedAnswersId.forEach(value -> {
                if (value.equals(k)) {
                    answer.setResult(true);
                }
            });
            answerToAdd.add(answer);
        });

        try {
            questionAnswerDAO.updateQuestionWithAnswersByQuestionId(updatedQuestion, answerToUpdate, answerToAdd, answerToDelete, LocalDate.now());
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl updateQuestionWithAnswers() method", e);
        }
    }

    @Override
    public void completeTestCreation(int testID) throws AdminServiceException {

        try {
            testDAO.updateTestIsEdited(testID, false);
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl completeTestCreation() method", e);
        }
    }

    @Override
    public void deleteQuestionWithAnswers(int questionId) throws AdminServiceException {

        try {
            questionAnswerDAO.deleteQuestionWithAnswers(questionId, LocalDateTime.now());
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl deleteQuestionWithAnswers() method", e);
        }
    }

    @Override
    public void addTestType(String testTypeTitle) throws AdminServiceException, ExistsTypeAdminServiceException {

        try {
            Type typeByTitle = testDAO.getTypeByTitle(testTypeTitle);
            if (typeByTitle.getTitle() != null) {
                throw new ExistsTypeAdminServiceException("Exists test type exception in AdminService addTestType() method");
            }

            testDAO.saveTestType(testTypeTitle);

        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl addTestType() method", e);
        }
    }

    @Override
    public void deleteAssignment(int assignment_id) throws ServiceException {
        try {
            userDAO.deleteAssignment(assignment_id, LocalDate.now());
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl deleteAssignment() method", e);
        }

    }

    @Override
    public Map<String, Set<User>> assignTestToUsers(int testId, LocalDate deadline, String[] assignUsersId) throws ServiceException, DateOutOfRangeException {

        List<Integer> usersId = new ArrayList<>();
        Map<String, Set<User>> assignmentResult = new HashMap<>();
        Set<User> existsAssignment = new HashSet<>();
        Set<User> successAssignment = new HashSet<>();

        if (!isWithinRange(deadline)) {
            throw new DateOutOfRangeException("Deadline date isn't valid");
        }

        try {
            for (String id : assignUsersId) {
                int userId = Integer.parseInt(id);
                Assignment assignment =userDAO.getUserAssignmentByTestId(userId, testId);
                if (assignment == null) {
                    usersId.add(userId);
                    User user = userDAO.getUserById(userId);
                    successAssignment.add(user);
                } else {
                    User userById = userDAO.getUserById(userId);
                    existsAssignment.add(userById);
                }
            }

            assignmentResult.put("successAssignment", successAssignment);
            assignmentResult.put("existsAssignment", existsAssignment);
            userDAO.insertAssignment(LocalDate.now(), deadline, testId, usersId);
            sendTestKeyToUsers(successAssignment, testId, deadline);
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl assignTestToUsers() method", e);
        }

        return assignmentResult;
    }


    private boolean isWithinRange(LocalDate deadline) {
        return deadline.isAfter(LocalDate.now());
    }

    private void sendTestKeyToUsers(Set<User> assignedUsers, int testId, LocalDate deadline) throws DAOException {

            Test testInfo = testDAO.getTestInfo(testId);
            SslSender sender = SslSender.getInstance();

            for (User user : assignedUsers) {
                String message = buildEmailMessage(user.getFirstName(), testInfo.getTitle(), testInfo.getKey(), deadline);
                sender.send(EMAIL_ABOUT_TEST_ASSIGNMENT_SUBJECT, message, user.getEmail());
            }

    }

    private String buildEmailMessage(String userName, String testName, String key, LocalDate deadline) {
        StringBuilder message = new StringBuilder();
        message.append(userName)
                .append(COMMA)
                .append(EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_TEST)
                .append(QUOTATION_MARK).append(testName).append(QUOTATION_MARK).append(DOT+NEW_LINE);
        message.append(EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_DEADLINE).append(deadline).append(DOT+NEW_LINE);
        if (key != null) {
            message.append(EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_KEY).append(key).append(DOT+NEW_LINE);
        }
        message.append(EMAIL_ABOUT_TEST_ASSIGNMENT_REGARDS);
        return message.toString();
    }

}
