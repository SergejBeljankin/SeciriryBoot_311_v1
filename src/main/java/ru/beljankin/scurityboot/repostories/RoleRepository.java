package ru.beljankin.scurityboot.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.beljankin.scurityboot.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
