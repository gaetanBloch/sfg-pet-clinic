package guru.springframework.sfgpetclinic.services.springdatajpa;

import com.google.common.collect.ImmutableSet;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static guru.springframework.sfgpetclinic.services.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Gaetan Bloch
 * Created on 20/03/2020
 */
@ExtendWith(MockitoExtension.class)
class OwnerSDJpaServiceTest {
    @Mock
    private OwnerRepository ownerRepository;
    @InjectMocks
    private OwnerSDJpaService ownerService;

    @Test
    void findByIdTest() {
        // Given
        when(ownerRepository.findById(ID)).thenReturn(Optional.of(OWNER));
        Owner owner;

        // When
        owner = ownerService.findById(ID);

        //Then
        assertNotNull(owner);
        assertEquals(ID, owner.getId());
        verify(ownerRepository, times(1)).findById(ID);
    }

    @Test
    void findByIdNotFoundTest() {
        // Given
        when(ownerRepository.findById(ID)).thenReturn(Optional.empty());
        Owner owner;

        // When
        owner = ownerService.findById(ID);

        //Then
        assertNull(owner);
    }

    @Test
    void findAllTest() {
        // Given
        when(ownerRepository.findAll()).thenReturn(ImmutableSet.of(Owner.builder().build(), OWNER));
        Set<Owner> owners;

        // When
        owners = ownerService.findAll();

        //Then
        assertNotNull(owners);
        assertEquals(2, owners.size());
        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    void saveTest() {
        // Given
        when(ownerRepository.save(OWNER)).thenReturn(OWNER);
        Owner owner;

        // When
        owner = ownerService.save(OWNER);

        //Then
        assertNotNull(owner);
        assertEquals(ID, owner.getId());
        verify(ownerRepository, times(1)).save(OWNER);
    }

    @Test
    void deleteByIdTest() {
        // Given

        // When
        ownerService.deleteById(ID);

        //Then
        verify(ownerRepository, times(1)).deleteById(ID);
    }

    @Test
    void deleteTest() {
        // Given

        // When
        ownerService.delete(OWNER);

        //Then
        verify(ownerRepository, times(1)).delete(OWNER);
    }

    @Test
    void findByLastNameTest() {
        // Given
        Owner owner;
        when(ownerRepository.findByLastName(LAST_NAME)).thenReturn(OWNER);

        // When
        owner = ownerService.findByLastName(LAST_NAME);

        //Then
        assertNotNull(owner);
        assertEquals(LAST_NAME, owner.getLastName());
        verify(ownerRepository, times(1)).findByLastName(LAST_NAME);
    }
}