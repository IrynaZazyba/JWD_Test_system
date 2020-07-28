package by.jwd.testsys.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public @Getter
@Setter
@ToString
@EqualsAndHashCode
class Test implements Serializable {

    private static final long serialVersionUID = -2148195974182493903L;

    private int id;
    private String title;
    private String key;
    private LocalTime duration;
    private LocalDate deletedAt;
    private Type type;
    private Set<Question> questions;
    private int countQuestion;
    private Assignment assignment;

    private boolean started;
    private boolean edited;


    public Test() {
    }

    public static class Builder {

        private Test newTest;

        public Builder() {
            newTest = new Test();
        }

        public Builder withId(int id) {
            newTest.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            newTest.title = title;
            return this;
        }

        public Builder withKey(String key) {
            newTest.key = key;
            return this;
        }

        public Builder withDuration(LocalTime duration) {
            newTest.duration = duration;
            return this;
        }

        public Builder withDeletedAt(LocalDate deletedAt) {
            newTest.deletedAt = deletedAt;
            return this;
        }

        public Builder withType(Type type) {
            newTest.type = type;
            return this;
        }

        public Builder withQuestions(Set<Question> questions) {
            newTest.questions = questions;
            return this;
        }

        public Builder withCountQuestion(int countQuestion) {
            newTest.countQuestion = countQuestion;
            return this;
        }

        public Builder withAssignment(Assignment assignment) {
            newTest.assignment = assignment;
            return this;
        }

        public Builder withStarted(boolean started) {
            newTest.started = started;
            return this;
        }

        public Builder withEdited(boolean edited) {
            newTest.edited = edited;
            return this;
        }

        public Test build() {
            return newTest;

        }
    }


}
