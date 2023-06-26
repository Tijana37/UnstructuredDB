package mk.ukim.finki.NBNP.repository;

import mk.ukim.finki.NBNP.model.Department;
import mk.ukim.finki.NBNP.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {

    @Query("SELECT pd.department FROM PersonDepartment pd WHERE pd.person = ?1")
    Department findAllDepartmentsById(Long id);

}
