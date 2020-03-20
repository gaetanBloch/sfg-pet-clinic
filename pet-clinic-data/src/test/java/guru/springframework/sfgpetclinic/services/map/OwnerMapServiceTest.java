package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OwnerMapServiceTest {

    static final Long ID = 1L;
    static final String LAST_NAME = "Smith";
    OwnerMapService ownerMapService;


    @BeforeEach
    void setUp() {
        ownerMapService = new OwnerMapService(new PetTypeMapService(), new PetMapService());
        ownerMapService.save(Owner.builder().id(ID).lastName(LAST_NAME).build());
    }

    @Test
    public void findAll() {
        // Given
        Set<Owner> owners;

        // When
        owners = ownerMapService.findAll();

        // Then
        assertEquals(1, owners.size());
    }

    @Test
    public void findById() {
        // Given
        Owner owner;

        // When
        owner = ownerMapService.findById(ID);

        // Then
        assertEquals(ID, owner.getId());
    }

    @Test
    public void deleteById() {
        // Given
        Owner owner = ownerMapService.findById(ID);

        // When
        ownerMapService.delete(owner);

        // Then
        assertEquals(0, ownerMapService.findAll().size());
    }

    @Test
    public void delete() {
        // Given
        Owner owner = ownerMapService.findById(ID);

        // When
        ownerMapService.delete(owner);

        // Then
        assertEquals(0, ownerMapService.findAll().size());
    }

    @Test
    void saveExistingId() {
        // Given
        final Long id = 2L;
        Owner owner = Owner.builder().id(id).build();
        Owner savedOwner;

        // When
        savedOwner = ownerMapService.save(owner);

        // Then
        assertEquals(id, savedOwner.getId());
    }

    @Test
    void saveNoId() {
        // Given
        Owner owner = Owner.builder().build();
        Owner savedOwner;

        // When
        savedOwner = ownerMapService.save(owner);

        // Then
        assertNotNull(savedOwner);
        assertNotNull(savedOwner.getId());
    }

    @Test
    void findByLastName() {
        // Given
        Owner owner;

        // When
        owner = ownerMapService.findByLastName(LAST_NAME);

        //Then
        assertNotNull(owner);
        assertEquals(ID, owner.getId());
        assertEquals(LAST_NAME, owner.getLastName());
    }

    @Test
    void findByLastNameNotFound() {
        // Given
        Owner owner;

        // When
        owner = ownerMapService.findByLastName("foo");

        //Then
        assertNull(owner);
    }
}