package ru.yandex.practicum.models;

public class Courier {
    private String login;
    private String password;
    private String firstName;
    private Integer courierId;

    public Courier() {
    }

    public String getLogin() {
        return login;
    }

    public Courier withLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Courier withPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Courier withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public Courier withCourierId(Integer courierId) {
        this.courierId = courierId;
        return this;
    }
}
