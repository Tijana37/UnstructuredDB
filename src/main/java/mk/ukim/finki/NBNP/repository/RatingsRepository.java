package mk.ukim.finki.NBNP.repository;

import mk.ukim.finki.NBNP.model.Rating;
import org.springframework.data.repository.CrudRepository;

public interface RatingsRepository extends CrudRepository<Rating, Long> {
}
