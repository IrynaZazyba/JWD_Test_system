package by.jwd.testsys.logic;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.logic.exception.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AdminService {


    void deleteTest(int testId) throws AdminServiceException, InvalidDeleteActionServiceException;

    int createTest(int typeId, String title, String key, LocalTime duration) throws AdminServiceException;

    void updateTestData(int testId, int typeId, String title, String key, LocalTime duration) throws AdminServiceException;

    void createQuestionAnswer(String question, Map<Integer, String> answers, List<Integer> rightAnswers, int testId) throws AdminServiceException;

    Test receiveTestWithQuestionsAndAnswers(int testId) throws AdminServiceException;

    void changeTestIsEdited(int testId, boolean isEdited) throws AdminServiceException;

    void updateQuestionWithAnswers(int questionId,
                                   String question,
                                   String deletedAnswers,
                                   Map<Integer, String> answers,
                                   Map<Integer, String> addedAnswers,
                                   List<Integer> rightAnswersId,
                                   List<Integer> rightAddedAnswersId) throws AdminServiceException;

    void completeTestCreation(int testID) throws AdminServiceException;

    void deleteQuestionWithAnswers(int questionId) throws AdminServiceException;

    void addTestType(String testTypeTitle) throws AdminServiceException, ExistsTypeAdminServiceException;

    void deleteAssignment(int assignment_id) throws ServiceException;

    Map<String, Set<User>> assignTestToUsers(int testId, LocalDate deadline, String[] assignUsersId) throws ServiceException, DateOutOfRangeException;
}
