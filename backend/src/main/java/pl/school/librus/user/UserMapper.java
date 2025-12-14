package pl.school.librus.user;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.school.librus.user.api.request.RegisterUserRequest;
import pl.school.librus.user.api.response.UserDetailsResponse;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserEntity map(RegisterUserRequest request);
    UserDetailsResponse map(UserEntity userEntity);
}
