package mk.ukim.finki.NBNP.model;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "Collection")
public class Collection {

    @Id
    private Long id;
    private String name;

    @OneToMany (mappedBy = "collection")
    List<Movie> movies = new ArrayList<>();

    public Collection() {

    }

    public Collection(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
