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

    Question getQuestionByTestId(Assignment assignment) throws TestServiceException, ImpossibleTestDataServiceException, TimeIsOverServiceException;

    Test getTestInfo(int id) throws TestServiceException;

    Assignment checkPermission(int testId, int userId, String key) throws TestServiceException, InvalidKeyException;

    boolean checkKey(Integer key, int testId) throws TestServiceException;

    Integer getTestKey(int testId) throws TestServiceException;

    LocalDateTime getStartTestTime(int assignmentId) throws TestServiceException;

    LocalTime getTestDuration(int assignmentId) throws TestServiceException;

    Assignment getAssignment(int assignmentId) throws TestServiceException;
}
