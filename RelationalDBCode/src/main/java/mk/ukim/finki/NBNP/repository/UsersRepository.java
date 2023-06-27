package mk.ukim.finki.NBNP.repository;

import mk.ukim.finki.NBNP.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {
}
