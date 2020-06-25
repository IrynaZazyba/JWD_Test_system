package by.jwd.testsys.dao.factory;

import by.jwd.testsys.dao.*;

public interface DAOFactory {

    UserDAO getUserDao();

    TestDAO getTestDao();

    TestLogDAO getTestLogDao();

    TestResultDAO getTestResultDao();

    QuestionAnswerDAO getQuestionAnswerDao();
}
