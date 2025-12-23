package pl.school.librus.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID>, ListPagingAndSortingRepository<UserEntity, UUID> {

    boolean existsByUsername(String username);
    boolean existsByIdAndRoles_Id(UUID userId, UUID roleId);
    Optional<UserEntity> findByUsername(String username);
    Page<UserEntity> findAllByRoles_Id(UUID roleId, Pageable pageable);
}
