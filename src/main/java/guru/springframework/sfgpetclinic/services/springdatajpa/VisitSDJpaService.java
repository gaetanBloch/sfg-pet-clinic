package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import guru.springframework.sfgpetclinic.services.VisitService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("springdatajpa")
final class VisitSDJpaService extends AbstractSDJpaService<Visit, VisitRepository> implements VisitService {
    VisitSDJpaService(VisitRepository visitRepository) {
        super(visitRepository);
    }
}
