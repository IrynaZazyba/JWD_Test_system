package by.jwd.testsys.bean;

import java.time.LocalDate;

public class Assignment {

    private int id;
    private User user;
    private LocalDate asgmtDate;
    private LocalDate deadline;
    private Test test;


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

    public Assignment(int id, LocalDate asgmtDate, LocalDate deadline, Test test) {
        this.id = id;
        this.asgmtDate = asgmtDate;
        this.deadline = deadline;
        this.test = test;
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

    public LocalDate getAssgmtDate() {
        return asgmtDate;
    }

    public void setAssgmtDate(LocalDate assgmtDate) {
        this.asgmtDate = assgmtDate;
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
}
