package by.jwd.testsys.dao;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.dbconn.SqlConnectionPoolDAOTestImpl;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.dao.util.ResultComparator;
import by.jwd.testsys.dao.util.StatisticComparator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.TreeSet;

@RunWith(Theories.class)
public class SQLTestResultDAOTest {

    private static Logger logger = LogManager.getLogger(SQLTestResultDAOTest.class);

    @Mock
    private static ConnectionPoolDAO mockedConnectionPool;

    private static TestResultDAO testResultDAO = DAOFactoryProvider.getSqlDaoFactory().getTestResultDao();


    @BeforeClass
    public static void initializeConnectionPool() {

        mockedConnectionPool = new SqlConnectionPoolDAOTestImpl();
        try {
            mockedConnectionPool.initPoolData();
        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, "Connection pool didn't initialize.", e);
        }

        Whitebox.setInternalState(testResultDAO, "connectionPool", mockedConnectionPool);
    }


    @AfterClass
    public static void disposeConnectionPool() {
        mockedConnectionPool.dispose();
    }

    @DataPoint
    public static Assignment ASSIGNMENT = new Assignment.Builder()
            .withId(249)
            .withAssignmentDate(LocalDate.parse("2020-07-21"))
            .withDeadline(LocalDate.parse("2020-07-28"))
            .withTest(new by.jwd.testsys.bean.Test.Builder().withId(11).build())
            .withIsCompleted(true)
            .build();


    @DataPoint
    public static Result RESULT = new Result.Builder()
            .withDateStart(LocalDateTime.parse("2020-07-27 12:58:58", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .withRightCountQuestion(7)
            .withCountTestQuestion(10)
            .withAssignment(new Assignment.Builder().withId(153).build())
            .build();


    @Theory
    public void getTestResult(Assignment assignment) throws DAOException {

        Result actualTestResult = testResultDAO.getTestResult(assignment);
        Result expectedTestResult = new Result.Builder()
                .withId(54)
                .withDateStart(LocalDateTime.parse("2020-07-22 21:12:58", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .withDateEnd(LocalDateTime.parse("2020-07-22 21:33:29", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .withRightCountQuestion(3)
                .withAssignment(ASSIGNMENT)
                .build();
        Assert.assertEquals(expectedTestResult, actualTestResult);

    }


    @Test
    public void getTestStartDateTime() throws DAOException {

        Timestamp actualTestStartDateTime = testResultDAO.getTestStartDateTime(249);
        Timestamp expectedTestStartDateTime = Timestamp.valueOf("2020-07-22 21:12:58");
        Assert.assertEquals(expectedTestStartDateTime, actualTestStartDateTime);

    }


    @Theory
    public void insertResult(Result result) throws DAOException {

        int beforeInsert = getCountRowResultTableTestDB();
        testResultDAO.insertResult(result);
        int afterInsert = getCountRowResultTableTestDB();
        Assert.assertEquals(beforeInsert + 1, afterInsert);
        deleteResultInsertTestDB(result);
    }

    @Test
    public void updateResultDateEndRightCount() throws DAOException {
        Result resultForUpdate = new Result.Builder()
                .withRightCountQuestion(10)
                .withDateEnd(LocalDateTime.parse("2020-07-22 21:33:29", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .withId(58)
                .build();
        testResultDAO.updateResultDateEndRightCount(resultForUpdate);
        Result updatedResult = getResultTestDB(58);

        Assert.assertEquals(resultForUpdate.getRightCountQuestion(), updatedResult.getRightCountQuestion());
        Assert.assertEquals(resultForUpdate.getDateEnd(), updatedResult.getDateEnd());
        updateResultTestDB(58);
    }

    @Test
    public void getUserTestStatistic() throws DAOException {
        Set<Statistic> actualUserTestStatistic = new TreeSet<>(new StatisticComparator());
        actualUserTestStatistic.addAll(testResultDAO.getUserTestStatistic(2));

        Set<Statistic> expectedUserTestStatistic = new TreeSet<>(new StatisticComparator());
        Statistic statisticFirst = new Statistic.Builder().withTestTitle("Modal verb")
                .withTimeOnTest(LocalTime.parse("00:10:00"))
                .withRightCountQuestion(7)
                .withAllCountQuestion(10)
                .withTestStart(LocalDateTime.parse("2020-07-27 13:50:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .withTestEnd(LocalDateTime.parse("2020-07-27 13:58:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        expectedUserTestStatistic.add(statisticFirst);
        Statistic statisticSecond = new Statistic.Builder().withTestTitle("Verb to be")
                .withTimeOnTest(LocalTime.parse("00:10:00"))
                .withRightCountQuestion(3)
                .withAllCountQuestion(10)
                .withTestStart(LocalDateTime.parse("2020-07-27 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .withTestEnd(LocalDateTime.parse("2020-07-27 12:05:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        expectedUserTestStatistic.add(statisticSecond);
        Statistic statisticThird = new Statistic.Builder().withTestTitle("Кредиты")
                .withTimeOnTest(LocalTime.parse("00:15:00"))
                .withRightCountQuestion(6)
                .withAllCountQuestion(10)
                .withTestStart(LocalDateTime.parse("2020-07-27 09:05:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .withTestEnd(LocalDateTime.parse("2020-07-27 09:15:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        expectedUserTestStatistic.add(statisticThird);
        Assert.assertEquals(expectedUserTestStatistic.size(), actualUserTestStatistic.size());
        Assert.assertEquals(expectedUserTestStatistic, actualUserTestStatistic);
    }

    @Test
    public void getTestResult_oneParameter() throws DAOException {
        int typeId = 1;
        int testId = 0;
        int userId = 0;
        LocalDate date = null;
        ResultComparator resultComparator = new ResultComparator();
        Set<Result> actualResults = new TreeSet<>(resultComparator);
        actualResults.addAll(testResultDAO.getTestResult(typeId, testId, userId, date));
        Set<Result> expectedResult = new TreeSet<>(resultComparator);
        Result resultFirst = new Result.Builder()
                .withDateStart(LocalDateTime.parse("2020-07-27 13:58:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .withRightCountQuestion(7)
                .withCountTestQuestion(10)
                .withAssignment(new Assignment.Builder().withId(250).build())
                .withTest(new by.jwd.testsys.bean.Test.Builder().withTitle("Modal verb").
                        withType(new Type.Builder().withTitle("English").build()).build())
                .withUser(new User.Builder().withFirstName("Ирина").withLastName("Зазыбо").build())
                .build();
        expectedResult.add(resultFirst);
        Result resultSecond = new Result.Builder()
                .withDateStart(LocalDateTime.parse("2020-07-27 12:05:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .withRightCountQuestion(3)
                .withCountTestQuestion(10)
                .withAssignment(new Assignment.Builder().withId(251).build())
                .withTest(new by.jwd.testsys.bean.Test.Builder().withTitle("Verb to be").
                        withType(new Type.Builder().withTitle("English").build()).build())
                .withUser(new User.Builder().withFirstName("Ирина").withLastName("Зазыбо").build())
                .build();
        expectedResult.add(resultSecond);

        Assert.assertEquals(expectedResult.size(), actualResults.size());
        Assert.assertEquals(expectedResult, actualResults);

    }

    @Test
    public void getTestResult_twoParameters() throws DAOException {
        int typeId = 1;
        int testId = 6;
        int userId = 0;
        LocalDate date = null;
        ResultComparator resultComparator = new ResultComparator();
        Set<Result> actualResults = new TreeSet<>(resultComparator);
        actualResults.addAll(testResultDAO.getTestResult(typeId, testId, userId, date));
        Set<Result> expectedResult = new TreeSet<>(resultComparator);
        Result resultSecond = new Result.Builder()
                .withDateStart(LocalDateTime.parse("2020-07-27 12:05:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .withRightCountQuestion(3)
                .withCountTestQuestion(10)
                .withAssignment(new Assignment.Builder().withId(251).build())
                .withTest(new by.jwd.testsys.bean.Test.Builder().withTitle("Verb to be").
                        withType(new Type.Builder().withTitle("English").build()).build())
                .withUser(new User.Builder().withFirstName("Ирина").withLastName("Зазыбо").build())
                .build();
        expectedResult.add(resultSecond);

        Assert.assertEquals(expectedResult.size(), actualResults.size());
        Assert.assertEquals(expectedResult, actualResults);

    }


    private int getCountRowResultTableTestDB() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;
        try {
            connection = mockedConnectionPool.takeConnection();
            preparedStatement = connection.prepareStatement("SELECT count(id) FROM result");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, "Exception in attempt to test SQLTestResultDAOTest method insertResult()", e);
        } finally {
            mockedConnectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return count;
    }

    private void deleteResultInsertTestDB(Result result) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = mockedConnectionPool.takeConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM result WHERE assignment_id=?");
            preparedStatement.setInt(1, result.getAssignment().getId());
            preparedStatement.executeUpdate();

        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, "Exception in attempt to test SQLTestResultDAOTest method insertResult()", e);
        } finally {
            mockedConnectionPool.closeConnection(connection, preparedStatement);
        }
    }


    private Result getResultTestDB(int resultId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Result result = null;
        try {
            connection = mockedConnectionPool.takeConnection();
            preparedStatement = connection.prepareStatement("SELECT date_start,date_end,right_count_quest " +
                    "FROM result where id=?");
            preparedStatement.setInt(1, resultId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Timestamp date_start = resultSet.getTimestamp(1);
                LocalDateTime dateStart = date_start.toLocalDateTime();
                Timestamp date_end = resultSet.getTimestamp(2);
                LocalDateTime dateEnd = null;
                if (date_end != null) {
                    dateEnd = date_end.toLocalDateTime();
                }
                int rightCountQuestions = resultSet.getInt(3);

                result = new Result.Builder()
                        .withId(resultId)
                        .withDateStart(dateStart)
                        .withDateEnd(dateEnd)
                        .withRightCountQuestion(rightCountQuestions)
                        .build();
            }
        } catch (SQLException | ConnectionPoolException e) {
            logger.log(Level.ERROR, "Exception in attempt to test SQLTestResultDAOTest method updateResultDateEndRightCount()", e);
        } finally {
            mockedConnectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return result;
    }

    private void updateResultTestDB(int resultId) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = mockedConnectionPool.takeConnection();
            preparedStatement = connection.prepareStatement("UPDATE result SET date_end=?,right_count_quest=? WHERE id=?");
            preparedStatement.setNull(1, Types.NULL);
            preparedStatement.setNull(2, Types.NULL);
            preparedStatement.setInt(3, resultId);
            preparedStatement.executeUpdate();
        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, "Exception in attempt to test SQLTestResultDAOTest method updateResultDateEndRightCount()", e);
        } finally {
            mockedConnectionPool.closeConnection(connection, preparedStatement);
        }
    }
}
