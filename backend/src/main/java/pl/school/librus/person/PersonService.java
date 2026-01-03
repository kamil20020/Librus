package pl.school.librus.person;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.school.librus.person.api.request.person.CreatePersonRequest;
import pl.school.librus.person.api.request.address.PatchAddressRequest;
import pl.school.librus.person.api.request.person.PatchPersonRequest;
import pl.school.librus.person.api.request.person.SearchPersonRequest;
import pl.school.librus.security.AuthService;
import pl.school.librus.user.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    private final AuthService authService;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<PersonEntity> getPage(SearchPersonRequest request, Pageable pageable){

        if(pageable == null){

            pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        }

        if(pageable.getPageSize() > MAX_PAGE_SIZE){

            pageable = PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE);
        }

        if(request == null){

            return personRepository.findAll(pageable);
        }

        List<Specification<PersonEntity>> specifications = new ArrayList<>();

        if(isNotNullAndNotBlank(request.firstname())){

            specifications.add(PersonSpecification.personAboutFirstname(request.firstname()));
        }

        if(isNotNullAndNotBlank(request.surname())){

            specifications.add(PersonSpecification.personAboutSurname(request.surname()));
        }

        if(isNotNullAndNotBlank(request.phone())){

            specifications.add(PersonSpecification.personAboutPhone(request.phone()));
        }

        if(isNotNullAndNotBlank(request.city())){

            specifications.add(PersonSpecification.personAboutCity(request.city()));
        }

        if(isNotNullAndNotBlank(request.searchText())){

            String[] words = request.searchText().split("\\s");

            if(words.length > 2){

                Specification<PersonEntity> specification = Specification.anyOf(
                    Specification.allOf(
                        PersonSpecification.personAboutFirstname(words[0]),
                        PersonSpecification.personAboutSurname(words[1])
                    ),
                    Specification.allOf(
                        PersonSpecification.personAboutFirstname(words[1]),
                        PersonSpecification.personAboutSurname(words[0])
                    )
                );

                specifications.add(specification);
            }
            else{

                String word = words[0];

                Specification<PersonEntity> specification = Specification.anyOf(
                    PersonSpecification.personAboutFirstname(word),
                    PersonSpecification.personAboutSurname(word),
                    PersonSpecification.personAboutPhone(word)
                );

                specifications.add(specification);
            }
        }

        return personRepository.findAll(Specification.allOf(specifications), pageable);
    }

    @Transactional
    public PersonEntity create(CreatePersonRequest request){

        PersonAddress address = personMapper.map(request.address());
        PersonEntity newPerson = PersonEntity.builder()
            .firstname(request.firstname())
            .surname(request.surname())
            .email(request.email())
            .phone(request.phone())
            .build();
        newPerson.setAddress(address);

        return create(newPerson);
    }

    @Transactional
    public PersonEntity create(PersonEntity newPerson){

        UserEntity loggedUser = authService.getLoggedUser();

        PersonEntity savedPerson = personRepository.save(newPerson);

        loggedUser.setPerson(savedPerson);

        return savedPerson;
    }

    @Transactional
    public PersonEntity patch(PatchPersonRequest request) throws IllegalStateException{

        UserEntity loggedUser = authService.getLoggedUser();

        if(loggedUser.getPerson() == null){

            throw new IllegalStateException("Użytkownik nie ma podanych danych osobowych");
        }

        PersonEntity person = loggedUser.getPerson();

        if(isNotNullAndNotBlank(request.firstname())){

            person.setFirstname(request.firstname());
        }

        if(isNotNullAndNotBlank(request.surname())){

            person.setFirstname(request.surname());
        }

        if(isNotNullAndNotBlank(request.email())){

            person.setFirstname(request.email());
        }

        if(isNotNullAndNotBlank(request.phone())){

            person.setFirstname(request.phone());
        }

        if(request.address() != null){

            PersonAddress personAddress = person.getAddress();
            PatchAddressRequest patchAddressRequest = request.address();
            PersonAddress changedAddress = patchAddress(personAddress, patchAddressRequest);

            person.setAddress(changedAddress);
        }

        return person;
    }

    private PersonAddress patchAddress(PersonAddress address, PatchAddressRequest request){

        if(isNotNullAndNotBlank(request.city())){

            address.setCity(request.city());
        }

        if(isNotNullAndNotBlank(request.street())){

            address.setCity(request.street());
        }

        if(isNotNullAndNotBlank(request.postCode())){

            address.setCity(request.postCode());
        }

        if(isNotNullAndNotBlank(request.buildingNumber())){

            address.setCity(request.buildingNumber());
        }

        if(isNotNullAndNotBlank(request.buildingFloor())){

            address.setCity(request.buildingFloor());
        }

        if(isNotNullAndNotBlank(request.doorCode())){

            address.setCity(request.doorCode());
        }

        return address;
    }

    @Transactional
    public void deletePersonById(UUID personId) throws EntityNotFoundException{

        if(!personRepository.existsById(personId)){

            throw new EntityNotFoundException("Nie znaleziono danych osobowych o id " + personId);
        }

        personRepository.deleteById(personId);
    }

    private static boolean isNotNullAndNotBlank(String value){

        return value != null && !value.isBlank();
    }
}
