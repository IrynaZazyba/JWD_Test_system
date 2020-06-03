package by.jwd.testsys.bean;

import java.io.Serializable;
import java.time.LocalDate;

public class Answer implements Serializable {

    private static final long serialVersionUID = 8087378516798590910L;

    private int id;
    private String answer;
    private boolean result;
    private LocalDate deletedAt;
    private Question question;

    public Answer() {

    }

    public Answer(String answer) {
        this.answer = answer;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public LocalDate getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDate deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Answer other = (Answer) obj;
        if (id != other.id) {
            return false;
        }
        if (answer == null) {
            if (other.answer != null) return false;
        } else if (!answer.equals(other.answer)) {
            return false;
        }
        if (result != other.result) {
            return false;
        }
        if (deletedAt == null) {
            if (other.deletedAt != null) return false;
        } else if (!deletedAt.equals(other.deletedAt)) {
            return false;
        }
        if (question == null) {
            if (other.question != null) return false;
        } else if (!question.equals(other.question)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((answer == null) ? 0 : answer.hashCode());
        result = prime * result + Boolean.valueOf(this.result).hashCode();
        result = prime * result + ((deletedAt == null) ? 0 : deletedAt.hashCode());
        result = prime * result + ((question == null) ? 0 : question.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + '@' +
                "id=" + id +
                ", answer=" + answer +
                ", result=" + result +
                ", deletedAt=" + deletedAt +
                ", question=" + question;
    }
}
