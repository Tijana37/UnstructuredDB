package mk.ukim.finki.NBNP.repository;

import mk.ukim.finki.NBNP.model.MovieKeyword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieKeywordRepository extends CrudRepository<MovieKeyword, Long> {
}
