package mk.ukim.finki.NBNP.model;

import javax.persistence.*;

@Entity
@Table(name = "movies_keywords")
public class MovieKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Keyword keyword;

    public MovieKeyword() {
    }

    public MovieKeyword(Movie movie, Keyword keyword) {
        this.movie = movie;
        this.keyword = keyword;
    }
}
