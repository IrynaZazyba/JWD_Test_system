package by.jwd.testsys.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class Test implements Serializable {

    private static final long serialVersionUID = -2148195974182493903L;

    private int id;
    private String title;
    private int key;
    private LocalTime duration;
    private LocalDate deletedAt;
    private Type type;
    private Set<Question> questions;
    private int countQuestion;
    private Assignment assignment;


    public Test() {
    }


    public Test(int id, String title, int key, LocalTime duration, LocalDate deletedAt) {
        this.id = id;
        this.title = title;
        this.key = key;
        this.duration = duration;
        this.deletedAt = deletedAt;
    }

    public Test(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Test(int id, String title, int countQuestion, int key,LocalTime duration) {
        this.id = id;
        this.title = title;
        this.key = key;
        this.duration = duration;
        this.countQuestion=countQuestion;
    }

    public Test(int id, String title, Type testType,LocalTime duration, int countQuestion) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.type=testType;
        this.countQuestion=countQuestion;
    }

    public Test(int id, String title,LocalTime duration, int countQuestion) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.countQuestion=countQuestion;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public int getCountQuestion() {
        return countQuestion;
    }

    public void setCountQuestion(int countQuestion) {
        this.countQuestion = countQuestion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public LocalDate getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDate deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        if (questions == null) {
            questions = new HashSet<>();
        }
        questions.add(question);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Test other = (Test) obj;
        if (other.id != this.id) {
            return false;
        }
        if (title == null) {
            if (other.title != null) return false;
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (key != other.key) {
            return false;
        }
        if (duration == null) {
            if (other.duration != null)
                return false;
        } else if (!duration.equals(other.duration)) {
            return false;
        }
        if (deletedAt == null) {
            if (other.deletedAt != null) return false;
        } else if (!deletedAt.equals(other.deletedAt)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) return false;
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (questions == null) {
            if (other.questions != null)
                return false;
        } else if (!questions.equals(other.questions)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = result * prime + id;
        result = result * prime + ((title == null) ? 0 : title.hashCode());
        result = result * prime + key;
        result = result * prime + ((duration == null) ? 0 : duration.hashCode());
        result = result * prime + ((deletedAt == null) ? 0 : deletedAt.hashCode());
        result = result * prime + ((type == null) ? 0 : type.hashCode());
        result = result * prime + ((questions == null ? 0 : questions.hashCode()));
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + '@' +
                "id=" + id +
                ", title=" + title +
                ", key=" + key +
                ", duration=" + duration +
                ", deletedAt=" + deletedAt +
                ", type=" + type +
                ", questions=" + questions;
    }
}
