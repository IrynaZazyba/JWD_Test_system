package by.jwd.testsys.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.BufferedWriter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public @Getter
@Setter
@ToString
@EqualsAndHashCode
class Type implements Serializable {
    private static final long serialVersionUID = -3178409370985417975L;

    private int id;
    private String title;
    private LocalDate deletedAt;
    private Set<Test> tests;

    public Type() {
    }

    public static class Builder {

        private Type newType;

        public Builder() {
            newType = new Type();
        }

        public Builder withId(int id) {
            newType.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            newType.title = title;
            return this;
        }

        public Builder withDeletedAt(LocalDate deletedAt) {
            newType.deletedAt = deletedAt;
            return this;
        }

        public Builder withTests(Set<Test> tests) {
            newType.tests = tests;
            return this;
        }

        public Type build() {
            return newType;
        }
    }

    public void addTest(Test test) {
        if (tests == null) {
            tests = new HashSet<>();
        }
        tests.add(test);

    }
}
