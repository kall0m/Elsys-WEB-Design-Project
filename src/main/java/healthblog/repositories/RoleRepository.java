package healthblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import healthblog.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
