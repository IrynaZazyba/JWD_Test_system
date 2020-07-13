package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.ExistsUserException;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.UserServiceException;
import by.jwd.testsys.logic.util.HashStringHelper;
import by.jwd.testsys.logic.validator.FrontDataValidator;
import by.jwd.testsys.logic.validator.UserValidator;
import by.jwd.testsys.logic.validator.factory.ValidatorFactory;
import by.jwd.testsys.logic.validator.util.InvalidParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;


public class UserServiceImpl implements UserService {

    private final static Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private UserDAO userDao = daoFactory.getUserDao();


    private ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
    private FrontDataValidator frontDataValidator = validatorFactory.getFrontDataValidator();
    private UserValidator userValidator = validatorFactory.getUserValidator();


    @Override
    public User checkUserCredentials(String userLogin, String userPassword) throws UserServiceException {

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
            throw new UserServiceException("Exception in UserServiceImpl method checkUserCredentials().", e);
        }
        return userByLogin;
    }

    @Override
    public synchronized User registerUser(User user) throws UserServiceException, ExistsUserException, InvalidUserDataException {

        Set<String> validateResult = userValidator.validate(user);

        if (validateResult.size() != 0) {
            throw new InvalidUserDataException("Invalid user data.", validateResult);
        }

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
            throw new UserServiceException("Exception in UserServiceImpl method registerUser().", e);
        }
        return userCreated;
    }

    @Override
    public User userInfoToAccount(int id) throws UserServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(id)) {
            throw new InvalidUserDataException("Invalid userId in UserServiceImpl userInfoToAccount() method");
        }

        try {
            return userDao.getUserById(id);
        } catch (DAOException e) {
            throw new UserServiceException("Exception in UserServiceImpl method userInfoToAccount().", e);
        }
    }

    @Override
    public User editUserInfo(User user) throws UserServiceException, InvalidUserDataException, ExistsUserException {

        Set<String> validateResult = userValidator.validate(user.getLogin(),user.getFirstName(),user.getLastName(),user.getEmail());

        if (validateResult.size() != 0) {
            throw new InvalidUserDataException("Invalid user data.", validateResult);
        }

        try {
            User userById = userDao.getUserById(user.getId());
            if(!userById.getLogin().equals(user.getLogin())){
                User userByLogin = userDao.getUserByLogin(user.getLogin());
                if (userByLogin != null) {
                    throw new ExistsUserException("Such login alreadyExists.");
                }
            }

            return userDao.updateUser(user);
        } catch (DAOException e) {
            throw new UserServiceException("Exception in UserServiceImpl method editUserInfo().", e);
        }
    }

    @Override
    public Set<User> getUsers() throws UserServiceException {
        try {
            return userDao.getAll();
        } catch (DAOException e) {
            throw new UserServiceException("Exception in UserServiceImpl method getUsers().", e);
        }
    }

    @Override
    public Set<User> getUsersWithAssignment(int testId, int testTypeId, boolean isCompleted) throws UserServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(testId) ||
                !frontDataValidator.validateId(testTypeId)) {
            throw new InvalidUserDataException("Invalid userId in UserServiceImpl getUsersWithAssignment() method");
        }

        Set<User> users;
        try {
            users = userDao.getUsersWithAssignmentByTestId(testId, testTypeId, isCompleted);
        } catch (DAOException e) {
            throw new UserServiceException("Exception in UserServiceImpl method getUsersWithAssignment().", e);
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
    public void changePassword(int userId, String oldPassword, String newPassword) throws UserServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(userId)) {
            throw new InvalidUserDataException("Invalid userId in UserServiceImpl changePassword() method");
        }

        ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
        UserValidator userValidator = validatorFactory.getUserValidator();

//todo проверить что старый пароль?
        Set<String> validationResult;
        if (!userValidator.validatePassword(newPassword)) {
            validationResult = new HashSet<>();
            validationResult.add(InvalidParam.INVALID_PASSWORD.toString());
            throw new InvalidUserDataException("Invalid user data.", validationResult);
        }

        try {
            String password = userDao.getPassword(userId);
            boolean isValid = HashStringHelper.checkPassword(oldPassword, password);
            if (!isValid) {
                validationResult = new HashSet<>();
                validationResult.add(InvalidParam.PASSWORD_MISMATCH.toString());
                throw new InvalidUserDataException("Invalid user data.", validationResult);
            }

            String newHashedPassword = HashStringHelper.hashPassword(newPassword);
            userDao.updateUserPassword(userId, newHashedPassword);

        } catch (DAOException e) {
            throw new UserServiceException("Exception in UserServiceImpl method changePassword().", e);
        }


    }

}
