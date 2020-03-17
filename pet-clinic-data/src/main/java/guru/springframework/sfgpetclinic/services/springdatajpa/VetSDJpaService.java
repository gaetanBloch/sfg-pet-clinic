package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Vet;
import guru.springframework.sfgpetclinic.repositories.VetRepository;
import guru.springframework.sfgpetclinic.services.VetService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("springdatajpa")
final class VetSDJpaService extends AbstractSDJpaService<Vet, VetRepository> implements VetService {
    VetSDJpaService(VetRepository vetRepository) {
        super(vetRepository);
    }
}
