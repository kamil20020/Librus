package pl.school.librus.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, UUID>, ListCrudRepository<RoleEntity, UUID> {

    public boolean existsByNameContaining(String name);
}
