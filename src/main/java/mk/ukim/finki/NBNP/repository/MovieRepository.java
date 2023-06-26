package mk.ukim.finki.NBNP.repository;

import mk.ukim.finki.NBNP.model.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {



}
