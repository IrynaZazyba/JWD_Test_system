package by.jwd.testsys.logic;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.logic.exception.ImpossibleTestDataServiceException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.exception.TestServiceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TestService {

    List<Type> allTestsType() throws ServiceException;

    Set<Type> typeWithTests() throws ServiceException;

    Question getQuestionByTestId(Assignment assignment) throws TestServiceException, ImpossibleTestDataServiceException;

    Test getTestInfo(int id) throws TestServiceException;

    Assignment exeTest(int testId, int userId,String key) throws TestServiceException;

    Result getResult(int assignmentId) throws TestServiceException;

    boolean checkKey(Integer key, int testId) throws TestServiceException;

    LocalDateTime getStartTestTime(int assignmentId);
}
