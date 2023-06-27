package mk.ukim.finki.NBNP.repository;

import mk.ukim.finki.NBNP.model.Department;
import mk.ukim.finki.NBNP.model.Person;
import mk.ukim.finki.NBNP.model.PersonDepartment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDepartmentRepository extends CrudRepository <PersonDepartment, Long> {

    @Query("SELECT pd FROM PersonDepartment pd WHERE pd.person = ?1 and pd.department = ?2 and pd.job like ?3")
    PersonDepartment findByPersonAndDepartmentAndJob(Person person, Department department, String jobName);

}
