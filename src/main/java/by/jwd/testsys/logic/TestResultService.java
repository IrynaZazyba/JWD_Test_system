package by.jwd.testsys.logic;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Result;
import by.jwd.testsys.bean.Test;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.logic.exception.TestServiceException;

import java.time.LocalDateTime;

public interface TestResultService {


    Result getResult(Assignment assignment) throws TestServiceException;

    Result calculateResult(Assignment assignment, Result result, LocalDateTime localDateTime) throws TestServiceException;

    void checkResult(Assignment assignment, String key) throws TestServiceException, DAOSqlException;

    void writeResultToDB(Result result) throws DAOSqlException;

    double calculatePercentageOfCorrectAnswers(Result result, Test test);
}
