package by.jwd.testsys.logic;

import by.jwd.testsys.bean.Result;
import by.jwd.testsys.logic.exception.TestLogServiceException;

import java.time.LocalDate;
import java.util.Set;

public interface TestLogService {

    void writeUserAnswer(int questionLogId,String [] answerSet) throws TestLogServiceException;

    int writeQuestionLog(int questionId, int assignmentId) throws TestLogServiceException;

    Set<Result> reciveResultData(int typeId, int testId, int userId, LocalDate date) throws TestLogServiceException;
}
