package by.jwd.testsys.bean;

import java.time.LocalDate;

public class Assignment {

    private int id;
    private User user;
    private LocalDate asgmtDate;
    private LocalDate deadline;
    private Test test;
    private boolean isComplete;


    private TestLog testLog;

    public Assignment() {

    }

    public Assignment(int id, User user, LocalDate asgmtDate, LocalDate deadline, Test test) {
        this.id = id;
        this.user = user;
        this.asgmtDate = asgmtDate;
        this.deadline = deadline;
        this.test = test;
    }
    public Assignment(int id, LocalDate asgmtDate, LocalDate deadline, boolean isComplete) {
        this.id = id;
        this.asgmtDate = asgmtDate;
        this.deadline = deadline;
        this.isComplete=isComplete;
    }

    public Assignment(int id, LocalDate asgmtDate, LocalDate deadline, Test test, boolean isComplete) {
        this.id = id;
        this.asgmtDate = asgmtDate;
        this.deadline = deadline;
        this.test = test;
        this.isComplete=isComplete;

    }

    public Assignment(User user, LocalDate asgmtDate,LocalDate deadline, Test test) {
        this.user = user;
        this.asgmtDate = asgmtDate;
        this.deadline=deadline;
        this.test = test;
        this.isComplete=isComplete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public TestLog getTestLog() {
        return testLog;
    }

    public void setTestLog(TestLog testLog) {
        this.testLog = testLog;
    }

    public LocalDate getAsgmtDate() {
        return asgmtDate;
    }

    public void setAsgmtDate(LocalDate asgmtDate) {
        this.asgmtDate = asgmtDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
