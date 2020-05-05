package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static guru.springframework.sfgpetclinic.services.TestUtils.ID;
import static guru.springframework.sfgpetclinic.services.TestUtils.ID2;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gaetan Bloch
 * Created on 24/03/2020
 */
class PetMapServiceTest {
    private PetMapService petMapService;

    @BeforeEach
    void setUp() {
        petMapService = new PetMapService();
        petMapService.save(Pet.builder().id(ID).build());
    }

    @Test
    void findAllTest() {
        // When
        Set<Pet> petSet = petMapService.findAll();

        // Then
        assertEquals(1, petSet.size());
    }

    @Test
    void findByIdExistingId() {
        // When
        Pet pet = petMapService.findById(ID);

        // Then
        assertEquals(ID, pet.getId());
    }

    @Test
    void findByIdNotExistingId() {
        // When
        Pet pet = petMapService.findById(5L);

        // Then
        assertNull(pet);
    }

    @Test
    void findByIdNullId() {
        // When
        Pet pet = petMapService.findById(null);

        // Then
        assertNull(pet);
    }

    @Test
    void saveExistingId() {
        // Given
        Pet pet2 = Pet.builder().id(ID).build();

        // When
        Pet savedPet = petMapService.save(pet2);

        // Then
        assertEquals(ID, savedPet.getId());
        assertEquals(1, petMapService.findAll().size());
    }

    @Test
    void saveNoId() {
        // When
        Pet savedPet = petMapService.save(Pet.builder().build());

        // Then
        assertNotNull(savedPet);
        assertNotNull(savedPet.getId());
        assertEquals(2, petMapService.findAll().size());
    }

    @Test
    void deletePet() {
        // When
        petMapService.delete(petMapService.findById(ID));

        // Then
        assertEquals(0, petMapService.findAll().size());
    }

    @Test
    void deleteWithWrongId() {
        // Given
        Pet pet = Pet.builder().id(ID2).build();

        // When
        petMapService.delete(pet);

        // Then
        assertEquals(1, petMapService.findAll().size());
    }

    @Test
    void deleteWithNullId() {
        // Given
        Pet pet = Pet.builder().build();

        // When
        petMapService.delete(pet);

        // Then
        assertEquals(1, petMapService.findAll().size());
    }

    @Test
    void deleteNull() {
        // When
        petMapService.delete(null);

        // Then
        assertEquals(1, petMapService.findAll().size());
    }

    @Test
    void deleteByIdCorrectId() {
        // When
        petMapService.deleteById(ID);

        // Then
        assertEquals(0, petMapService.findAll().size());
    }

    @Test
    void deleteByIdWrongId() {
        // When
        petMapService.deleteById(ID2);

        // Then
        assertEquals(1, petMapService.findAll().size());
    }

    @Test
    void deleteByIdNullId() {
        // When
        petMapService.deleteById(null);

        // Then
        assertEquals(1, petMapService.findAll().size());
    }
}