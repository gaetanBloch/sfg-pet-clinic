package guru.springframework.sfgpetclinic.controllers;

import com.google.common.collect.ImmutableSet;
import guru.springframework.sfgpetclinic.formatters.PetTypeFormatter;
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
import org.springframework.format.support.DefaultFormattingConversionService;
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
import static org.mockito.Mockito.*;
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
        var conversionService = new DefaultFormattingConversionService();
        conversionService.addFormatterForFieldType(PetType.class, new PetTypeFormatter(petTypeService));
        mockMvc = MockMvcBuilders
                .standaloneSetup(petController)
                .setConversionService(conversionService)
                .build();
        petTypes = ImmutableSet.of(
                PetType.builder().id(ID).name("Dog").build(),
                PetType.builder().id(ID2).name("Cat").build()
        );
        when(ownerService.findById(ID)).thenReturn(OWNER);
        when(petTypeService.findAll()).thenReturn(petTypes);
    }

    private void verifyMocks(int times) {
        verify(ownerService).findById(ID);
        verify(petTypeService, times(times)).findAll();
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

        verifyMocks(1);
    }

    @Test
    void processCreationFormTest() throws Exception {
        // When
        mockMvc.perform(post(URL_OWNERS_PETS_NEW)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Caline")
                .param("birthDate", "2020-01-01")
                .param("petType", "Cat"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(URL_REDIRECT_OWNER_ID))
                .andExpect(model().attributeExists(ATTRIBUTE_PET));

        verifyMocks(2);
        verify(petService).save(any());
    }

    @Test
    void processCreationFormValidationFailedTest() throws Exception {
        // When
        mockMvc.perform(post(URL_OWNERS_PETS_NEW).contentType(MediaType.APPLICATION_FORM_URLENCODED))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_PETS_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_PET));

        verifyNoInteractions(petService);
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

        verifyMocks(1);
        verify(petService).findById(ID2);
    }

    @Test
    void processUpdateFormTest() throws Exception {
        // When
        mockMvc.perform(post(URL_OWNERS_PETS_EDIT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Milou")
                .param("birthDate", "2020-01-01")
                .param("petType", "Dog"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(URL_REDIRECT_OWNER_ID))
                .andExpect(model().attributeExists(ATTRIBUTE_PET));

        verifyMocks(2);
        verify(petService).save(any());
    }

    @Test
    void processUpdateFormValidationFailedTest() throws Exception {
        // When
        mockMvc.perform(post(URL_OWNERS_PETS_EDIT).contentType(MediaType.APPLICATION_FORM_URLENCODED))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_PETS_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_PET));

        verifyNoInteractions(petService);
    }
}