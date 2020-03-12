package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Vet;

public class VetServiceMap extends AbstractMapService<Vet, Long> {

    @Override
    public Vet save(Vet vet) {
        return super.save(vet.getId(), vet);
    }
}
