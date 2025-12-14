package pl.school.librus.user;

import org.junit.jupiter.api.Test;
import pl.school.librus.user.api.response.LoggedUserResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private static final UserMapper userMapper = new UserMapperImpl();

    @Test
    void shouldMap() {

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
        LoggedUserResponse gotResponse = userMapper.map(user);

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
    void shouldMapWithNull() {

        //given
        //when
        LoggedUserResponse gotResponse = userMapper.map(null);

        //then
        assertNull(gotResponse);
    }
}