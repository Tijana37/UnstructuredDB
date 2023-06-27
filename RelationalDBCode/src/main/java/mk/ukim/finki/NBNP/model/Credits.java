package mk.ukim.finki.NBNP.model;

import javax.persistence.*;

@Entity
@Table(name = "Credits")
public class Credits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Person person;

    @Column(columnDefinition = "text")
    private String characterName;

    public Credits(Movie movie, Person person, String characterName) {
        this.movie = movie;
        this.person = person;
        this.characterName = characterName;
    }

    public Credits() {

    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
}
