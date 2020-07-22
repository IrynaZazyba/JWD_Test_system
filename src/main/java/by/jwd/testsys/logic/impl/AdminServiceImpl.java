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
import by.jwd.testsys.logic.util.LetterBuilder;
import by.jwd.testsys.logic.util.MailSender;
import by.jwd.testsys.logic.validator.FrontDataValidator;
import by.jwd.testsys.logic.validator.TestValidator;
import by.jwd.testsys.logic.validator.factory.ValidatorFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class AdminServiceImpl implements AdminService {

    private final static Logger logger = LogManager.getLogger(AdminServiceImpl.class);

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestDAO testDAO = daoFactory.getTestDao();
    private UserDAO userDAO = daoFactory.getUserDao();
    private QuestionAnswerDAO questionAnswerDAO = daoFactory.getQuestionAnswerDao();

    private ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
    private FrontDataValidator frontDataValidator = validatorFactory.getFrontDataValidator();
    private TestValidator testValidator = validatorFactory.getTestValidator();

    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_SUBJECT = "BeeTesting test assignment";



    @Override
    public void deleteTest(int testId) throws AdminServiceException, InvalidDeleteActionServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(testId)) {
            throw new InvalidUserDataException("Invalid testId in AdminServiceImpl deleteTest() method");
        }

        try {
            int countIncompleteTestAssignment = testDAO.getCountTestAssignment(testId, false);
            if (countIncompleteTestAssignment != 0) {
                throw new InvalidDeleteActionServiceException("Impossible to delete current test. Assignment exists.");
            }

            testDAO.deleteTestById(testId, LocalDateTime.now());
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl deleteTest() method", e);
        }
    }

    @Override
    public int createTest(int typeId, String title, String key, LocalTime duration) throws AdminServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(typeId)) {
            throw new InvalidUserDataException("Invalid typeId in AdminServiceImpl createTest() method");
        }

        if (!testValidator.validateTestTitle(title)) {
            throw new InvalidUserDataException("Invalid title in AdminServiceImpl createTest() method");
        }

        if (!testValidator.validateKey(key)) {
            throw new InvalidUserDataException("Invalid key in AdminServiceImpl createTest() method");
        }


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
    public void updateTestData(int testId, int typeId, String title, String key, LocalTime duration) throws AdminServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(testId)) {
            throw new InvalidUserDataException("Invalid typeId in AdminServiceImpl updateTestData() method");
        }

        if (!frontDataValidator.validateId(typeId)) {
            throw new InvalidUserDataException("Invalid typeId in AdminServiceImpl updateTestData() method");
        }

        if (!testValidator.validateTestTitle(title)) {
            throw new InvalidUserDataException("Invalid title in AdminServiceImpl updateTestData() method");
        }

        if (!testValidator.validateKey(key)) {
            throw new InvalidUserDataException("Invalid key in AdminServiceImpl updateTestData() method");
        }


        Test test = new Test.Builder()
                .withId(testId).withTitle(title).withKey(key).withDuration(duration).withEdited(true).build();
        try {
            testDAO.updateTest(test, typeId);
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl updateTestData() method", e);
        }
    }

    @Override
    public void createQuestionAnswer(String question,
                                     Map<Integer, String> answers,
                                     List<Integer> rightAnswers,
                                     int testId) throws AdminServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(testId) ||
                !testValidator.validateQuestionTitle(question)) {
            throw new InvalidUserDataException("Invalid data in AdminServiceImpl createQuestionAnswer() method");
        }

        validateData(answers);
        validateData(rightAnswers);

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

    private void validateData(List<Integer> rightAnswers) throws InvalidUserDataException {
        for (Integer answerId : rightAnswers) {
            if (!frontDataValidator.validateId(answerId)) {
                throw new InvalidUserDataException("Invalid answerId in AdminServiceImpl createQuestionAnswer() method");
            }
        }
    }

    private void validateData(Map<Integer, String> answers) throws InvalidUserDataException {
        for (Map.Entry<Integer, String> answer : answers.entrySet()) {
            if (!frontDataValidator.validateId(answer.getKey()) ||
                    !testValidator.validateAnswerTitle(answer.getValue())) {
                throw new InvalidUserDataException("Invalid answer data in AdminServiceImpl createQuestionAnswer() method");
            }
        }
    }

    @Override
    public Test receiveTestWithQuestionsAndAnswers(int testId) throws AdminServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(testId)) {
            throw new InvalidUserDataException("Invalid testId in AdminServiceImpl receiveTestWithQuestionsAndAnswers() method");
        }

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
    public void changeTestIsEdited(int testId, boolean isEdited) throws AdminServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(testId)) {
            throw new InvalidUserDataException("Invalid testId in AdminService changeTestIsEdited() method");
        }
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
                                          List<Integer> rightAddedAnswersId) throws AdminServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(questionId) ||
                !testValidator.validateQuestionTitle(question)) {
            throw new InvalidUserDataException("Invalid testId in AdminService changeTestIsEdited() method");
        }
        validateData(rightAddedAnswersId);
        validateData(rightAnswersId);
        validateData(answers);
        validateData(addedAnswers);


        Question updatedQuestion = new Question.Builder().withId(questionId).withQuestion(question).build();
        Set<Answer> answerToUpdate = new HashSet<>();
        Set<Answer> answerToAdd = new HashSet<>();

        List<Integer> answerToDelete = new ArrayList<>();

        if (!deletedAnswers.equals("")) {
            String[] answersIdToDelete = deletedAnswers.split(",");
            for (String s : answersIdToDelete) {
                int deletedAnswerId = Integer.parseInt(s);
                if (!frontDataValidator.validateId(deletedAnswerId)) {
                    throw new InvalidUserDataException("Invalid deletedAnswerId in AdminService changeTestIsEdited() method");
                }
                answerToDelete.add(deletedAnswerId);
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
    public void completeTestCreation(int testId) throws AdminServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(testId)) {
            throw new InvalidUserDataException("Invalid testId in AdminService completeTestCreation() method");
        }

        try {
            testDAO.updateTestIsEdited(testId, false);
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl completeTestCreation() method", e);
        }
    }

    @Override
    public void deleteQuestionWithAnswers(int questionId) throws AdminServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(questionId)) {
            throw new InvalidUserDataException("Invalid questionId in AdminService deleteQuestionWithAnswers() method");
        }

        try {
            questionAnswerDAO.deleteQuestionWithAnswers(questionId, LocalDateTime.now());
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl deleteQuestionWithAnswers() method", e);
        }
    }

    @Override
    public void addTestType(String testTypeTitle) throws AdminServiceException, ExistsTypeAdminServiceException, InvalidUserDataException {

        if (!testValidator.validateTypeTitle(testTypeTitle)) {
            throw new InvalidUserDataException("Invalid testTypeTitle in AdminService addTestType() method");
        }

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
    public void deleteAssignment(int assignmentId) throws AdminServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(assignmentId)) {
            throw new InvalidUserDataException("Invalid assignmentId in AdminService deleteAssignment() method");
        }

        try {
            userDAO.deleteAssignment(assignmentId, LocalDate.now());
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl deleteAssignment() method", e);
        }

    }

    @Override
    public Map<String, Set<User>> assignTestToUsers(int testId, LocalDate deadline, String[] assignUsersId)
            throws InvalidUserDataException, AdminServiceException {

        if (!frontDataValidator.validateId(testId)) {
            throw new InvalidUserDataException("Invalid testId in AdminService assignTestToUsers() method");
        }

        List<Integer> usersId = new ArrayList<>();
        Map<String, Set<User>> assignmentResult = new HashMap<>();
        Set<User> existsAssignment = new HashSet<>();
        Set<User> successAssignment = new HashSet<>();

        if (!testValidator.validateDeadlineDate(deadline)) {
            throw new InvalidUserDataException("Invalid deadline in AdminService assignTestToUsers() method");
        }

        try {
            for (String id : assignUsersId) {
                int userId = Integer.parseInt(id);
                if (!frontDataValidator.validateId(userId)) {
                    throw new InvalidUserDataException("Invalid userId in AdminService assignTestToUsers() method");
                }

                Assignment assignment = userDAO.getUserAssignmentByTestId(userId, testId);
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
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl assignTestToUsers() method", e);
        }

        return assignmentResult;
    }


    @Override
    public boolean sendTestKeyToUsers(Set<User> assignedUsers, int testId, LocalDate deadline) throws AdminServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(testId)) {
            throw new InvalidUserDataException("Invalid testId in AdminService sendTestKeyToUsers() method");
        }

        if (!testValidator.validateDeadlineDate(deadline)) {
            throw new InvalidUserDataException("Invalid deadline in AdminService sendTestKeyToUsers() method");
        }

        Test testInfo;
        try {
            testInfo = testDAO.getTestInfo(testId);
            MailSender sender = MailSender.getInstance();

            for (User user : assignedUsers) {
                String message = LetterBuilder.buildAssignedTestMessage(user.getFirstName(), testInfo.getTitle(), testInfo.getKey(), deadline);
                sender.send(EMAIL_ABOUT_TEST_ASSIGNMENT_SUBJECT, message, user.getEmail());
            }
        } catch (DAOException e) {
            throw new AdminServiceException("DAOException in AdminServiceImpl sendTestKeyToUsers() method", e);
        } catch (FaildSendMailException e) {
            logger.log(Level.ERROR, "Failure in attempt to send email in AdminServiceImpl sendTestKeyToUsers() method", e);
            return false;
        }
        return true;
    }



}
