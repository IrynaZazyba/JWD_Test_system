package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.dao.TestTypeDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPool;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLTestTypeDAOImpl implements TestTypeDAO {

    private static Logger logger = LogManager.getLogger();
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String SELECT_ALL_TYPES = "SELECT id, title, deleted_at from type";
    private static final String INSERT_USER = "INSERT INTO users (login, password, first_name, last_name, role_id) " +
            "VALUES (?,?,?,?,?)";
    private static final String SELECT_USER_BY_LOGIN = "SELECT u.id,u.login,u.password,u.first_name,u.last_name," +
            "role.title FROM users as u INNER JOIN role ON u.role_id=role.id WHERE u.login=?";
    private static final String SELECT_ROLE_ID = "SELECT id FROM role WHERE title=?";


    @Override
    public List<Type> getAll() throws DAOException {

        Connection connection;
        List<Type> typesFromDB = null;
        try {
            connection = connectionPool.takeConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_TYPES);
            typesFromDB = new ArrayList<>();
            while (resultSet.next()) {
                int typeId = resultSet.getInt("id");
                String typeTitle = resultSet.getString("title");
                LocalDate deletedAt;
                if (resultSet.getDate("deleted_at") != null) {
                    deletedAt = resultSet.getDate("deleted_at").toLocalDate();
                }else{
                    deletedAt=null;
                }
                System.out.println(typeId + " " + typeTitle + " " + deletedAt);
                typesFromDB.add(new Type(typeId, typeTitle, deletedAt));
            }
        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, "Exception in SQLTypeDAOImpl getAll().");
            throw new DAOException("Exception in SQLTypeDAOImpl getAll().");
        }

        if (typesFromDB != null) {
            System.out.println("not null");
        }
        return typesFromDB;
    }




}
