package it.unibs.pajc.lithium.db.om;

import it.unibs.pajc.db.Column;
import it.unibs.pajc.db.Id;
import it.unibs.pajc.db.Table;

import java.io.Serializable;

@Table(name = "genre")
public class Genre implements Serializable {
    @Id
    @Column(name = "genre_id")
    private Integer id;

    @Column(name = "genre_name")
    private String name;

    @Column(name = "genre_description")
    private String description;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
