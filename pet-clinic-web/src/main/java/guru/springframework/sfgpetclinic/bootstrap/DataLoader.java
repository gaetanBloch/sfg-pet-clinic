package guru.springframework.sfgpetclinic.bootstrap;

import guru.springframework.sfgpetclinic.model.*;
import guru.springframework.sfgpetclinic.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
final class DataLoader implements CommandLineRunner {

    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeService petTypeService;
    private final SpecialtyService specialtyService;
    private final VisitService visitService;

    public DataLoader(OwnerService ownerService,
                      VetService vetService,
                      PetTypeService petTypeService,
                      SpecialtyService specialtyService,
                      VisitService visitService) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeService = petTypeService;
        this.specialtyService = specialtyService;
        this.visitService = visitService;
    }

    @Override
    public void run(String... args) throws Exception {

        int count = petTypeService.findAll().size();

        // We load data only when the DB is empty
        if (count == 0) {
            loadData();
        }
    }

    private void loadData() {
        PetType savedDogPetType = petTypeService.save(PetType.builder().name("dog").build());
        PetType savedCatPetType = petTypeService.save(PetType.builder().name("cat").build());

        Specialty savedRadiology = specialtyService.save(Specialty.builder().description("radiology").build());
        Specialty savedSurgery = specialtyService.save(Specialty.builder().description("surgery").build());
        Specialty savedDentistry = specialtyService.save(Specialty.builder().description("dentistry").build());

        Owner owner1 = Owner.builder()
                .firstName("Michael")
                .lastName("Weston")
                .address("123 Brickerel")
                .city("Miami")
                .telephone("1321313233")
                .build();
        Pet mikesPet = Pet.builder()
                .petType(savedDogPetType)
                .owner(owner1)
                .birthDate(LocalDate.now())
                .name("Rosco")
                .build();
        owner1.getPets().add(mikesPet);
        ownerService.save(owner1);

        Owner owner2 = Owner.builder()
                .firstName("Fiona")
                .lastName("Glenanne")
                .address("123 Brickerel")
                .city("Miami")
                .telephone("123123132132")
                .build();
        Pet fionasPet = Pet.builder()
                .petType(savedCatPetType)
                .owner(owner1)
                .birthDate(LocalDate.now())
                .name("Soleil")
                .build();
        owner2.getPets().add(fionasPet);
        ownerService.save(owner2);

        visitService.save(Visit.builder().pet(fionasPet).date(LocalDate.now()).description("Sneezy Kitty").build());

        vetService.save(Vet.builder().firstName("Sam").lastName("Axe").specialty(savedRadiology).build());
        vetService.save(Vet.builder().firstName("Jessie").lastName("Porter").specialty(savedSurgery).build());
    }
}
