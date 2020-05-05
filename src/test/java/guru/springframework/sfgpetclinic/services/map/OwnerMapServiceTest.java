package guru.springframework.sfgpetclinic.services.map;


import guru.springframework.sfgpetclinic.model.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static guru.springframework.sfgpetclinic.services.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class OwnerMapServiceTest {
    private OwnerMapService ownerMapService;

    @BeforeEach
    void setUp() {
        ownerMapService = new OwnerMapService(new PetTypeMapService(), new PetMapService());
        ownerMapService.save(OWNER);
    }

    @Test
    public void findAllTest() {
        // Given
        Set<Owner> owners;

        // When
        owners = ownerMapService.findAll();

        // Then
        assertEquals(1, owners.size());
    }

    @Test
    public void findByIdTest() {
        // Given
        Owner owner;

        // When
        owner = ownerMapService.findById(ID);

        // Then
        assertEquals(ID, owner.getId());
    }

    @Test
    public void deleteByIdTest() {
        // Given
        Owner owner = ownerMapService.findById(ID);

        // When
        ownerMapService.delete(owner);

        // Then
        assertEquals(0, ownerMapService.findAll().size());
    }

    @Test
    public void deleteTest() {
        // Given
        Owner owner = ownerMapService.findById(ID);

        // When
        ownerMapService.delete(owner);

        // Then
        assertEquals(0, ownerMapService.findAll().size());
    }

    @Test
    void saveExistingIdTest() {
        // Given
        Owner savedOwner;

        // When
        savedOwner = ownerMapService.save(OWNER);

        // Then
        assertEquals(ID, savedOwner.getId());
    }

    @Test
    void saveNoIdTest() {
        // Given
        Owner savedOwner;

        // When
        savedOwner = ownerMapService.save(OWNER);

        // Then
        assertNotNull(savedOwner);
        assertNotNull(savedOwner.getId());
    }

    @Test
    void findByLastNameTest() {
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
    void findByLastNameNotFoundTest() {
        // Given
        Owner owner;

        // When
        owner = ownerMapService.findByLastName("foo");

        //Then
        assertNull(owner);
    }
}