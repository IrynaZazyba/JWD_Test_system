package by.jwd.testsys.bean;

import java.util.List;
import java.util.Map;

public class TestLog {

    private Map<Integer, List<Integer>> questionAnswerMap;
    private int assignment_id;

    public TestLog() {
    }

    public Map<Integer, List<Integer>> getQuestionAnswerMap() {
        return questionAnswerMap;
    }

    public void setQuestionAnswerMap(Map<Integer, List<Integer>> questionAnswerMap) {
        this.questionAnswerMap = questionAnswerMap;
    }

    public int getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(int assignment_id) {
        this.assignment_id = assignment_id;
    }
}
