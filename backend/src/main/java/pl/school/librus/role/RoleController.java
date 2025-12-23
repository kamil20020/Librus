package pl.school.librus.role;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.school.librus.role.api.request.CreateRoleRequest;
import pl.school.librus.role.api.request.PatchRoleRequest;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserMapper;
import pl.school.librus.user.UserService;
import pl.school.librus.user.api.response.UserDetailsResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/roles")
public class RoleController {

    private final RoleService roleService;
    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<RoleEntity>> getAll(){

        List<RoleEntity> foundRoles = roleService.getAll();

        return ResponseEntity.ok(foundRoles);
    }

    @GetMapping("/{roleId}/users")
    public ResponseEntity<Page<UserDetailsResponse>> getRoleUsers(@PathVariable("roleId") String roleIdStr, Pageable pageable){

        UUID roleId = UUID.fromString(roleIdStr);

        Page<UserEntity> foundUsersPage = roleService.getRoleUsers(roleId, pageable);
        Page<UserDetailsResponse> foundUsersDetailsPage = foundUsersPage.map(user -> userMapper.map(user));

        return ResponseEntity.ok(foundUsersDetailsPage);
    }

    @PostMapping
    public ResponseEntity<RoleEntity> create(@RequestBody CreateRoleRequest request){

        String roleName = request.name();

        RoleEntity createdRole = roleService.create(roleName);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PatchMapping("/{roleId}")
    public ResponseEntity<RoleEntity> patchById(@PathVariable("roleId") String roleIdStr, @RequestBody PatchRoleRequest request){

        UUID roleId = UUID.fromString(roleIdStr);
        String newName = request.name();
        RoleEntity changedRole = roleService.patchById(roleId, newName);

        return ResponseEntity.ok(changedRole);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteById(@PathVariable("roleId") String roleIdStr){

        UUID roleId = UUID.fromString(roleIdStr);

        roleService.deleteById(roleId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
