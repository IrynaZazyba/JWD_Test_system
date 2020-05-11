package by.jwd.testsys.bean;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;

public class Statistic implements Serializable {


    private static final long serialVersionUID = 887666526432335145L;
    private String testTitle;
    private LocalTime timeOnTest;
    private LocalDateTime testStart;
    private LocalDateTime testEnd;
    private long minutesSpentOnTest;
    private int rightCountQuestion;
    private int allCountQuestion;

    public Statistic() {
    }

    public Statistic(String testTitle, LocalTime timeOnTest, LocalDateTime testStart, LocalDateTime testEnd, int rightCountQuestion, int allCountQuestion) {
        this.testTitle = testTitle;
        this.timeOnTest = timeOnTest;
        this.testStart = testStart;
        this.testEnd = testEnd;
        this.rightCountQuestion = rightCountQuestion;
        this.allCountQuestion = allCountQuestion;
        Duration duration=Duration.between(testStart,testEnd);
        this.minutesSpentOnTest= Math.abs(duration.toMinutes());
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }

    public LocalTime getTimeOnTest() {
        return timeOnTest;
    }

    public void setTimeOnTest(LocalTime timeOnTest) {
        this.timeOnTest = timeOnTest;
    }

    public LocalDateTime getTestStart() {
        return testStart;
    }

    public void setTestStart(LocalDateTime testStart) {
        this.testStart = testStart;
    }

    public LocalDateTime getTestEnd() {
        return testEnd;
    }

    public void setTestEnd(LocalDateTime testEnd) {
        this.testEnd = testEnd;
    }

    public int getRightCountQuestion() {
        return rightCountQuestion;
    }

    public void setRightCountQuestion(int rightCountQuestion) {
        this.rightCountQuestion = rightCountQuestion;
    }

    public int getAllCountQuestion() {
        return allCountQuestion;
    }

    public void setAllCountQuestion(int allCountQuestion) {
        this.allCountQuestion = allCountQuestion;
    }

    public long getMinutesSpentOnTest() {
        return minutesSpentOnTest;
    }

    public void setMinutesSpentOnTest(long minutesSpentOnTest) {
        this.minutesSpentOnTest = minutesSpentOnTest;
    }
}
