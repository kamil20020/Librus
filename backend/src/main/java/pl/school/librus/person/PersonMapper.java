package pl.school.librus.person;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.school.librus.person.api.request.address.CreateAddressRequest;
import pl.school.librus.person.api.response.PersonResponse;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper {

    public PersonAddress map(CreateAddressRequest request);

    @Mappings(value = {
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "user.id", target = "userId")
    })
    public PersonResponse map(PersonEntity person);
}
