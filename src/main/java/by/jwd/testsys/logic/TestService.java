package by.jwd.testsys.logic;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.logic.exception.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface TestService {

    List<Type> allTestsType() throws ServiceException;

    Set<Type> typeWithTests() throws ServiceException;

    Set<Test> getUserAssignmentTests(int userId) throws ServiceException;

    Question getQuestionByTestId(Assignment assignment) throws TestServiceException, TimeIsOverServiceException;

    Test getTestInfo(int id) throws TestServiceException;

    Assignment receiveTestAssignment(int testId, int userId) throws TestServiceException;

    void checkPermission(int userId, int testId, String key) throws TestServiceException, InvalidUserDataException, InvalidTestKeyException;

    boolean checkKey(String key, int testId) throws TestServiceException;

//    Integer getTestKey(int testId) throws TestServiceException;

    long calculateTestDuration(Assignment assignment) throws TestServiceException, TimeIsOverServiceException;

    void completeTest(Assignment assignment, LocalDateTime localDateTime) throws TestServiceException;

    LocalDateTime getStartTestTime(int assignmentId) throws TestServiceException;

    LocalTime getTestDuration(int assignmentId) throws TestServiceException;

    Assignment getAssignment(int assignmentId) throws TestServiceException;

    double calculatePercentageOfCorrectAnswers(Assignment assignment, Test test) throws TestServiceException;

    Set<Statistic> getUserTestStatistic(int userId) throws TestServiceException;
}
