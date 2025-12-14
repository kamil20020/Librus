package pl.school.librus.user.unit;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.school.librus.exception.GlobalExceptionHandler;
import pl.school.librus.security.config.JwtFilter;
import pl.school.librus.security.config.SecurityConfig;
import pl.school.librus.user.UserController;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserMapper;
import pl.school.librus.user.UserService;
import pl.school.librus.user.api.request.RegisterUserRequest;
import pl.school.librus.user.api.response.LoggedUserResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTestUnit {

    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private JwtFilter jwtFilter;

    private static final String API_PREFIX = "/users";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp(){

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    void shouldRegister() throws Exception {

        //given
        RegisterUserRequest request = new RegisterUserRequest(
            "kamil", "KamilKamil1", "kamil@mail.com", "kamil", "nowak", "123"
        );
        String requestStr = objectMapper.writeValueAsString(request);

        UserEntity expectedUser = new UserEntity();

        LoggedUserResponse expectedResponse = new LoggedUserResponse(
            "", "", "", "", "", "", ""
        );

        //when
        Mockito.when(userService.register(any())).thenReturn(expectedUser);
        Mockito.when(userMapper.map(any())).thenReturn(expectedResponse);

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
        LoggedUserResponse gotResponse = objectMapper.readValue(gotResponseStr, LoggedUserResponse.class);

        //then
        assertNotNull(gotResponse);
        assertEquals(expectedResponse, gotResponse);

        Mockito.verify(userService).register(request);
        Mockito.verify(userMapper).map(expectedUser);
    }

    @Test
    void shouldNotRegisterWithDuplicateUsername() throws Exception {

        //when

    }
}