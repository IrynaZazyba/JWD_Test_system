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

    List<Type> allTestsType() throws TestServiceException;

    Set<Test> getUserAssignmentTests(int userId) throws TestServiceException, InvalidUserDataException;

    Question getQuestionByTestId(Assignment assignment) throws TestServiceException, TimeIsOverServiceException;

    Test getTestInfo(int id) throws TestServiceException, InvalidUserDataException;

    Assignment checkTestAssignment(int testId, int userId) throws TestServiceException, InvalidUserDataException;

    void checkPermission(int userId, int testId, String key) throws TestServiceException, InvalidUserDataException, InvalidTestKeyException;

    Set<Result> receiveResultData(int typeId, int testId, int userId, LocalDate date) throws TestLogServiceException, InvalidUserDataException;

    long calculateTestDuration(Assignment assignment) throws TestServiceException, TimeIsOverServiceException;

    void completeTest(Assignment assignment, LocalDateTime localDateTime) throws TestServiceException;

    Result getResultInfo(Assignment assignment, Test test) throws TestServiceException;

    Set<Statistic> getUserTestStatistic(int userId) throws TestServiceException, InvalidUserDataException;

    Assignment getAssignment(int assignmentId) throws TestServiceException, InvalidUserDataException;

    Result checkResult(int userId, int testId) throws TestServiceException, InvalidUserDataException;

    Set<Test> getNotEditedTestByTypeId(int typeId) throws TestServiceException, InvalidUserDataException;

    Set<Test> getTestByTypeId(int typeId, int currentPage, int recordsPerPage) throws TestServiceException, InvalidUserDataException;

    Set<Test> getTestsPermittedForUser(int typeId, int currentPage, int recordsPerPage) throws TestServiceException, InvalidUserDataException;

    int receiveNumberTestPages(int testId, int recordsPerPage, boolean isEdited, boolean isExistsKey) throws TestServiceException, InvalidUserDataException;
}
