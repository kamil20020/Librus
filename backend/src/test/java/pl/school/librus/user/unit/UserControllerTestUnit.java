package pl.school.librus.user.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.school.librus.role.RoleService;
import pl.school.librus.security.config.JwtFilter;
import pl.school.librus.user.api.UserController;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserMapper;
import pl.school.librus.user.UserService;
import pl.school.librus.user.api.request.RegisterUserRequest;
import pl.school.librus.user.api.response.UserDetailsResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTestUnit {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private RoleService roleService;

    @MockBean
    private JwtFilter jwtFilter;

    private static final String API_PREFIX = "/users";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldRegister() throws Exception {

        //given
        RegisterUserRequest request = new RegisterUserRequest(
            "kamil", "KamilKamil1", "kamil@mail.com", "123"
        );
        String requestStr = objectMapper.writeValueAsString(request);

        UserEntity expectedUser = new UserEntity();

        UserDetailsResponse expectedResponse = new UserDetailsResponse(
            "", "", "", ""
        );

        //when
        Mockito.when(userService.register(any())).thenReturn(expectedUser);
        Mockito.when(userMapper.map(any(UserEntity.class))).thenReturn(expectedResponse);

        MvcResult gotResult = mockMvc
            .perform(
                post(API_PREFIX + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestStr)
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

        String gotResponseStr = gotResult.getResponse().getContentAsString();
        UserDetailsResponse gotResponse = objectMapper.readValue(gotResponseStr, UserDetailsResponse.class);

        //then
        assertNotNull(gotResponse);
        assertEquals(expectedResponse, gotResponse);

        Mockito.verify(userService).register(request);
        Mockito.verify(userMapper).map(expectedUser);
    }

    @Test
    void shouldNotRegisterWithDuplicateUsername() throws Exception {

        //given
        RegisterUserRequest request = new RegisterUserRequest(
            "kamil", "KamilKamil1", "kamil@mail.com", "123"
        );
        String requestStr = objectMapper.writeValueAsString(request);

        //when
        Mockito.when(userService.register(any())).thenThrow(EntityExistsException.class);

        mockMvc
            .perform(
                post(API_PREFIX + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestStr)
            )
            .andDo(print())
            .andExpect(status().isConflict());

        //then
        Mockito.verify(userService).register(request);
    }
}