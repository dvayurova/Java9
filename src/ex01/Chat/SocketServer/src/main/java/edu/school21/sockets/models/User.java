package edu.school21.sockets.models;

public class User {

    private Long id;
    private String email;
    private String password;

    public User() {
    }

    public User(Long id, String email) {
        this.id = id;
        this.email = email;
        password = "";
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }

}
