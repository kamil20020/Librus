package pl.school.librus.person;

import jakarta.persistence.Column;
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

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String postCode;

    @Column(nullable = false)
    private String buildingNumber;

    private String buildingFloor;
    private String doorCode;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonAddress that = (PersonAddress) o;

        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (street != null ? !street.equals(that.street) : that.street != null) return false;
        if (postCode != null ? !postCode.equals(that.postCode) : that.postCode != null) return false;
        if (buildingNumber != null ? !buildingNumber.equals(that.buildingNumber) : that.buildingNumber != null)
            return false;
        if (buildingFloor != null ? !buildingFloor.equals(that.buildingFloor) : that.buildingFloor != null)
            return false;

        return doorCode != null ? doorCode.equals(that.doorCode) : that.doorCode == null;
    }

    @Override
    public int hashCode() {

        int result = city != null ? city.hashCode() : 0;
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (postCode != null ? postCode.hashCode() : 0);
        result = 31 * result + (buildingNumber != null ? buildingNumber.hashCode() : 0);
        result = 31 * result + (buildingFloor != null ? buildingFloor.hashCode() : 0);
        result = 31 * result + (doorCode != null ? doorCode.hashCode() : 0);

        return result;
    }
}
