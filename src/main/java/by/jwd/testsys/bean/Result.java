package by.jwd.testsys.bean;

import java.time.LocalDateTime;

public class Result {

    private int id;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private int rightCountQuestion;
    private int testId;
    private Assignment assignment;



    public Result(){}

    public Result(int id, LocalDateTime dateStart, LocalDateTime dateEnd, int countRight, Assignment assignment){
        this.id=id;
        this.dateStart=dateStart;
        this.dateEnd=dateEnd;
        this.rightCountQuestion=countRight;
        this.assignment=assignment;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getRightCountQuestion() {
        return rightCountQuestion;
    }

    public void setRightCountQuestion(int rightCountQuestion) {
        this.rightCountQuestion = rightCountQuestion;
    }


    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }
}
