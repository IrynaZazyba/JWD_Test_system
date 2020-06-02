package by.jwd.testsys.logic;

import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.exception.InvalidDeleteActionServiceException;

public interface AdminService {


    void deleteTest(int testId) throws AdminServiceException, InvalidDeleteActionServiceException;
}
