package ru.naumen.javka.domain;

import com.sun.istack.internal.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String path;

    private long creator;

    private String description;

    public File(String name, String path, String description, long creator){
        this.name = name;
        this.path = path;
        this.description = description;
        this.creator = creator;
    }

    @Nullable
    private long parentId;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public long getParentId() {return parentId;}
}
