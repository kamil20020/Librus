package pl.school.librus.person;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.school.librus.person.api.request.address.CreateAddressRequest;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper {

    public PersonAddress map(CreateAddressRequest request);
}
