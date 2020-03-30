package guru.springframework.sfgpetclinic.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class Person extends BaseEntity {
    @Column(name = "first_name")
    @NotEmpty
    private String firstName;
    @Column(name = "last_name")
    @NotEmpty
    private String lastName;

    public Person(Long id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
