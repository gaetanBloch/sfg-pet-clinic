package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Pet;

public class PetServiceMap extends AbstractMapService<Pet, Long> {

    @Override
    public Pet save(Pet pet) {
        return super.save(pet.getId(), pet);
    }
}
