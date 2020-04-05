package by.jwd.testsys.logic;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.logic.exception.ServiceException;

import java.util.List;
import java.util.Set;

public interface TestService {

    List<Type> allTestsType() throws ServiceException;
    Set<Type> typeWithTests() throws ServiceException;

    }
