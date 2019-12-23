package ru.naumen.javka.domain;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String path;

    private long creator;

    private String description;

    public File(String name, long creator, long parentId,  String description, boolean isFolder) {
        this.name = name;
        this.path = isFolder? "" : String.format("%s_%s_%s", creator, parentId, name);
        this.description = description;
        this.creator = creator;
        this.parentId = parentId;
    }

    private Long parentId;

    public File() {

    }

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

    public Long getCreator() {
        return creator;
    }

    public long getParentId() {
        return parentId;
    }
}
