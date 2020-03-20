package guru.springframework.sfgpetclinic.services.springdatajpa;

import com.google.common.collect.ImmutableSet;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.services.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Gaetan Bloch
 * Created on 20/03/2020
 */
@ExtendWith(MockitoExtension.class)
class OwnerSDJpaServiceTest {
    @Mock
    OwnerRepository ownerRepository;
    @InjectMocks
    OwnerSDJpaService ownerService;

    @Test
    void findById() {
        // Given
        when(ownerRepository.findById(TestUtils.ID)).thenReturn(Optional.of(TestUtils.OWNER));
        Owner owner;

        // When
        owner = ownerService.findById(TestUtils.ID);

        //Then
        assertNotNull(owner);
        assertEquals(TestUtils.ID, owner.getId());
        verify(ownerRepository, times(1)).findById(TestUtils.ID);
    }

    @Test
    void findByIdNotFound() {
        // Given
        when(ownerRepository.findById(TestUtils.ID)).thenReturn(Optional.empty());
        Owner owner;

        // When
        owner = ownerService.findById(TestUtils.ID);

        //Then
        assertNull(owner);
    }

    @Test
    void findAll() {
        // Given
        when(ownerRepository.findAll()).thenReturn(ImmutableSet.of(Owner.builder().build(), TestUtils.OWNER));
        Set<Owner> owners;

        // When
        owners = ownerService.findAll();

        //Then
        assertNotNull(owners);
        assertEquals(2, owners.size());
        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    void save() {
        // Given
        when(ownerRepository.save(TestUtils.OWNER)).thenReturn(TestUtils.OWNER);
        Owner owner;

        // When
        owner = ownerService.save(TestUtils.OWNER);

        //Then
        assertNotNull(owner);
        assertEquals(TestUtils.ID, owner.getId());
        verify(ownerRepository, times(1)).save(TestUtils.OWNER);
    }

    @Test
    void deleteById() {
        // Given

        // When
        ownerService.deleteById(TestUtils.ID);

        //Then
        verify(ownerRepository, times(1)).deleteById(TestUtils.ID);
    }

    @Test
    void delete() {
        // Given

        // When
        ownerService.delete(TestUtils.OWNER);

        //Then
        verify(ownerRepository, times(1)).delete(TestUtils.OWNER);
    }

    @Test
    void findByLastName() {
        // Given
        Owner owner;
        when(ownerRepository.findByLastName(TestUtils.LAST_NAME)).thenReturn(TestUtils.OWNER);

        // When
        owner = ownerService.findByLastName(TestUtils.LAST_NAME);

        //Then
        assertNotNull(owner);
        assertEquals(TestUtils.LAST_NAME, owner.getLastName());
        verify(ownerRepository, times(1)).findByLastName(TestUtils.LAST_NAME);
    }
}