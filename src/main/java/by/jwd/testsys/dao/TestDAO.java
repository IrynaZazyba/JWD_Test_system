package by.jwd.testsys.dao;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TestDAO {

    Set<Test> getAssignedTests(int userId) throws DAOException;

    List<Type> getTypes() throws DAOException;

    void saveTestType(String testTypeTitle) throws DAOException;

    Type getTypeByTitle(String title) throws DAOException;

    Test getTestInfo(int id) throws DAOException;

    String getTestKey(int testId) throws DAOException;

    LocalTime getTestDuration(int assignmentId) throws DAOException;

    Set<Test> getTests(int typeId, boolean isEdited) throws DAOException;

    Set<Test> getTestsByLimit(int typeId, int from, int to) throws DAOException;

    Set<Test> getTestsByLimit(int typeId, int from, int to, boolean isEdited, boolean isExistsKey) throws DAOException;

    int getCountTests(int typeId) throws DAOException;

    void deleteTestById(int testId, LocalDateTime deletedDate) throws DAOException;

    int getCountTests(int typeId, boolean isEdited, boolean isExistsKey) throws DAOException;

    int getCountTestAssignment(int testId, boolean isCompleted) throws DAOException;

    int saveTest(Test test, int testId) throws DAOException;

    void updateTest(Test test, int typeId) throws DAOException;

    void updateTestIsEdited(int testId, boolean isEdited) throws DAOException;


}
