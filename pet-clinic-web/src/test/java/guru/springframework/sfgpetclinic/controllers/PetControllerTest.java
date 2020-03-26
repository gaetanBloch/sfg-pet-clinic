package guru.springframework.sfgpetclinic.controllers;

import com.google.common.collect.ImmutableSet;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static guru.springframework.sfgpetclinic.TestUtils.*;
import static guru.springframework.sfgpetclinic.controllers.ControllerUtils.URL_EDIT;
import static guru.springframework.sfgpetclinic.controllers.OwnerController.ATTRIBUTE_OWNER;
import static guru.springframework.sfgpetclinic.controllers.OwnerController.URL_OWNERS;
import static guru.springframework.sfgpetclinic.controllers.PetController.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Gaetan Bloch
 * Created on 25/03/2020
 */
@ExtendWith(MockitoExtension.class)
class PetControllerTest {
    private static final String URL_OWNERS_PETS_NEW = URL_OWNERS + "/" + ID + URL_PETS_NEW;
    private static final String URL_OWNERS_PETS_EDIT = URL_OWNERS + "/" + ID + URL_PETS + "/" + ID2 + URL_EDIT;
    private static final String URL_REDIRECT_OWNER_ID = URL_REDIRECT_OWNER + ID;
    MockMvc mockMvc;
    Set<PetType> petTypes;
    @Mock
    private PetService petService;
    @Mock
    private OwnerService ownerService;
    @Mock
    private PetTypeService petTypeService;
    @InjectMocks
    private PetController petController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
        petTypes = ImmutableSet.of(
                PetType.builder().id(ID).name("Dog").build(),
                PetType.builder().id(ID2).name("Cat").build()
        );
        when(ownerService.findById(ID)).thenReturn(OWNER);
        when(petTypeService.findAll()).thenReturn(petTypes);
    }

    private void verifyMocks() {
        verify(ownerService).findById(ID);
        verify(petTypeService).findAll();
    }

    @Test
    void initCreationFormTest() throws Exception {
        // When
        mockMvc.perform(get(URL_OWNERS_PETS_NEW))

                // Then
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER))
                .andExpect(model().attributeExists(ATTRIBUTE_PET))
                .andExpect(view().name(VIEW_PETS_CREATE_OR_UPDATE_FORM));

        verifyMocks();
    }

    @Test
    void processCreationFormTest() throws Exception {
        // When
        mockMvc.perform(post(URL_OWNERS_PETS_NEW)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "soleil")
                .param("birthDate", "2020-01-01"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(URL_REDIRECT_OWNER_ID));

        verifyMocks();
        verify(petService).save(any());
    }

    @Test
    void initUpdateFormTest() throws Exception {
        // Given
        when(petService.findById(ID2)).thenReturn(Pet.builder().id(ID2).build());

        // When
        mockMvc.perform(get(URL_OWNERS_PETS_EDIT))

                // Then
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER))
                .andExpect(model().attributeExists(ATTRIBUTE_PET))
                .andExpect(view().name(VIEW_PETS_CREATE_OR_UPDATE_FORM));

        verifyMocks();
        verify(petService).findById(ID2);
    }

    @Test
    void processUpdateFormTest() throws Exception {
        // When
        mockMvc.perform(post(URL_OWNERS_PETS_EDIT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "soleil")
                .param("birthDate", "2020-01-01"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(URL_REDIRECT_OWNER_ID));

        verifyMocks();
        verify(petService).save(any());
    }
}