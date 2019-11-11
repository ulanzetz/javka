package ru.naumen.javka.domain;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(name = "password_hash")
    private String passwordHash;

    public long getId() {
        return id;
    }

    public User() {

    }

    public User(String name, String passwordHash) {
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void cleanPasswordHash() {
        passwordHash = null;
    }
}
