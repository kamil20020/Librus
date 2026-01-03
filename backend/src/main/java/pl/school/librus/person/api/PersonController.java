package pl.school.librus.person.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.school.librus.person.PersonEntity;
import pl.school.librus.person.PersonService;
import pl.school.librus.person.api.request.person.CreatePersonRequest;
import pl.school.librus.person.api.request.person.PatchPersonRequest;
import pl.school.librus.person.api.request.person.SearchPersonRequest;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/persons")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<Page<PersonEntity>> getPage(@RequestBody SearchPersonRequest request, Pageable pageable){

        Page<PersonEntity> foundPersons = personService.getPage(request, pageable);

        return ResponseEntity.ok(foundPersons);
    }

    @PostMapping
    public ResponseEntity<PersonEntity> create(@RequestBody @Valid CreatePersonRequest request){

        PersonEntity createdPerson = personService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
    }

    @PatchMapping
    public ResponseEntity<PersonEntity> patch(@RequestBody @Valid PatchPersonRequest request){

        PersonEntity patchedPerson = personService.patch(request);

        return ResponseEntity.ok(patchedPerson);
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> deleteById(@PathVariable("personId") String personIdStr){

        UUID personId = UUID.fromString(personIdStr);

        personService.deletePersonById(personId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
