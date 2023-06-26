package mk.ukim.finki.NBNP.model;

import java.io.Serializable;

public class RatingId implements Serializable {
    private Long userId;

    private Long movieId;

    // default constructor

    public RatingId(Long userId, Long movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    // equals() and hashCode()
}
