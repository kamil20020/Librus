package pl.school.librus.person;

import jakarta.persistence.*;
import lombok.*;
import pl.school.librus.user.UserEntity;

import java.util.Objects;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "PERSONS")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @Embedded
    private PersonAddress address;

    @JoinColumn(name = "user_id")
    @OneToOne(optional = false)
    private UserEntity user;

    public static String clearPhone(String phone){

        if(phone == null || phone.isBlank()){

            return phone;
        }

        return phone
            .replaceAll("-", "")
            .replaceAll("/", "")
            .replaceAll("\\s", "")
            .replaceAll("\\+48", "");
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonEntity person = (PersonEntity) o;

        if (!Objects.equals(id, person.id)) return false;
        if (!Objects.equals(firstname, person.firstname)) return false;
        if (!Objects.equals(surname, person.surname)) return false;
        if (!Objects.equals(email, person.email)) return false;
        if (!Objects.equals(phone, person.phone)) return false;

        return Objects.equals(address, person.address);
    }

    @Override
    public int hashCode() {

        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);

        return result;
    }
}
