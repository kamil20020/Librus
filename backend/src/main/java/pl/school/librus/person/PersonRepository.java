package pl.school.librus.person;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<PersonEntity, UUID>, ListPagingAndSortingRepository<PersonEntity, UUID>, JpaSpecificationExecutor<PersonEntity> {

    Optional<PersonEntity> findByUserId(UUID userId);
}
