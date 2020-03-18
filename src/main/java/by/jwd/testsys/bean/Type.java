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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Type other = (Type) obj;
        if (title == null) {
            if (other.title != null) return false;
        } else {
            if (!title.equals(other.title)) return false;
        }
        if (deletedAt == null) {
            if (other.deletedAt != null) return false;
        } else {
            if (!deletedAt.equals(other.deletedAt)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((deletedAt == null) ? 0 : deletedAt.hashCode());
        result = prime * result + ((tests == null) ? 0 : tests.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + '@' +
                "id=" + id +
                ", title=" + title +
                ", deletedAt=" + deletedAt +
                ", tests=" + tests;
    }
}
