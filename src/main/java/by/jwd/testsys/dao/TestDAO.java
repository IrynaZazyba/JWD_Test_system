package by.jwd.testsys.dao;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TestDAO {

    Question getQuestionByTestId(int id, int assignment_id) throws DAOException;

    Set<Answer> getAnswersByQuestionId(int id) throws DAOSqlException;

    Test getTestInfo(int id) throws DAOException;

    void insertResult(Result result) throws DAOSqlException;

    void updateResult(Result result) throws DAOSqlException;

   // Result getTestResultByAssignmentId(int assignmentId) throws DAOSqlException;

    void writeAssignment(int assignmentId, boolean isCompleted) throws DAOSqlException;

    Map<Integer, List<Integer>> getRightAnswersToQuestionByTestId(int testId) throws DAOSqlException;

    Integer getTestKey(int testId) throws DAOSqlException;

    Timestamp getTestStartDateTime(int assignmentId) throws DAOSqlException;

    LocalTime getTestDuration(int assignmentId) throws DAOSqlException;
}
