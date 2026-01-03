package pl.school.librus.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import pl.school.librus.person.PersonService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @WithMockUser
    @Test
    public void shouldGetIsUserLoggedWhenUserIsLogged() {

        assertTrue(authService.isUserLogged());
    }

    @Test
    public void shouldGetIsUserLoggedWhenUserIsNotLogged(){

        assertFalse(authService.isUserLogged());
    }
}