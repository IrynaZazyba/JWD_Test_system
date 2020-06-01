package by.jwd.testsys.dao;

import by.jwd.testsys.bean.Result;
import by.jwd.testsys.bean.TestLog;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;

import java.time.LocalDate;
import java.util.Set;

public interface TestLogDAO {

    void writeAnswerLog(int questionLogId, Set<Integer> answers) throws DAOException;

    Integer writeQuestionLog(int idQuestion, int idAssignment) throws DAOSqlException;

    TestLog getTestLog(int assignmentId) throws DAOSqlException;

    Set<Result> getTestResult(int typeId, int testId, int userId, LocalDate date) throws DAOSqlException;
}
