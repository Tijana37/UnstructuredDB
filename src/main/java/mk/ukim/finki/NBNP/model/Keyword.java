package mk.ukim.finki.NBNP.model;

import javax.persistence.*;

@Entity
@Table(name = "Keyword")
public class Keyword {

    @Id
    private Long id;
    private String name;

    public Keyword() {

    }

    public Keyword(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
