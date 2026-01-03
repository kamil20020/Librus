package pl.school.librus.person;

import org.springframework.data.jpa.domain.Specification;

public interface PersonSpecification {

    public static Specification<PersonEntity> personAboutFirstname(String firstname){

        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.upper(root.get("firstname")),
                "%" + firstname.toUpperCase().trim() + "%"
            );
    }

    public static Specification<PersonEntity> personAboutSurname(String surname){

        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.upper(root.get("surname")),
                "%" + surname.toUpperCase().trim() + "%"
            );
    }

    public static Specification<PersonEntity> personAboutPhone(String phone){

        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.upper(root.get("phone")),
                "%" + phone.toUpperCase().trim() + "%"
            );
    }

    public static Specification<PersonEntity> personAboutCity(String city){

        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.upper(root.get("address").get("city")),
                "%" + city.toUpperCase().trim() + "%"
            );
    }
}
