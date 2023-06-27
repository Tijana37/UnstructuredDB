package mk.ukim.finki.NBNP.repository;

import mk.ukim.finki.NBNP.model.Country;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {

    @Query("SELECT c FROM Country c WHERE c.iso_country_code = ?1")
    Country findByIso_country_code(String Iso_country_code);

}
