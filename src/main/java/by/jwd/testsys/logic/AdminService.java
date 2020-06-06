package by.jwd.testsys.logic;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.exception.InvalidDeleteActionServiceException;

import java.util.List;
import java.util.Map;

public interface AdminService {


    void deleteTest(int testId) throws AdminServiceException, InvalidDeleteActionServiceException;

    int createTest(int typeId, String title, String key, int duration) throws AdminServiceException;

    void updateTestData(int testId, int typeId, String title, String key, int duration) throws AdminServiceException;

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
}
