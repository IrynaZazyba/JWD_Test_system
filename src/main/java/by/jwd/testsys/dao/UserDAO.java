package by.jwd.testsys.dao;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.exception.DAOException;

import java.util.List;

public interface UserDAO {

    List<User> getAll() throws DAOException;

    boolean save(User user) throws DAOException;

    User getUserByLoginPassword(String login, String password) throws DAOException;

    User getUserById(int id) throws DAOException;

}
