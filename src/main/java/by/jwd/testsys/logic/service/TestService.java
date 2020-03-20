package by.jwd.testsys.logic.service;

import by.jwd.testsys.bean.Type;

import java.util.List;
import java.util.Set;

public interface TestService {

    List<Type> getAllTestsType() throws ServiceException;
    Set<Type> getTypeWithTests();


    }
