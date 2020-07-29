package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.*;
import by.jwd.testsys.logic.util.HashStringHelper;
import by.jwd.testsys.logic.util.LetterBuilder;
import by.jwd.testsys.logic.util.MailSender;
import by.jwd.testsys.logic.validator.FrontDataValidator;
import by.jwd.testsys.logic.validator.UserValidator;
import by.jwd.testsys.logic.validator.factory.ValidatorFactory;
import by.jwd.testsys.logic.validator.util.InvalidParam;
import lombok.Builder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;


public class UserServiceImpl implements UserService {

    private static Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private UserDAO userDao = daoFactory.getUserDao();


    private ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
    private FrontDataValidator frontDataValidator = validatorFactory.getFrontDataValidator();
    private UserValidator userValidator = validatorFactory.getUserValidator();

    private final static String EMAIL_ACTIVATION_LETTER_SUBJECT = "BeeTesting activation letter";
    private final static String MARK_EQUAL = "=";
    private final static String AMPERSAND = "&";


    @Override
    public User checkUserCredentials(String userLogin, String userPassword) throws UserServiceException {

        User userByLogin;
        try {
            userByLogin = userDao.getUserByLogin(userLogin);
            boolean checkResult = false;
            if (userByLogin != null) {
                checkResult = HashStringHelper.checkString(userPassword, userByLogin.getPassword());
            }

            if (userByLogin == null || !checkResult) {
                return new User();
            }
        } catch (DAOException e) {
            throw new UserServiceException("Exception in UserServiceImpl method checkUserCredentials().", e);
        }
        return userByLogin;
    }

    /**
     * Registers a user, after that an account confirmation is required
     *
     * @param user user data
     * @return registered user
     * @throws UserServiceException     in case of error getting data from the database
     * @throws ExistsUserException      if a user with the same login already exists
     * @throws InvalidUserDataException in case if the parameters passed to the method
     *                                  are not valid
     * @throws ExistsEmailException     if a user with the same email already exists
     */
    @Override
    public synchronized User registerUser(User user) throws UserServiceException, ExistsUserException,
            InvalidUserDataException, ExistsEmailException {

        Set<String> validateResult = userValidator.validate(user);

        if (validateResult.size() != 0) {
            throw new InvalidUserDataException("Invalid user data.", validateResult);
        }

        User userCreated;
        try {
            int userIdByEmail = userDao.getUserIdByEmail(user.getEmail());
            if (userIdByEmail != 0) {
                throw new ExistsEmailException("Exists email");
            }

            User userByLogin = userDao.getUserByLogin(user.getLogin());

            if (userByLogin == null) {
                String password = user.getPassword();
                String hashedPassword = HashStringHelper.hashString(password);
                user.setPassword(hashedPassword);
                String activationCode = HashStringHelper.hashString(user.getLogin());
                user.setConfirmCode(activationCode);
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
    public void sendActivationLetter(String link, String activationCodeParamName, User user) throws UserServiceException {

        String message = LetterBuilder.
                buildActivationLetter(link + AMPERSAND + activationCodeParamName + MARK_EQUAL + user.getConfirmCode(), user);

        try {
            MailSender.getInstance().send(EMAIL_ACTIVATION_LETTER_SUBJECT, message, user.getEmail());

        } catch (FaildSendMailException e) {
            logger.log(Level.ERROR, "Failure in attempt to send activation code", e);
            throw new UserServiceException("Exception in UserServiceImpl method sendActivationLetter().", e);
        }
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

        Set<String> validateResult = userValidator.validate(user.getLogin(), user.getFirstName(), user.getLastName());

        if (validateResult.size() != 0) {
            throw new InvalidUserDataException("Invalid user data.", validateResult);
        }

        try {
            User userById = userDao.getUserById(user.getId());
            if (!userById.getLogin().equals(user.getLogin())) {
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

    /**
     * Changes user password and saves it in the database in a hashed form
     *
     * @param userId      user id
     * @param oldPassword old user password value
     * @param newPassword new user password value
     * @throws UserServiceException     in case of error getting data from the database
     * @throws InvalidUserDataException in case if the parameters passed to the method
     *                                  are not valid or the old password was entered incorrectly
     */
    @Override
    public void changePassword(int userId, String oldPassword, String newPassword) throws UserServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(userId)) {
            throw new InvalidUserDataException("Invalid userId in UserServiceImpl changePassword() method");
        }

        ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
        UserValidator userValidator = validatorFactory.getUserValidator();

        Set<String> validationResult;
        if (!userValidator.validatePassword(newPassword)) {
            validationResult = new HashSet<>();
            validationResult.add(InvalidParam.INVALID_PASSWORD.toString());
            throw new InvalidUserDataException("Invalid user data.", validationResult);
        }

        try {
            String password = userDao.getPassword(userId);
            boolean isValid = HashStringHelper.checkString(oldPassword, password);
            if (!isValid) {
                validationResult = new HashSet<>();
                validationResult.add(InvalidParam.PASSWORD_MISMATCH.toString());
                throw new InvalidUserDataException("Invalid user data.", validationResult);
            }

            String newHashedPassword = HashStringHelper.hashString(newPassword);
            userDao.updateUserPassword(userId, newHashedPassword);

        } catch (DAOException e) {
            throw new UserServiceException("Exception in UserServiceImpl method changePassword().", e);
        }
    }


    @Override
    public boolean checkRegistration(int userId, String activatedCode) throws UserServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(userId) ||
                activatedCode == null) {
            throw new InvalidUserDataException("Invalid userId in UserServiceImpl checkRegistration() method");
        }

        try {
            String storedActivationCode = userDao.getActivationCode(userId);
            return activatedCode.equals(storedActivationCode);

        } catch (DAOSqlException e) {
            throw new UserServiceException("Exception in UserServiceImpl method checkRegistration().", e);
        }
    }

    @Override
    public boolean checkAccountStatus(int userId) throws InvalidUserDataException, UserServiceException {
        if (!frontDataValidator.validateId(userId)) {
            throw new InvalidUserDataException("Invalid userId in UserServiceImpl checkRegistration() method");
        }
        try {
            return userDao.getUserIsActivated(userId);

        } catch (DAOSqlException e) {
            throw new UserServiceException("Exception in UserServiceImpl method checkAccountStatus().", e);
        }
    }

    @Override
    public void markAccountActive(int userId) throws UserServiceException {
        try {
            userDao.updateUserIsActivated(userId, true);
        } catch (DAOException e) {
            throw new UserServiceException("Exception in UserServiceImpl method markAccountActive().", e);
        }
    }

}