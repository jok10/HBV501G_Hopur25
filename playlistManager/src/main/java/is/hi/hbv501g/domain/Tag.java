package is.hi.hbv501g.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;
    private String name;

    public Tag() {}

    // Getters
    public Long getTagId() { return tagId; }
    public String getName() { return name; }

    // Setters
    public void setTagId(Long tagId) { this.tagId = tagId; }
    public void setName(String name) { this.name = name; }
}
