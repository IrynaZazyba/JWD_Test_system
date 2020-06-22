package by.jwd.testsys.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

public  @Getter @Setter @ToString @EqualsAndHashCode
class Assignment {

    private int id;
    private User user;
    private LocalDate assignmentDate;
    private LocalDate deadline;
    private Test test;
    private boolean isComplete;
    private TestLog testLog;

    public Assignment() {

    }

    public static class Builder {

        private Assignment newAssignment;

        public Builder() {
            newAssignment = new Assignment();
        }

        public Builder withId(int id) {
            newAssignment.id = id;
            return this;
        }

        public Builder withUser(User user) {
            newAssignment.user = user;
            return this;
        }

        public Builder withAssignmentDate(LocalDate asgmtDate) {
            newAssignment.assignmentDate = asgmtDate;
            return this;
        }

        public Builder withDeadline(LocalDate deadline) {
            newAssignment.deadline = deadline;
            return this;
        }

        public Builder withTest(Test test) {
            newAssignment.test = test;
            return this;
        }

        public Builder withIsCompleted(boolean isCompleted) {
            newAssignment.isComplete = isCompleted;
            return this;
        }

        public Builder withTestLog(TestLog testLog) {
            newAssignment.testLog = testLog;
            return this;
        }

        public Assignment build() {
            return newAssignment;

        }
    }

}
