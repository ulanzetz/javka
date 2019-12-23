package ru.naumen.javka.domain;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "files")
public class File {
    @Id
    private String id;

    private String name;

    private String path;

    private long creator;

    private String description;

    public File(String id, String name, long creator, Optional<String> parentId, String description, boolean isFolder) {
        this.id = id;
        this.path = isFolder ? "" : id;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.parentId = parentId.orElse(null);
    }

    @Column(name = "parent_id")
    private String parentId;

    public File() {

    }

    public String getId() {
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

    public String getParentId() {
        return parentId;
    }
}
