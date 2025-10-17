package is.hi.hbv501g.repository;

import is.hi.hbv501g.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
