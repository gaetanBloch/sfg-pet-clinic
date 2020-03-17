package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.repositories.PetTypeRepository;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("springdatajpa")
final class PetTypeSDJpaService extends AbstractSDJpaService<PetType, PetTypeRepository> implements PetTypeService {

    PetTypeSDJpaService(PetTypeRepository petTypeRepository) {
        super(petTypeRepository);
    }
}
