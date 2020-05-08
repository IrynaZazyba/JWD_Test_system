package by.jwd.testsys.dao;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Result;
import by.jwd.testsys.dao.exception.DAOSqlException;

public interface TestResultDAO {

    Result getTestResult(Assignment assignment) throws DAOSqlException;

    void insertResult(Result result) throws DAOSqlException;

    void updateResult(Result result) throws DAOSqlException;
}
