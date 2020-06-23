package by.jwd.testsys.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public @Getter
@Setter
@ToString
@EqualsAndHashCode
class Question implements Serializable {

    private static final long serialVersionUID = 1068955883794784134L;

    private int id;
    private String question;
    private LocalDate deletedAt;
    private Test test;
    private Set<Answer> answers;

    public Question() {

    }

    public static class Builder {

        private Question newQuestion;

        public Builder() {
            newQuestion = new Question();
        }

        public Builder withId(int id) {
            newQuestion.id = id;
            return this;
        }

        public Builder withQuestion(String question) {
            newQuestion.question = question;
            return this;
        }

        public Builder withDeletedAt(LocalDate deletedAt) {
            newQuestion.deletedAt = deletedAt;
            return this;
        }

        public Builder withTest(Test test) {
            newQuestion.test = test;
            return this;
        }

        public Builder withAnswers(Set<Answer> answers) {
            newQuestion.answers = answers;
            return this;
        }

        public Question build() {
            return newQuestion;
        }
    }

}
