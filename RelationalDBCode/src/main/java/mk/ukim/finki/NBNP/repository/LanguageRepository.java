package mk.ukim.finki.NBNP.repository;

import mk.ukim.finki.NBNP.model.Language;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends CrudRepository<Language, Long> {

    @Query("SELECT l FROM Language l WHERE l.iso_lang_code like ?1")
    Language findByIso_lang_code(String iso_lang_code);

}
