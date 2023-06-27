package mk.ukim.finki.NBNP.repository;

import mk.ukim.finki.NBNP.model.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {
}
