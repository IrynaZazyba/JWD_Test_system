package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.Answer;
import by.jwd.testsys.bean.Question;
import by.jwd.testsys.bean.Result;
import by.jwd.testsys.bean.Test;
import by.jwd.testsys.dao.TestDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.dbconn.factory.ConnectionPoolFactory;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class SQLTestDAOImpl implements TestDAO {

    private static Logger logger = LogManager.getLogger();
    private final ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
    private ConnectionPoolDAO connectionPool = connectionPoolFactory.getMySqlConnectionPoolDAO();


    private static final String SELECT_QUESTION_BY_ID = "SELECT id, question FROM question q WHERE test_id=? " +
            "and not EXISTS (select * from `question-log` where q.id=question_id  and assignment_id=?) Limit 1;";


    private static final String SELECT_TEST_INFO_BY_ID = "SELECT t.id tid, t.title , t.key, t.time, t.deleted_at tdel " +
            "FROM test t inner join question q on q.test_id=t.id  WHERE t.id=? AND q.deleted_at is null;";


    private static final String SELECT_ANSWER_BY_QUESTION_ID = "SELECT a.id, a.answer FROM answer a WHERE question_id=?";


    private static final String UPDATE_ASSIGNMENT_COMPLETE = "UPDATE `assignment` SET `completed`=? " +
            "WHERE assignment.id=?";

    private static final String SELECT_RIGHT_ANSWERS_TO_QUESTION = "SELECT q.id q_id, a.id  a_id FROM question q " +
            "INNER JOIN answer a ON q.id=a.question_id WHERE test_id=? AND result=1";


    private static final String SELECT_KEY_TO_TEST = "SELECT `key` FROM `test` WHERE id=?";

    private static final String SELECT_START_TEST_DATE_TIME = "SELECT date_start FROM `result` WHERE assignment_id=?";

    private static final String SELECT_TEST_TIME = "SELECT time FROM `test` WHERE id=(" +
            "SELECT test_id FROM assignment WHERE id=?)";

    public static final String GET_COUNT_QUESTION_BY_TEST_ID = "SELECT count(id) as count_question FROM `question` " +
            "WHERE test_id=?";

    @Override
    public Question getQuestionByTestId(int id, int assigment_id) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Question question = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_QUESTION_BY_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, assigment_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                question = new Question();
                question.setId(resultSet.getInt("id"));
                question.setQuestion(resultSet.getString("question"));
            }


        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, e.getMessage());
            //todo not only sql
            throw new DAOSqlException("SQLException in getQuestionByTestId method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);

        }
        return question;
    }

    @Override
    public Set<Answer> getAnswersByQuestionId(int id) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Answer> answers = new HashSet<>();
        Answer answer = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_ANSWER_BY_QUESTION_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                answer = new Answer();
                answer.setId(resultSet.getInt("id"));
                answer.setAnswer(resultSet.getString("answer"));
                answers.add(answer);
            }
        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, "ConnectionPoolException in SQLTestLogDAOImpl method getAnswersByQuestionId", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method getAnswersByQuestionId", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method getAnswersByQuestionId", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method getAnswersByQuestionId", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);

        }
        return answers;
    }


    @Override
    public Test getTestInfo(int id) throws DAOSqlException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Test test = null;
        Set<Question> questions = new HashSet<>();
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_TEST_INFO_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (test == null) {
                    String title = resultSet.getString("title");
                    Date tdel = resultSet.getDate("tdel");
                    LocalDate deleted_at = null;
                    if (tdel != null) {
                        deleted_at = tdel.toLocalDate();
                    }

                    LocalTime time = resultSet.getTime("time").toLocalTime();

                    int key = resultSet.getInt("key");
                    test = new Test(id, title, key, time, deleted_at);
                }

            }
        } catch (ConnectionPoolException | SQLException e) {
            //todo
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method getTestInfo", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method getTestInfo", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);

        }

        return test;
    }

    @Override
    public int getCountQuestion(int testId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int countQuestion = 0;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(GET_COUNT_QUESTION_BY_TEST_ID);
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                countQuestion = resultSet.getInt("count_question");
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("Exception in SQLTestDAOImpl method getCountQuestion", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getCountQuestion", e);
            throw new DAOSqlException("Exception in SQLTestDAOImpl method getCountQuestion", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement,resultSet);
        }
        return countQuestion;
    }


    @Override
    public void writeAssignment(int assignmentId, boolean isCompleted) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_ASSIGNMENT_COMPLETE);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, assignmentId);
            preparedStatement.executeUpdate();

        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, "ConnectionPoolException in SQLTestDAOImpl method writeAssignment", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method writeAssignment", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method writeAssignment", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method writeAssignment", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);

        }
    }


    @Override
    public Map<Integer, List<Integer>> getRightAnswersToQuestionByTestId(int testId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Map<Integer, List<Integer>> questionAnswers = new HashMap<>();
        List<Integer> listAnswers = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_RIGHT_ANSWERS_TO_QUESTION);
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int q_id = resultSet.getInt("q_id");
                if (questionAnswers.containsKey(q_id)) {
                    List<Integer> answers = questionAnswers.get(q_id);
                    answers.add(resultSet.getInt("a_id"));
                } else {
                    int a_id = resultSet.getInt("a_id");
                    listAnswers = new ArrayList<>();
                    listAnswers.add(a_id);
                    questionAnswers.put(q_id, listAnswers);
                }
            }

            questionAnswers.forEach((k, v) -> System.out.println("~~~" + k + " " + v));
        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, "ConnectionPoolException in SQLTestLogDAOImpl method getRightAnswersToQuestionByTestId", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method getRightAnswersToQuestionByTestId", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method getRightAnswersToQuestionByTestId", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method getRightAnswersToQuestionByTestId", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return questionAnswers;
    }

    @Override
    public Integer getTestKey(int testId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer key = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_KEY_TO_TEST);
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                key = resultSet.getInt("key");
            }
        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, "ConnectionPoolException in SQLTestLogDAOImpl method getTestKey", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method getTestKey\"", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method getTestKey", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method wgetTestKey\"", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return key;
    }


    private Test buildTest(ResultSet resultSet) throws SQLException {
        int idTest = resultSet.getInt("tt_id");
        String titleTest = resultSet.getString("tt_title");
        return new Test(idTest, titleTest);
    }

    @Override
    public Timestamp getTestStartDateTime(int assignmentId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Timestamp dateStart = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_START_TEST_DATE_TIME);
            preparedStatement.setInt(1, assignmentId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                dateStart = resultSet.getTimestamp("date_start");
                System.out.println(dateStart);

            }
        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, "ConnectionPoolException in SQLTestLogDAOImpl method getTestStartDateTime", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method getTestStartDateTime", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method getTestStartDateTime", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method getTestStartDateTime", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return dateStart;
    }

    @Override
    public LocalTime getTestDuration(int assignmentId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        LocalTime duration = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_TEST_TIME);
            preparedStatement.setInt(1, assignmentId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Time time = resultSet.getTime("time");
                duration = time.toLocalTime();
            }
        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, "ConnectionPoolException in SQLTestLogDAOImpl method getTestStartDateTime", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method getTestStartDateTime", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method getTestStartDateTime", e);
            throw new DAOSqlException("Exception in SQLTestLogDAOImpl method getTestStartDateTime", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return duration;
    }


    //todo
    private Question buildQuestion(ResultSet resultSet) {
        Question question = null;
        return question;
    }

    private Answer buildAnswer(ResultSet resultSet) {
        Answer answer = null;
        return answer;
    }
}
