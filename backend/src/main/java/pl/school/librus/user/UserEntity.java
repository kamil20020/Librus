package pl.school.librus.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.school.librus.role.RoleEntity;

import java.util.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "USERS")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String surname;

    @Column
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "USERS_ROLES",
        joinColumns = @JoinColumn(name = "user_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    @ToString.Exclude
    private Set<RoleEntity> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if(roles == null){

            return authorities;
        }

        for(RoleEntity role : roles){

            String roleName = role.getName();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

            authorities.add(authority);
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(username, that.username)) return false;
        if (!Objects.equals(password, that.password)) return false;
        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(firstname, that.firstname)) return false;
        if (!Objects.equals(surname, that.surname)) return false;

        return Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }
}
