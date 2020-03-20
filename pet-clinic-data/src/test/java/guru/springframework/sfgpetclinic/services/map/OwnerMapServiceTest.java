package guru.springframework.sfgpetclinic.services.map;


import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OwnerMapServiceTest {
    OwnerMapService ownerMapService;

    @BeforeEach
    void setUp() {
        ownerMapService = new OwnerMapService(new PetTypeMapService(), new PetMapService());
        ownerMapService.save(TestUtils.OWNER);
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
        owner = ownerMapService.findById(TestUtils.ID);

        // Then
        assertEquals(TestUtils.ID, owner.getId());
    }

    @Test
    public void deleteById() {
        // Given
        Owner owner = ownerMapService.findById(TestUtils.ID);

        // When
        ownerMapService.delete(owner);

        // Then
        assertEquals(0, ownerMapService.findAll().size());
    }

    @Test
    public void delete() {
        // Given
        Owner owner = ownerMapService.findById(TestUtils.ID);

        // When
        ownerMapService.delete(owner);

        // Then
        assertEquals(0, ownerMapService.findAll().size());
    }

    @Test
    void saveExistingId() {
        // Given
        Owner savedOwner;

        // When
        savedOwner = ownerMapService.save(TestUtils.OWNER);

        // Then
        assertEquals(TestUtils.ID, savedOwner.getId());
    }

    @Test
    void saveNoId() {
        // Given
        Owner savedOwner;

        // When
        savedOwner = ownerMapService.save(TestUtils.OWNER);

        // Then
        assertNotNull(savedOwner);
        assertNotNull(savedOwner.getId());
    }

    @Test
    void findByLastName() {
        // Given
        Owner owner;

        // When
        owner = ownerMapService.findByLastName(TestUtils.LAST_NAME);

        //Then
        assertNotNull(owner);
        assertEquals(TestUtils.ID, owner.getId());
        assertEquals(TestUtils.LAST_NAME, owner.getLastName());
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