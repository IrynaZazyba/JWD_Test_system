package by.jwd.testsys.logic.service;

import by.jwd.testsys.bean.Type;

import java.util.List;

public interface TestService {

    List<Type> getAllTestsType() throws ServiceException;

}
