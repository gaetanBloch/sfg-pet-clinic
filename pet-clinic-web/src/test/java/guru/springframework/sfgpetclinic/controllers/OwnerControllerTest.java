package guru.springframework.sfgpetclinic.controllers;

import com.google.common.collect.ImmutableSet;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
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
import static guru.springframework.sfgpetclinic.controllers.ControllerUtils.URL_NEW;
import static guru.springframework.sfgpetclinic.controllers.OwnerController.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Gaetan Bloch
 * Created on 20/03/2020
 */
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {
    private static final String URL_OWNERS_FIND = URL_OWNERS + URL_FIND;
    private static final String URL_OWNERS_NEW = URL_OWNERS + URL_NEW;
    private static final String URL_OWNERS_EDIT = URL_OWNERS + "/" + ID + URL_EDIT;
    private static final String URL_REDIRECT_OWNER = "redirect:" + URL_OWNERS + "/" + ID;

    @Mock
    private OwnerService ownerService;
    @InjectMocks
    private OwnerController ownerController;
    private Set<Owner> owners;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        owners = ImmutableSet.of(OWNER, OWNER2);
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @Test
    void findOwnersTest() throws Exception {
        // When
        mockMvc.perform(get(URL_OWNERS_FIND))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_OWNER_FIND_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER));

        verifyNoInteractions(ownerService);
    }

    @Test
    void processFindFormReturnManyTest() throws Exception {
        // Given
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(owners);

        // When
        mockMvc.perform(get(URL_OWNERS))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_OWNERS_LIST))
                .andExpect(model().attribute(ATTRIBUTE_SELECTIONS, hasSize(2)));

        verify(ownerService).findAllByLastNameLike(anyString());
    }

    @Test
    void processFindFormReturnOneTest() throws Exception {
        // Given
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(ImmutableSet.of(OWNER));

        // When
        mockMvc.perform(get(URL_OWNERS))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(URL_REDIRECT_OWNER));

        verify(ownerService).findAllByLastNameLike(anyString());
    }

    @Test
    void processFindFormReturnNoneTest() throws Exception {
        // Given
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(ImmutableSet.of());

        // When
        mockMvc.perform(get(URL_OWNERS))

                // Then
                .andExpect(view().name(VIEW_OWNER_FIND_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER));

        verify(ownerService).findAllByLastNameLike(anyString());
    }

    @Test
    void processFindFormEmptyReturnManyTest() throws Exception {
        // Given
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(owners);

        // When
        mockMvc.perform(get(URL_OWNERS).param("lastName", ""))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_OWNERS_LIST))
                .andExpect(model().attribute(ATTRIBUTE_SELECTIONS, hasSize(2)));

        verify(ownerService).findAllByLastNameLike(anyString());
    }

    @Test
    void displayOwnerTest() throws Exception {
        // Given
        when(ownerService.findById(ID)).thenReturn(OWNER);

        // When
        mockMvc.perform(get(URL_OWNERS + "/" + ID))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_OWNER_DETAILS))
                .andExpect(model().attribute(ATTRIBUTE_OWNER, hasProperty("id", is(ID))));

        verify(ownerService).findById(ID);
    }

    @Test
    void initCreationFormTest() throws Exception {
        // When
        mockMvc.perform(get(URL_OWNERS_NEW))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_OWNER_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER));

        verifyNoInteractions(ownerService);
    }

    @Test
    void processCreationFormTest() throws Exception {
        // Given
        when(ownerService.save(any())).thenReturn(OWNER);

        // When
        mockMvc.perform(post(URL_OWNERS_NEW)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Gaetan")
                .param("lastName", "Bloch")
                .param("address", "123 Paris street")
                .param("city", "Paris")
                .param("telephone", "0123123123")
        )

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(URL_REDIRECT_OWNER))
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER));

        verify(ownerService).save(any());
    }

    @Test
    void processCreationForm() throws Exception {
        // Given
        when(ownerService.save(any())).thenReturn(OWNER);

        // When
        mockMvc.perform(post(URL_OWNERS_NEW)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Gaetan")
                .param("lastName", "Bloch")
                .param("address", "123 Paris street")
                .param("city", "Paris")
                .param("telephone", "0123123123"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(URL_REDIRECT_OWNER))
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER));

        verify(ownerService).save(any());
    }

    @Test
    void processCreationFormValidationFailedTest() throws Exception {
        // When
        mockMvc.perform(post(URL_OWNERS_NEW)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_OWNER_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER));

        verifyNoInteractions(ownerService);
    }

    @Test
    void initUpdateOwnerFormTest() throws Exception {
        // Given
        when(ownerService.findById(ID)).thenReturn(OWNER);

        // When
        mockMvc.perform(get(URL_OWNERS_EDIT))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_OWNER_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER));

        verify(ownerService).findById(ID);
    }

    @Test
    void processUpdateOwnerFormTest() throws Exception {
        // Given
        when(ownerService.save(any())).thenReturn(OWNER);

        // When
        mockMvc.perform(post(URL_OWNERS_EDIT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("address", "123 Paris street")
                .param("city", "Paris")
                .param("telephone", "0123123123"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(URL_REDIRECT_OWNER))
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER));

        verify(ownerService).save(any());
    }

    @Test
    void processUpdateOwnerFormValidationFailedTest() throws Exception {
        // When
        mockMvc.perform(post(URL_OWNERS_EDIT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_OWNER_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_OWNER));

        verifyNoInteractions(ownerService);
    }
}