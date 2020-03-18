package by.jwd.testsys.logic.service.impl;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.dao.TestTypeDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.service.ServiceException;
import by.jwd.testsys.logic.service.TestService;

import java.util.List;

public class TestServiceImpl implements TestService {

    private DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestTypeDAO typeDAO = daoFactory.getTypeDao();

    public List<Type> getAllTestsType() throws ServiceException {
        List<Type> testsType = null;
        try {
            testsType = typeDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Error in TestService from DAO", e);
        }
        return testsType;
    }


}
