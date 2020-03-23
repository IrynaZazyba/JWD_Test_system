package by.jwd.testsys.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Type implements Serializable {
    private static final long serialVersionUID = -3178409370985417975L;

    private int id;
    private String title;
    private LocalDate deletedAt;
    private Set<Test> tests;

    public Type() {
    }

    public Type(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Type(int id, String title, Set<Test> tests) {
        this.id = id;
        this.title = title;
        this.tests = new HashSet<>(tests);
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setTests(Test test) {
        if (tests == null) {
            tests = new HashSet<>();
        }
        tests.add(test);
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
