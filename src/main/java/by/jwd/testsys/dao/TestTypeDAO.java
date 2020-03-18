package by.jwd.testsys.dao;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.dao.exception.DAOException;

import java.util.List;

public interface TestTypeDAO {

    List<Type> getAll() throws DAOException;

}
