package by.jwd.testsys.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

public @Getter
@Setter
@ToString
@EqualsAndHashCode
class Answer implements Serializable {

    private static final long serialVersionUID = 8087378516798590910L;

    private int id;
    private String answer;
    private boolean result;
    private LocalDate deletedAt;
    private Question question;

    public Answer() {

    }

    public static class Builder {

        private Answer newAnswer;

        public Builder() {
            newAnswer = new Answer();
        }

        public Builder withId(int id) {
            newAnswer.id = id;
            return this;
        }

        public Builder withAnswer(String answer) {
            newAnswer.answer = answer;
            return this;
        }

        public Builder withResult(boolean result) {
            newAnswer.result = result;
            return this;
        }

        public Builder withDeletedAt(LocalDate deletedAt) {
            newAnswer.deletedAt = deletedAt;
            return this;
        }

        public Builder withQuestion(Question question) {
            newAnswer.question = question;
            return this;
        }

        public Answer build() {
            return newAnswer;
        }

    }

}
