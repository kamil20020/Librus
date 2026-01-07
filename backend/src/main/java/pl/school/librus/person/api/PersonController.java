package pl.school.librus.person.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.school.librus.person.PersonEntity;
import pl.school.librus.person.PersonMapper;
import pl.school.librus.person.PersonService;
import pl.school.librus.person.api.request.person.CreatePersonRequest;
import pl.school.librus.person.api.request.person.PatchPersonRequest;
import pl.school.librus.person.api.request.person.SearchPersonRequest;
import pl.school.librus.person.api.response.PersonResponse;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/persons")
public class PersonController {

    private final PersonService personService;

    private final PersonMapper personMapper;

    @PostMapping("/search")
    public ResponseEntity<Page<PersonResponse>> getPage(@RequestBody SearchPersonRequest request, Pageable pageable){

        Page<PersonEntity> foundPersons = personService.getPage(request, pageable);
        Page<PersonResponse> foundPersonsResponse = foundPersons.map(person -> personMapper.map(person));

        return ResponseEntity.ok(foundPersonsResponse);
    }

    @PostMapping
    public ResponseEntity<PersonResponse> create(@RequestBody @Valid CreatePersonRequest request){

        PersonEntity createdPerson = personService.create(request);
        PersonResponse personResponse = personMapper.map(createdPerson);

        return ResponseEntity.status(HttpStatus.CREATED).body(personResponse);
    }

    @PatchMapping
    public ResponseEntity<PersonResponse> patch(@RequestBody @Valid PatchPersonRequest request){

        PersonEntity patchedPerson = personService.patch(request);
        PersonResponse personResponse = personMapper.map(patchedPerson);

        return ResponseEntity.ok(personResponse);
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> deleteById(@PathVariable("personId") String personIdStr){

        UUID personId = UUID.fromString(personIdStr);

        personService.deletePersonById(personId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
