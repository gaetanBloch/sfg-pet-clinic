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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static guru.springframework.sfgpetclinic.TestUtils.*;
import static guru.springframework.sfgpetclinic.controllers.OwnerController.ATTRIBUTE_OWNER;
import static guru.springframework.sfgpetclinic.controllers.OwnerController.URL_OWNERS;
import static guru.springframework.sfgpetclinic.controllers.PetController.VIEW_PETS_CREATE_OR_UPDATE_FORM;
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
    private static final String URL_PETS_NEW = URL_OWNERS + "/" + ID + "/pets/new";
    private static final String URL_PETS_EDIT = URL_OWNERS + "/" + ID + "/pets/" + ID2 + "/edit";
    private static final String URL_REDIRECT_OWNER = "redirect:" + URL_OWNERS + "/" + ID;

    @Mock
    private PetService petService;
    @Mock
    private OwnerService ownerService;
    @Mock
    private PetTypeService petTypeService;
    @InjectMocks
    private PetController petController;
    MockMvc mockMvc;
    Set<PetType> petTypes;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
        petTypes = ImmutableSet.of(
                PetType.builder().id(ID).name("Dog").build(),
                PetType.builder().id(ID2).name("Cat").build()
        );
    }

    private void initMocksReturn() {
        when(ownerService.findById(ID)).thenReturn(OWNER);
        when(petTypeService.findAll()).thenReturn(petTypes);
    }

    private void verifyMocks() {
        verify(ownerService).findById(ID);
        verify(petTypeService).findAll();
    }

    @Test
    void initCreationForm() throws Exception {
        // Given
        initMocksReturn();

        // When
        mockMvc.perform(get(URL_PETS_NEW))

                // Then
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER))
                .andExpect(model().attributeExists("pet"))
                .andExpect(view().name(VIEW_PETS_CREATE_OR_UPDATE_FORM));

        verifyMocks();
    }

    @Test
    void processCreationForm() throws Exception {
        // Given
        initMocksReturn();

        // When
        mockMvc.perform(post(URL_PETS_NEW))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(URL_REDIRECT_OWNER));

        verifyMocks();
        verify(petService).save(any());
    }

    @Test
    void initUpdateForm() throws Exception {
        // Given
        initMocksReturn();
        when(petService.findById(ID2)).thenReturn(Pet.builder().id(ID2).build());

        // When
        mockMvc.perform(get(URL_PETS_EDIT))

                // Then
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER))
                .andExpect(model().attributeExists("pet"))
                .andExpect(view().name(VIEW_PETS_CREATE_OR_UPDATE_FORM));

        verifyMocks();
        verify(petService).findById(ID2);
    }

    @Test
    void processUpdateForm() throws Exception {
        // Given
        initMocksReturn();

        // When
        mockMvc.perform(post(URL_PETS_EDIT))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(URL_REDIRECT_OWNER));

        verifyMocks();
        verify(petService).save(any());
    }
}