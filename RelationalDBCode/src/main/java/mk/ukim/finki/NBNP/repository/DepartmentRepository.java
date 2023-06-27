package mk.ukim.finki.NBNP.repository;

import mk.ukim.finki.NBNP.model.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

    Department findByNameLike (String name);

}
