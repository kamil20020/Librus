package pl.school.librus.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.school.librus.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID>, ListPagingAndSortingRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

}
