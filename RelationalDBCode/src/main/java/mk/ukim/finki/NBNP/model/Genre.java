package mk.ukim.finki.NBNP.model;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "Genre")
public class Genre {

    @Id
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "genres")
    private List<Movie> movies = new ArrayList<>();

    public Genre() {

    }

    public Genre(Long id, String name) {
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
