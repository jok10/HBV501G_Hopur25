package is.hi.hbv501g.repository;

import is.hi.hbv501g.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

}
