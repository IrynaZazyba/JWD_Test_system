package by.jwd.testsys.logic.service.factory;

import by.jwd.testsys.logic.service.TestService;
import by.jwd.testsys.logic.service.UserService;
import by.jwd.testsys.logic.service.impl.TestServiceImpl;
import by.jwd.testsys.logic.service.impl.UserServiceImpl;

public final class ServiceFactory {

    private static final ServiceFactory instance = new ServiceFactory();

    private final TestService testService = new TestServiceImpl();
    private final UserService userService = new UserServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public TestService getTestService() {
        return testService;
    }

    public UserService getUserService() {
        return userService;
    }
}
