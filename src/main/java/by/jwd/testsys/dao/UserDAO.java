package by.jwd.testsys.dao;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;

import java.util.List;

public interface UserDAO {

    List<User> getAll() throws DAOException;

    User create(User user) throws DAOException;

    User getUserByLoginPassword(String login, String password) throws DAOException;

    User getUserById(int id) throws DAOException;

    User updateUser(User user) throws DAOSqlException;

    User getUserByLogin(String login) throws DAOException;


}
