package ru.naumen.javka.domain;

import javax.persistence.*;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private long creator;

    public Group() {}

    public Group(String name, long creator) {
        this.name = name;
        this.creator = creator;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCreator() {
        return creator;
    }
}
