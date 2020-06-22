package by.jwd.testsys.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

public @Getter
@Setter
@EqualsAndHashCode
@ToString
class User implements Serializable {

    private static final long serialVersionUID = -6672256886751595811L;

    private int id;
    private String login;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private Set<Assignment> assignment;

    public User() {
    }

    public static class Builder {

        private User newUser;

        public Builder() {
            newUser = new User();
        }

        public Builder withId(int id) {
            newUser.id = id;
            return this;
        }

        public Builder withLogin(String login) {
            newUser.login = login;
            return this;
        }

        public Builder withPassword(String password) {
            newUser.password = password;
            return this;
        }

        public Builder withEmail(String email) {
            newUser.email = email;
            return this;
        }

        public Builder withFirstName(String firstName) {
            newUser.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            newUser.lastName = lastName;
            return this;
        }

        public Builder withRole(Role role) {
            newUser.role = role;
            return this;
        }

        public Builder withAssignment(Set<Assignment> assignment) {
            newUser.assignment = assignment;
            return this;
        }

        public User build() {
            return newUser;
        }
    }

}
