package pl.school.librus.user.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.school.librus.user.UserController;
import pl.school.librus.user.UserMapper;
import pl.school.librus.user.UserService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTestSecurity {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private static final String API_PREFIX = "/users";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldNotDeleteUserWithoutLogin() throws Exception {

        //given
        UUID userId = UUID.randomUUID();

        //when
        //then
        mockMvc
            .perform(
                delete(API_PREFIX + "/{userId}", userId)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldNotGetUsersWithoutLogin() throws Exception {

        //given
        //when
        //then
        mockMvc
            .perform(
                get(API_PREFIX)
                .param("size", "10")
                .param("page", "0")
            )
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }
}
