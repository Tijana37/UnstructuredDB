package mk.ukim.finki.NBNP.model;

import javax.persistence.*;

@Entity
//@IdClass(RatingId.class)
@Table(name = "Rating")
public class Rating {

    /*

    @Id
    private Long userId;

    @Id
    private Long movieId;

     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private Float rating;

    private Integer timeStamp;

    public Rating() {

    }

    public Rating(Users user, Movie movie, Float rating, Integer timeStamp) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.timeStamp = timeStamp;
    }
}
