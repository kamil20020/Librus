package pl.school.librus.role;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserRepository;
import pl.school.librus.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    public RoleEntity getById(UUID id) throws EntityNotFoundException{

        Optional<RoleEntity> gotRoleOpt = roleRepository.findById(id);

        if(gotRoleOpt.isEmpty()){
            throw new EntityNotFoundException("Nie istnieje rola o id " + id);
        }

        return gotRoleOpt.get();
    }

    public List<RoleEntity> getAll(){

        return roleRepository.findAll();
    }


    public Page<UserEntity> getRoleUsers(UUID roleId, Pageable pageable) throws EntityNotFoundException{

        if(pageable == null){
            pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        }

        if(pageable.getPageSize() > MAX_PAGE_SIZE){
            pageable = PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE);
        }

        return userRepository.findAllByRoles_Id(roleId, pageable);
    }

    public RoleEntity create(String name) throws EntityExistsException {

        if(roleRepository.existsByNameContaining(name)){
            throw new EntityExistsException("Istnieje już rola o nazwie " + name);
        }

        RoleEntity newRoleData = new RoleEntity(name);

        return roleRepository.save(newRoleData);
    }

    @Transactional
    public RoleEntity assignRoleToUser(UUID roleId, UUID userId) throws EntityNotFoundException{

        RoleEntity gotRole = getById(roleId);
        UserEntity gotUser = userService.getById(userId);

        gotUser.getRoles().add(gotRole);

        return gotRole;
    }

    @Transactional
    public void removeRoleFromUser(UUID roleId, UUID userId) throws EntityNotFoundException{

        RoleEntity gotRole = getById(roleId);
        UserEntity gotUser = userService.getById(userId);

        if(!userRepository.existsByIdAndRoles_Id(userId, roleId)){
            throw new EntityNotFoundException("Użytkownik o id " + userId + " nie ma przypisanej roli o id " + roleId);
        }

        gotUser.getRoles().remove(gotRole);
    }

    @Transactional
    public RoleEntity patchById(UUID id, String name){

        RoleEntity gotRole = getById(id);

        gotRole.setName(name);

        return gotRole;
    }

    @Transactional
    public void deleteById(UUID id) throws EntityNotFoundException{

        if(!roleRepository.existsById(id)){
            throw new EntityNotFoundException("Nie istnieje rola o id " + id);
        }

        roleRepository.deleteById(id);
    }
}
