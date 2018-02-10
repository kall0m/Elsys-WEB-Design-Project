package healthblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import healthblog.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
