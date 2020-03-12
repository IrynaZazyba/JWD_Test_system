package by.jwd.testsys.dao;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.exception.DAOException;

import java.util.List;

public interface UserDAO {

    List<User> getAll() throws DAOException;

    void save(User user) throws DAOException;

    User getUserByLogin(String login) throws DAOException;


}
