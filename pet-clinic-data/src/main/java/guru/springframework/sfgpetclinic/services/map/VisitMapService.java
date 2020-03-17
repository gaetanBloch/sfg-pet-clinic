package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.VisitService;
import org.springframework.stereotype.Service;

@Service
final class VisitMapService extends AbstractMapService<Visit> implements VisitService {

    @Override
    public Visit save(Visit visit) {
        if (visit.getPet() == null ||
                visit.getPet().getOwner() == null ||
                visit.getPet().getId() == null ||
                visit.getPet().getOwner().getId() == null) {
            throw new IllegalArgumentException("Invalid visit");
        }
        return super.save(visit);
    }
}
