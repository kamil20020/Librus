package pl.school.librus.role;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "ROLES")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    public RoleEntity(String name){

        this.name = name;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoleEntity that = (RoleEntity) o;

        if (!Objects.equals(id, that.id)) {
            return false;
        }

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);

        return result;
    }
}
