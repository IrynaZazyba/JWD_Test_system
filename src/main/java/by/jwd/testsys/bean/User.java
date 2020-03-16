package by.jwd.testsys.bean;

import by.jwd.testsys.logic.util.Role;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = -6672256886751595811L;

    private int id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;


    public User() {
    }

    public User(int id, String login, String password, String firstName, String lastName, Role role){
        this.id=id;
        this.login=login;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
        this.role=role;
    }
    public User(String login, String password, String firstName, String lastName, Role role){
        this.login=login;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
        this.role=role;
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }


    public final String getFirstName() {
        return firstName;
    }

    public final void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public final String getLastName() {
        return lastName;
    }

    public final void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public final String getLogin() {
        return login;
    }

    public final void setLogin(String login) {
        this.login = login;
    }

    public final String getPassword() {
        return password;
    }

    public final void setPassword(String password) {
        this.password = password;
    }

    public final Role getRole() {
        return role;
    }

    public final void setRole(Role role) {
        this.role = role;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        User other = (User) obj;
        if(firstName ==null){
            if(other.firstName !=null) return false;
        } else{
            if(!firstName.equals(other.firstName)) return false;
        }
        if(login==null){
            if(other.login!=null) return false;
        } else{
            if(!login.equals(other.login)) return false;
        }
        if(password==null){
            if(other.password!=null) return false;
        } else{
            if(!password.equals(other.password)) return false;
        }
        if(role==null){
            if(other.role!=null) return false;
        } else{
            if(role!=other.role) return false;
        }


      return true;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + '@' +
                "id=" + id +
                ", firstName=" + firstName +
                ", login=" + login +
                ", password=" + password +
                ", role=" + role;
    }
}
