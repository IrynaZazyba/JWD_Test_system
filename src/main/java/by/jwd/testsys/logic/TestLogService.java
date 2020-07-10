package by.jwd.testsys.logic;

import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.TestLogServiceException;

public interface TestLogService {

    void writeUserAnswer(int questionLogId,String [] answerSet) throws TestLogServiceException, InvalidUserDataException;

    int writeQuestionLog(int questionId, int assignmentId) throws TestLogServiceException, InvalidUserDataException;

}
