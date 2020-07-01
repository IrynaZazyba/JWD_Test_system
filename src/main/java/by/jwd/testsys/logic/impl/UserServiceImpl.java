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
import by.jwd.testsys.logic.util.HashStringHelper;
import by.jwd.testsys.logic.validator.UserValidator;
import by.jwd.testsys.logic.validator.factory.ValidatorFactory;
import by.jwd.testsys.logic.validator.util.InvalidParam;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;


public class UserServiceImpl implements UserService {

    private final static Logger logger = LogManager.getLogger();

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private UserDAO userDao = daoFactory.getUserDao();

    @Override
    public User checkUserCredentials(String userLogin, String userPassword) throws ServiceException {

        User userByLogin;
        try {
            userByLogin = userDao.getUserByLogin(userLogin);
            boolean checkResult = false;
            if (userByLogin != null) {
                checkResult = HashStringHelper.checkPassword(userPassword, userByLogin.getPassword());
            }

            if (userByLogin == null || !checkResult) {
                return new User();
            }
        } catch (DAOException e) {
            throw new ServiceException("Exception in UserServiceImpl method checkUserCredentials().", e);
        }
        return userByLogin;
    }

    @Override
    public User registerUser(User user) throws ServiceException, ExistsUserException {
        User userCreated;
        try {
            User userByLogin = userDao.getUserByLogin(user.getLogin());

            if (userByLogin == null) {
                String password = user.getPassword();
                String hashedPassword = HashStringHelper.hashPassword(password);
                user.setPassword(hashedPassword);
                userCreated = userDao.create(user);
            } else {
                throw new ExistsUserException("Such login alreadyExists.");
            }

        } catch (DAOException e) {
            throw new ServiceException("Exception in UserServiceImpl method registerUser().", e);
        }
        return userCreated;
    }

    @Override
    public User userInfoToAccount(int id) throws ServiceException {
        User userFromDB;
        try {
            userFromDB = userDao.getUserById(id);
        } catch (DAOException e) {
            throw new ServiceException("Exception in UserServiceImpl method userInfoToAccount().", e);
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
            throw new ServiceException("Exception in UserServiceImpl method editUserInfo().", e);
        }
        return updatedUser;
    }

    @Override
    public Set<User> getStudents() throws ServiceException {
        Set<User> students;
        try {
            students = userDao.getUserByRole(Role.USER);
        } catch (DAOException e) {
            throw new ServiceException("Exception in UserServiceImpl method getStudents().", e);
        }
        return students;
    }

    @Override
    public Set<User> getUsersWithAssignment(int testId, int testTypeId, boolean isCompleted) throws ServiceException {
        Set<User> users;
        try {
            users = userDao.getUsersWithAssignmentByTestId(testId, testTypeId, isCompleted);
        } catch (DAOException e) {
            throw new ServiceException("Exception in UserServiceImpl method getUsersWithAssignment().", e);
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

    @Override
    public void changePassword(int userId, String oldPassword, String newPassword) throws ServiceException {

        ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
        UserValidator userValidator = validatorFactory.getUserValidator();

//todo проверить что старый пароль?
        Set<String> validationResult;
        if (!userValidator.validatePassword(newPassword)) {
            logger.log(Level.ERROR, "Invalid password");
            validationResult = new HashSet<>();
            validationResult.add(InvalidParam.INVALID_PASSWORD.toString());
            throw new InvalidUserDataException("Invalid user data.", validationResult);
        }

        try {
            User userById = userDao.getUserById(userId);
            boolean isValid = HashStringHelper.checkPassword(oldPassword, userById.getPassword());
            if (!isValid) {
                logger.log(Level.ERROR, "Old password invalid");
                validationResult = new HashSet<>();
                validationResult.add(InvalidParam.PASSWORD_MISMATCH.toString());
                throw new InvalidUserDataException("Invalid user data.");
            }

            String newHashedPassword = HashStringHelper.hashPassword(newPassword);
            userDao.updateUserPassword(userId, newHashedPassword);

        } catch (DAOException e) {
            //todo UserServiceException
            throw new ServiceException("Exception in UserServiceImpl method changePassword().", e);
        }


    }

}
