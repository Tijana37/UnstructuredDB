package mk.ukim.finki.NBNP.repository;

import mk.ukim.finki.NBNP.model.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {

}
