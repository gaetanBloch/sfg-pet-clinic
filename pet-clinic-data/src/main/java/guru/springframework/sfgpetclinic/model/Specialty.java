package guru.springframework.sfgpetclinic.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "specialties")
public class Specialty extends BaseEntity {
    @Column(name = "description")
    private String description;
    @ManyToMany(mappedBy = "specialties")
    private Set<Vet> vets = new HashSet<>();

    @Builder
    public Specialty(Long id, String description, @Singular Set<Vet> vets) {
        super(id);
        this.description = description;
        if (vets != null) {
            this.vets = vets;
        }
    }
}
