package by.jwd.testsys.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class Question implements Serializable {

    private static final long serialVersionUID = 1068955883794784134L;

    private int id;
    private String question;
    private LocalDate deletedAt;
    private Test test;
    private Set<Answer> answers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LocalDate getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDate deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Question other = (Question) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.question == null) {
            if (other.question != null) return false;
        } else if (!question.equals(other.question)) {
            return false;
        }
        if (deletedAt == null) {
            if (other.deletedAt != null) return false;
        } else if (!deletedAt.equals(other.deletedAt)) {
            return false;
        }
        if (test == null) {
            if (other.test != null) return false;
        } else if (!test.equals(other.test)) {
            return false;
        }
        if(answers==null){
            if(other.answers!=null)return false;
        }else if(!answers.equals(other.answers)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((question == null) ? 0 : question.hashCode());
        result = prime * result + ((deletedAt == null) ? 0 : deletedAt.hashCode());
        result = prime * result + ((test == null) ? 0 : test.hashCode());
        result = prime * result + ((answers == null) ? 0 : answers.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + '@' +
                "id=" + id +
                ", question=" + question +
                ", deletedAt=" + deletedAt +
                ", test=" + test +
                ", answers=" + answers;
    }
}
