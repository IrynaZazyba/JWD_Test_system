package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.Role;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.ExistsUserException;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.validator.UserValidator;
import by.jwd.testsys.logic.validator.factory.ValidatorFactory;

import java.util.Set;


public class UserServiceImpl implements UserService {

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private UserDAO userDao = daoFactory.getUserDao();

    @Override
    public User userByLoginPassword(String userLogin, String userPassword) throws ServiceException {

        User userByLogin;
        try {
            userByLogin = userDao.getUserByLoginPassword(userLogin, userPassword);
        } catch (DAOException e) {
            throw new ServiceException("Error in userByLoginPassword.", e);
        }
        return userByLogin;
    }

    @Override
    public User registerUser(User user) throws ServiceException, ExistsUserException {
        User userCreated;
        try {
            User userByLogin = userDao.getUserByLogin(user.getLogin());

            if (userByLogin == null) {
                userCreated = userDao.create(user);
            } else {
                throw new ExistsUserException("Such login alreadyExists.");
            }

        } catch (DAOException e) {
            throw new ServiceException("Error in save user.", e);
        }
        return userCreated;
    }

    @Override
    public User userInfoToAccount(int id) throws ServiceException {
        User userFromDB;
        try {
            userFromDB = userDao.getUserById(id);
        } catch (DAOException e) {
            throw new ServiceException("Error in getUserById().", e);
        }
        return userFromDB;
    }

    @Override
    public User editUserInfo(User user) throws ServiceException, InvalidUserDataException {
        User updatedUser;

        ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
        UserValidator userValidator = validatorFactory.getUserValidator();

        Set<String> validateResult = userValidator.validate(user);

        if (validateResult.size() != 0) {
            throw new InvalidUserDataException("Invalid user data.", validateResult);
        }

        try {
            updatedUser = userDao.updateUser(user);
        } catch (DAOException e) {
            throw new ServiceException("Error in editUserInfo().", e);
        }
        return updatedUser;
    }

    @Override
    public Set<User> getStudents() throws ServiceException {
        Set<User> students;
        try {
            students = userDao.getUserByRole(Role.USER);
        } catch (DAOException e) {
            throw new ServiceException("Error in getUserByRole().", e);
        }
        return students;
    }

    @Override
    public Set<User> getUsersWithAssignment(int testId, int testTypeId, boolean isCompleted) throws ServiceException {
        Set<User> users;
        try {
            users = userDao.getUsersWithAssignmentByTestId(testId, testTypeId, isCompleted);
        } catch (DAOException e) {
            throw new ServiceException("Error in getUsersWithAssignment().", e);
        }
        return users;
    }

    @Override
    public Set<String> validateUserData(String login, String password, String firstName, String lastName, String email) {

        ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
        UserValidator userValidator = validatorFactory.getUserValidator();
        User registerUser = new User.Builder()
                .withLogin(login)
                .withPassword(password)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withEmail(email)
                .build();
        return userValidator.validate(registerUser);
    }

}
