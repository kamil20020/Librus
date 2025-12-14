package pl.school.librus.user.unit;

import org.junit.jupiter.api.Test;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserMapper;
import pl.school.librus.user.UserMapperImpl;
import pl.school.librus.user.api.request.RegisterUserRequest;
import pl.school.librus.user.api.response.UserDetailsResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private static final UserMapper userMapper = new UserMapperImpl();

    @Test
    void shouldMapResponseToEntity() {

        //given
        UserEntity user = UserEntity.builder()
            .id(UUID.randomUUID())
            .username("kamil")
            .email("kamil@mail.com")
            .firstname("kamil")
            .surname("nowak")
            .phone("123")
            .build();

        //when
        UserDetailsResponse gotResponse = userMapper.map(user);

        //then
        assertNotNull(gotResponse);
        assertEquals(user.getId().toString(), gotResponse.id());
        assertEquals(user.getUsername(), gotResponse.username());
        assertEquals(user.getEmail(), gotResponse.email());
        assertEquals(user.getFirstname(), gotResponse.firstname());
        assertEquals(user.getSurname(), gotResponse.surname());
        assertEquals(user.getPhone(), gotResponse.phone());
    }

    @Test
    void shouldMapResponseToEntityWithNull() {

        //given
        //when
        UserDetailsResponse gotResponse = userMapper.map((UserEntity) null);

        //then
        assertNull(gotResponse);
    }

    @Test
    void shouldMapRequestToEntity() {

        //given
        RegisterUserRequest request = new RegisterUserRequest(
            "kamil",
            "password",
            "kamil@mail.com",
            "kamil",
            "nowak",
            "123"
        );

        //when
        UserEntity gotUser = userMapper.map(request);

        //then
        assertNotNull(gotUser);
        assertEquals(request.username(), gotUser.getUsername());
        assertEquals(request.email(), gotUser.getEmail());
        assertEquals(request.password(), gotUser.getPassword());
        assertEquals(request.firstname(), gotUser.getFirstname());
        assertEquals(request.surname(), gotUser.getSurname());
        assertEquals(request.phone(), gotUser.getPhone());
    }

    @Test
    void shouldMapRequestToEntityWhenIsNull() {

        //given
        //when
        UserEntity gotUser = userMapper.map((RegisterUserRequest) null);

        //then
        assertNull(gotUser);
    }
}