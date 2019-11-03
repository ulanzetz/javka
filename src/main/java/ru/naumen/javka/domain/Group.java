package ru.naumen.javka.domain;

import javax.persistence.*;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private long creator;

    public Group(String name, long creator) {
        this.name = name;
        this.creator = creator;
    }

    public long getCreator() { return creator;}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
