package pl.school.librus.person;

import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Embeddable
public class PersonAddress {

    private String city;
    private String street;
    private String postCode;
    private String buildingNumber;
    private String buildingFloor;
    private String doorCode;
}
