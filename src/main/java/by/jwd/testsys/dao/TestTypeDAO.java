package by.jwd.testsys.dao;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;

import java.util.List;
import java.util.Set;

public interface TestTypeDAO {

    List<Type> getAll() throws DAOException;
    Set<Type> getTypeWithTests() throws DAOException;

    void saveTestType(String testTypeTitle) throws DAOSqlException;

    Type getTypeByTitle(String title) throws DAOSqlException;
}
