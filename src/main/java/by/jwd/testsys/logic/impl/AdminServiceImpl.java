package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.Question;
import by.jwd.testsys.dao.TestDAO;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.exception.InvalidDeleteActionServiceException;

import java.time.LocalDateTime;
import java.util.Set;

public class AdminServiceImpl implements AdminService {

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestDAO testDAO = daoFactory.getTestDao();


    public Set<Question> getAllQuestionByTestId(int testId) {

        Set<Question> testQuestions = null;

        return testQuestions;
    }

    @Override
    public void deleteTest(int testId) throws AdminServiceException, InvalidDeleteActionServiceException {
        try {
            int countIncompleteTestAssignment = testDAO.getCountIncompleteTestAssignment(testId);
            if (countIncompleteTestAssignment != 0) {
                throw new InvalidDeleteActionServiceException("Impossible to delete current test. Assignment exists.");
            }

            testDAO.deleteTestById(testId, LocalDateTime.now());
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DB problem");
        }
    }


}
