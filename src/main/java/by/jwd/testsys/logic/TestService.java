package by.jwd.testsys.logic;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.logic.exception.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TestService {

    List<Type> allTestsType() throws ServiceException;

    List<Type> typeWithTests(int userId) throws ServiceException;

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

    Result getResultInfo(Assignment assignment, Test test) throws TestServiceException;

    Set<Statistic> getUserTestStatistic(int userId) throws TestServiceException;

    Result checkResult(int userId, int testId) throws TestServiceException;

    Set<Test> getNotEditedTestByTypeId(int typeId) throws TestServiceException;

    Set<Test> getAllTestByTypeId(int typeId, int currentPage) throws TestServiceException;

    Map<String,Set<User>> assignTestToUsers(int testId, LocalDate deadline, String[] assignUsersId) throws ServiceException, DateOutOfRangeException;

    void deleteAssignment(int assignment_id) throws ServiceException;

    int receiveCountTestPages(int typeId) throws TestServiceException;
}
