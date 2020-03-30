package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.HashSet;

import static guru.springframework.sfgpetclinic.TestUtils.*;
import static guru.springframework.sfgpetclinic.controllers.ControllerUtils.URL_NEW;
import static guru.springframework.sfgpetclinic.controllers.OwnerController.URL_OWNERS;
import static guru.springframework.sfgpetclinic.controllers.PetController.*;
import static guru.springframework.sfgpetclinic.controllers.VisitController.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Gaetan Bloch
 * Created on 26/03/2020
 */
@ExtendWith(MockitoExtension.class)
class VisitControllerTest {
    private static final String URL_CREATE_UPDATE_VISIT =
            URL_OWNERS + "/" + ID2 + URL_PETS + "/" + ID + URL_VISITS + URL_NEW;

    MockMvc mockMvc;
    @Mock
    private VisitService visitService;
    @Mock
    private PetService petService;
    @Mock
    private OwnerService ownerService;
    @InjectMocks
    private VisitController visitController;

    @BeforeEach
    void setUp() {
        when(petService.findById(ID)).thenReturn(Pet.builder()
                .id(ID)
                .birthDate(LocalDate.of(2018, 11, 13))
                .name("Cutie")
                .visits(new HashSet<>())
                .owner(OWNER2)
                .petType(PetType.builder().name("Dog").build())
                .build()
        );
        mockMvc = MockMvcBuilders.standaloneSetup(visitController).build();
    }

    @Test
    void initNewVisitFormTest() throws Exception {
        // When
        mockMvc.perform(get(URL_CREATE_UPDATE_VISIT))

                // Then
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(ATTRIBUTE_PET))
                .andExpect(model().attributeExists(ATTRIBUTE_VISIT))
                .andExpect(view().name(VIEW_VISIT_CREATE_OR_UPDATE_FORM));

        verify(petService).findById(ID);
    }

    @Test
    void processNewVisitFormTest() throws Exception {
        // Given
        when(ownerService.findById(ID2)).thenReturn(OWNER2);

        // When
        mockMvc.perform(post(URL_CREATE_UPDATE_VISIT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("date", "2018-11-11")
                .param("description", "description"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists(ATTRIBUTE_PET))
                .andExpect(model().attributeExists(ATTRIBUTE_VISIT))
                .andExpect(view().name(URL_REDIRECT_OWNER + ID2));

        verify(petService).findById(ID);
        verify(ownerService).findById(ID2);
        verify(visitService).save(any());
    }

    @Test
    void processNewVisitFormValidationFailedTest() throws Exception {
        // When
        mockMvc.perform(post(URL_CREATE_UPDATE_VISIT).contentType(MediaType.APPLICATION_FORM_URLENCODED))

                // Then
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(ATTRIBUTE_PET))
                .andExpect(model().attributeExists(ATTRIBUTE_VISIT))
                .andExpect(view().name(VIEW_VISIT_CREATE_OR_UPDATE_FORM));

        verifyNoInteractions(visitService);
    }
}