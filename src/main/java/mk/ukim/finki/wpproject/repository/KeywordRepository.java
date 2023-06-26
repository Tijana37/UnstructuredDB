package mk.ukim.finki.wpproject.repository;

import mk.ukim.finki.wpproject.model.Keyword;
import org.springframework.data.repository.CrudRepository;

public interface KeywordsRepository extends CrudRepository<Keyword, Long> {
}
