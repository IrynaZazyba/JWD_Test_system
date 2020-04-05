package by.jwd.testsys.logic.factory;

import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.impl.TestServiceImpl;
import by.jwd.testsys.logic.impl.UserServiceImpl;

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
