package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Vet;
import guru.springframework.sfgpetclinic.services.SpecialtyService;
import guru.springframework.sfgpetclinic.services.VetService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"default", "map"})
final class VetMapService extends AbstractMapService<Vet> implements VetService {

    private final SpecialtyService specialtyService;

    VetMapService(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @Override
    public Vet save(Vet vet) {

        if (vet == null) {
            throw new NullPointerException("Vet should not be null");
        }

        vet.getSpecialties().forEach(specialty -> {
            if (specialty.getId() == null) {
                specialty.setId(specialtyService.save(specialty).getId());
            }
        });
        return super.save(vet);

    }
}
