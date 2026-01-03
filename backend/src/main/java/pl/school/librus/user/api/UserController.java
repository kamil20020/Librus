package pl.school.librus.user.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.school.librus.role.RoleEntity;
import pl.school.librus.role.RoleService;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserMapper;
import pl.school.librus.user.UserService;
import pl.school.librus.user.api.request.PatchUserRequest;
import pl.school.librus.user.api.request.RegisterUserRequest;
import pl.school.librus.user.api.response.UserDetailsResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    private final UserMapper userMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsResponse> getById(@PathVariable("userId") String userIdStr){

        UUID userId = UUID.fromString(userIdStr);
        UserEntity gotUser = userService.getById(userId);
        UserDetailsResponse response = userMapper.map(gotUser);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<UserDetailsResponse>> getPage(Pageable pageable){

        Page<UserEntity> foundUsersPage = userService.getPage(pageable);
        Page<UserDetailsResponse> foundUsersResponsePage = foundUsersPage
            .map(user -> userMapper.map(user));

        return ResponseEntity.ok(foundUsersResponsePage);
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<Set<RoleEntity>> getUserRolesPage(@PathVariable("userId") String userIdStr){

        UUID userId = UUID.fromString(userIdStr);
        Set<RoleEntity> gotRoles = userService.getUserRoles(userId);

        return ResponseEntity.ok(gotRoles);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDetailsResponse> register(@RequestBody @Valid RegisterUserRequest request){

        UserEntity createdUser = userService.register(request);
        UserDetailsResponse response = userMapper.map(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<RoleEntity> assignRoleToUser(@PathVariable("userId") UUID userId, @PathVariable("roleId") UUID roleId){

        RoleEntity addedRole = roleService.assignRoleToUser(roleId, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(addedRole);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable("userId") UUID userId, @PathVariable("roleId") UUID roleId){

        roleService.removeRoleFromUser(roleId, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDetailsResponse> patchById(@PathVariable("userId") String userIdStr, @RequestBody PatchUserRequest request){

        UUID userId = UUID.fromString(userIdStr);
        UserEntity patchedUser = userService.patchUser(userId, request);
        UserDetailsResponse response = userMapper.map(patchedUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable("userId") String userIdStr){

        UUID userId = UUID.fromString(userIdStr);
        userService.deleteById(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){

        return ResponseEntity.ok("Test");
    }
}
