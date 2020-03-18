package by.jwd.testsys.bean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class Test {

    private int id;
    private String title;
    private int key;
    private LocalTime minutes;
    private LocalDate deletedAt;
    private Type type;
    private Set<Question> questions;


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

    public LocalTime getMinutes() {
        return minutes;
    }

    public void setMinutes(LocalTime minutes) {
        this.minutes = minutes;
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
}
