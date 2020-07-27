package by.jwd.testsys.logic;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.logic.exception.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AdminService {


    void deleteTest(int testId) throws AdminServiceException, InvalidDeleteActionServiceException, InvalidUserDataException;

    int createTest(int typeId, String title, String key, LocalTime duration) throws AdminServiceException, InvalidUserDataException;

    void updateTestData(int testId, int typeId, String title, String key, LocalTime duration) throws AdminServiceException, InvalidUserDataException;

    void createQuestionAnswer(String question, Map<Integer, String> answers, List<Integer> rightAnswers, int testId) throws AdminServiceException, InvalidUserDataException;

    Test receiveTestWithQuestionsAndAnswers(int testId) throws AdminServiceException, InvalidUserDataException;

    void changeTestIsEdited(int testId, boolean isEdited) throws AdminServiceException, InvalidUserDataException;

    void updateQuestionWithAnswers(int questionId,
                                   String question,
                                   String deletedAnswers,
                                   Map<Integer, String> answers,
                                   Map<Integer, String> addedAnswers,
                                   List<Integer> rightAnswersId,
                                   List<Integer> rightAddedAnswersId) throws AdminServiceException, InvalidUserDataException;

    void completeTestCreation(int testID) throws AdminServiceException, InvalidUserDataException;

    void deleteQuestionWithAnswers(int questionId) throws AdminServiceException, InvalidUserDataException;

    void addTestType(String testTypeTitle) throws AdminServiceException, ExistsTypeAdminServiceException, InvalidUserDataException;

    void deleteAssignment(int assignment_id) throws ServiceException;

    Map<String, Set<User>> assignTestToUsers(int testId, LocalDate deadline, String[] assignUsersId) throws InvalidUserDataException, AdminServiceException;

    boolean sendLetterAboutAssignmentToUsers(Set<User> assignedUsers, int testId, LocalDate deadline) throws AdminServiceException, InvalidUserDataException;

    void deleteType(int testTypeId) throws AdminServiceException, InvalidDeleteActionServiceException, InvalidUserDataException;
}
