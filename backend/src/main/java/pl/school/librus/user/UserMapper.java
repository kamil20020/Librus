package pl.school.librus.user;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.school.librus.user.api.response.LoggedUserResponse;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    LoggedUserResponse map(UserEntity userEntity);
}
