package by.jwd.testsys.bean;

import java.time.LocalDate;
import java.util.Set;

public class Type {

    private int id;
    private String title;
    private LocalDate deletedAt;
    private Set<Test> tests;

    public Type() {
    }

    public Type(int id, String title, LocalDate deletedAt) {
        this.id = id;
        this.title = title;
        this.deletedAt = deletedAt;
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

    public LocalDate getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDate deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<Test> getTests() {
        return tests;
    }

    public void setTests(Set<Test> tests) {
        this.tests = tests;
    }
}
