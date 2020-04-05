package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.dao.TestTypeDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.TestService;

import java.util.List;
import java.util.Set;

public class TestServiceImpl implements TestService {

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestTypeDAO typeDAO = daoFactory.getTypeDao();

    @Override
    public List<Type> allTestsType() throws ServiceException {
        List<Type> testsType = null;
        try {
            testsType = typeDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Error in TestService allTestsType() method", e);
        }
        return testsType;
    }

    @Override
    public Set<Type> typeWithTests() throws ServiceException {
        Set<Type> testsType = null;

        try {
            testsType = typeDAO.getTypeWithTests();
        } catch (DAOException e) {
            throw new ServiceException("Error in TestService typeWithTests() method", e);
        }
        return testsType;
    }
}
