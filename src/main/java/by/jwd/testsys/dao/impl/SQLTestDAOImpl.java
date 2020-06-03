package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.*;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class SQLTestDAOImpl implements TestDAO {

    private static Logger logger = LogManager.getLogger();
    private final ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
    private ConnectionPoolDAO connectionPool = connectionPoolFactory.getMySqlConnectionPoolDAO();


    private static final String SELECT_QUESTION = "SELECT id, question FROM question q WHERE test_id=? " +
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

    private static final String GET_COUNT_QUESTION_BY_TEST_ID = "SELECT count(id) as count_question FROM `question` " +
            "WHERE test_id=?";

    private static final String GET_ASSIGNMENT_TESTS = "SELECT test.id as t_id, test.title as t_title, test.time as t_time," +
            " count(question.id) as count_quest, type.title as type_title FROM test inner join question on question.test_id=test.id " +
            "INNER JOIN assignment on assignment.test_id=test.id inner join type on test.type_id=type.id where assignment.user_id =? " +
            "and completed is false and question.deleted_at is null and assignment.deleted_at is null group BY test.id";

    private static final String WRITE_ASSIGNMENT = "INSERT INTO `assignment`" +
            " (`date`,`deadline`, `test_id`, `user_id`, `completed`) " +
            "VALUES (?,?,?,?,?)";

    private static final String SELECT_TESTS_BY_TYPE_ID = "SELECT id, title,`key`,is_edited, time FROM `test` WHERE type_id=? " +
            "and deleted_at is null";

    private static final String UPDATE_ASSIGNMENT_DELETED_AT = "UPDATE `assignment` SET `deleted_at`=? where id=?";

    private static final String UPDATE_TEST_DELETED_AT = "UPDATE `test` SET `deleted_at`=? where id=?";

    private static final String GET_COUNT_INCOMPLETE_ASSIGNMENT_BY_TEST_ID = "SELECT count(id) as count_id FROM `assignment` " +
            "WHERE test_id=? and completed=0";

    private static final String DELETE_QUESTIONS_BY_TEST_ID = "UPDATE `question` set deleted_at=? WHERE test_id=?";

    private static final String DELETE_ANSWER_BY_QUESTION_ID = "UPDATE `answer` set deleted_at=? WHERE question_id=?";

    private static final String GET_QUESTIONS_ID_TO_TEST = "SELECT id from question where test_id=?";

    private static final String INSERT_TEST = "INSERT INTO `test` (`title`, `key`, `time`, `type_id`, `is_edited`) " +
            "VALUES (?,?,?,?,?)";

    private static final String INSERT_QUESTION = "INSERT INTO `question` (`question`, `test_id`) " +
            "VALUES (?,?)";

    private static final String INSERT_ANSWER = "INSERT INTO `answer` (`answer`, `result`, `question_id`) " +
            "VALUES (?,?,?)";

    private static final String UPDATE_TEST = "UPDATE test SET title=?, `key`=?, time=?,type_id=?, " +
            "is_edited=? WHERE id=?";

    @Override
    public Set<Test> getAssignmentTest(int userId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Test> assignmentTests = new HashSet<>();
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(GET_ASSIGNMENT_TESTS);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Test test = buildTest(resultSet);
                assignmentTests.add(test);
            }

        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("Exception in SQLTestDAOImpl method getAssignmentTest", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getAssignmentTest", e);
            throw new DAOSqlException("Exception in SQLTestDAOImpl method getAssignmentTest", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return assignmentTests;
    }


    @Override
    public Question getQuestionByTestId(int id, int assigment_id) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Question question = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_QUESTION);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, assigment_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                question = new Question();
                question.setId(resultSet.getInt("id"));
                question.setQuestion(resultSet.getString("question"));
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPool exception in getQuestionByTestId() method", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in getQuestionByTestId() method", e);
            throw new DAOSqlException("SQLException in getQuestionByTestId() method", e);
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
            throw new DAOSqlException("Exception in SQLTestDAOImpl method getAnswersByQuestionId()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getAnswersByQuestionId()", e);
            throw new DAOSqlException("Exception in SQLTestDAOImpl method getAnswersByQuestionId()", e);
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

                    String key = resultSet.getString("key");
                    test = new Test(id, title, key, time, deleted_at);
                }
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method getTestInfo()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getTestInfo()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getTestInfo()", e);

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
            throw new DAOSqlException("Exception in SQLTestDAOImpl method getCountQuestion()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getCountQuestion()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getCountQuestion()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return countQuestion;
    }


    @Override
    public void updateAssignment(int assignmentId, boolean isCompleted) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_ASSIGNMENT_COMPLETE);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, assignmentId);
            preparedStatement.executeUpdate();

        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method updateAssignment()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method updateAssignment()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method updateAssignment()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }

    @Override
    public Integer writeAssignment(Assignment assignment) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        Integer assignmentId = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(WRITE_ASSIGNMENT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, Date.valueOf(assignment.getAsgmtDate()));
            preparedStatement.setDate(2, Date.valueOf(assignment.getDeadline()));
            preparedStatement.setInt(3, assignment.getTest().getId());
            preparedStatement.setInt(4, assignment.getUser().getId());
            preparedStatement.setBoolean(5, false);
            preparedStatement.executeUpdate();
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                assignmentId = generatedKeys.getInt(1);
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method writeAssignment()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method writeAssignment()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method writeAssignment()", e);
        }
        return assignmentId;
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
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method getRightAnswersToQuestionByTestId", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getRightAnswersToQuestionByTestId()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getRightAnswersToQuestionByTestId()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return questionAnswers;
    }

    @Override
    public String getTestKey(int testId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String key = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_KEY_TO_TEST);
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                key = resultSet.getString("key");
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method getTestKey()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getTestKey()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getTestKey()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return key;
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
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method getTestStartDateTime()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getTestStartDateTime()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getTestStartDateTime()", e);
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
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method getTestStartDateTime()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getTestStartDateTime()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getTestStartDateTime()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return duration;
    }

    @Override
    public Set<Test> getTests(int typeId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Test> tests = new HashSet<>();

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_TESTS_BY_TYPE_ID);
            preparedStatement.setInt(1, typeId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String key = resultSet.getString("key");
                boolean isEdited = resultSet.getBoolean("is_edited");
                Time time = resultSet.getTime("time");
                LocalTime testDuration = null;
                if (time != null) {
                    testDuration = time.toLocalTime();
                }
                Test test = new Test(id, title, key, testDuration, isEdited);
                tests.add(test);
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method getTests()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getTests()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getTests()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return tests;
    }

    @Override
    public void makeAssignmentDeleted(int assignmentId, LocalDate deletedAtDate) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_ASSIGNMENT_DELETED_AT);
            preparedStatement.setDate(1, Date.valueOf(deletedAtDate));
            preparedStatement.setInt(2, assignmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method makeAssignmentDeleted()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method makeAssignmentDeleted()", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method makeAssignmentDeleted()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }


    }

    @Override
    public void deleteTestById(int testId, LocalDateTime deletedDate) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connection = connectionPool.takeConnection();
            connection.setAutoCommit(false);

            Set<Integer> questionsId = new HashSet<>();

            preparedStatement = connection.prepareStatement(GET_QUESTIONS_ID_TO_TEST);
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                questionsId.add(resultSet.getInt("id"));
            }

            for (Integer questId : questionsId) {
                preparedStatement = connection.prepareStatement(DELETE_ANSWER_BY_QUESTION_ID);
                preparedStatement.setTimestamp(1, Timestamp.valueOf(deletedDate));
                preparedStatement.setInt(2, questId);
                preparedStatement.executeUpdate();
            }

            preparedStatement = connection.prepareStatement(DELETE_QUESTIONS_BY_TEST_ID);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(deletedDate));
            preparedStatement.setInt(2, testId);
            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement(UPDATE_TEST_DELETED_AT);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(deletedDate));
            preparedStatement.setInt(2, testId);
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Rollback SQLTestDAOImpl method deleteTestById()", e);
                throw new DAOSqlException("Impossible to rollback SQLTestDAOImpl method deleteTestById()", e);
            }
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method deleteTestById()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method deleteTestById()", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method deleteTestById()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public int getCountIncompleteTestAssignment(int testId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int countAssignment = 0;
        try {
            connection = connectionPool.takeConnection();

            preparedStatement = connection.prepareStatement(GET_COUNT_INCOMPLETE_ASSIGNMENT_BY_TEST_ID);
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                countAssignment = resultSet.getInt("count_id");
            }

        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method getCountIncompleteTestAssignment()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getCountIncompleteTestAssignment()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getCountIncompleteTestAssignment()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return countAssignment;
    }

    @Override
    public int saveTest(Test test, int typeId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKey = null;
        int createdTestId = 0;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(INSERT_TEST, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, test.getTitle());
            preparedStatement.setString(2, test.getKey());
            preparedStatement.setTime(3, Time.valueOf(test.getDuration()));
            preparedStatement.setInt(4, typeId);
            preparedStatement.setBoolean(5, test.getEdited());
            preparedStatement.executeUpdate();

            generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                createdTestId = generatedKey.getInt(1);
            }

        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method saveTest()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method saveTest()", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method saveTest()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, generatedKey);
        }
        return createdTestId;
    }

    @Override
    public int saveQuestionWithAnswers(Question question, int testId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKey = null;
        int createdQuestionId = 0;

        try {
            connection = connectionPool.takeConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(INSERT_QUESTION, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, question.getQuestion());
            preparedStatement.setInt(2, testId);
            preparedStatement.executeUpdate();

            generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                createdQuestionId = generatedKey.getInt(1);
            }

            for (Answer answer : question.getAnswers()) {
                preparedStatement = connection.prepareStatement(INSERT_ANSWER);
                preparedStatement.setString(1, answer.getAnswer());
                preparedStatement.setBoolean(2, answer.isResult());
                preparedStatement.setInt(3, createdQuestionId);
                preparedStatement.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);

        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method saveQuestionWithAnswers()", e);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Rollback SQLTestDAOImpl method deleteTestById()", e);
                throw new DAOSqlException("Impossible to rollback SQLTestDAOImpl method deleteTestById()", e);
            }
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method saveQuestionWithAnswers()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method saveQuestionWithAnswers()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, generatedKey);
        }
        return createdQuestionId;
    }

    @Override
    public void updateTest(Test test, int typeId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_TEST);
            preparedStatement.setString(1, test.getTitle());
            preparedStatement.setString(2, test.getKey());
            preparedStatement.setTime(3, Time.valueOf(test.getDuration()));
            preparedStatement.setInt(4, typeId);
            preparedStatement.setBoolean(5, test.getEdited());
            preparedStatement.setInt(6,test.getId());
            preparedStatement.executeUpdate();
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method updateTest()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method updateTest()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method updateTest()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }

    private Test buildTest(ResultSet resultSet) throws SQLException {
        int testId = resultSet.getInt("t_id");
        String testTitle = resultSet.getString("t_title");
        LocalTime testTime = resultSet.getTime("t_time").toLocalTime();
        String typeTitle = resultSet.getString("type_title");
        int countQuestion = resultSet.getInt("count_quest");
        Type testType = new Type();
        testType.setTitle(typeTitle);
        return new Test(testId, testTitle, testType, testTime, countQuestion);
    }
}
