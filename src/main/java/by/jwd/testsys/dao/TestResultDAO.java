package by.jwd.testsys.dao;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Result;
import by.jwd.testsys.bean.Statistic;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;

public interface TestResultDAO {

    Result getTestResult(Assignment assignment) throws DAOException;

    Timestamp getTestStartDateTime(int assignmentId) throws DAOException;

    void insertResult(Result result) throws DAOException;

    void updateResult(Result result) throws DAOException;

    Set<Statistic> getUserTestStatistic(int userId) throws DAOException;

    Set<Result> getTestResult(int typeId, int testId, int userId, LocalDate date) throws DAOException;

}
