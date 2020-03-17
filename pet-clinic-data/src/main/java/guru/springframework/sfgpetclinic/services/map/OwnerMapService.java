package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.springframework.stereotype.Service;

@Service
final class OwnerMapService extends AbstractMapService<Owner> implements OwnerService {

    private final PetTypeService petTypeService;
    private final PetService petService;

    OwnerMapService(PetTypeService petTypeService, PetService petService) {
        this.petTypeService = petTypeService;
        this.petService = petService;
    }

    @Override
    public Owner save(Owner owner) {

        if (owner == null) {
            throw new NullPointerException("Owner cannot be null");
        }

        owner.getPets().forEach(pet -> {

            if (pet.getPetType() == null) {
                throw new NullPointerException("Pet Type of pet '" + pet.getName() + "' cannot be null");
            }

            // Save the pet type id
            if (pet.getPetType().getId() == null) {
                pet.setPetType(petTypeService.save(pet.getPetType()));
            }

            // Save the pet id
            if (pet.getId() == null) {
                pet.setId(petService.save(pet).getId());
            }
        });
        return super.save(owner);
    }

    @Override
    public Owner findByLastName(String lastName) {
        return null;
    }
}
