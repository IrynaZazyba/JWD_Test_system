package by.jwd.testsys.dao.factory;

import by.jwd.testsys.dao.*;

public interface DAOFactory {

    UserDAO getUserDao();

    TestTypeDAO getTypeDao();

    TestDAO getTestDao();

    TestLogDAO getTestLogDao();

    TestResultDAO getTestResultDao();

    QuestionAnswerDAO getQuestionAnswerDao();
}
