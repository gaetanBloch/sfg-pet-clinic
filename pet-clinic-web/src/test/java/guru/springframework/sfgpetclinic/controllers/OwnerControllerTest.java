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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static guru.springframework.sfgpetclinic.TestUtils.*;
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
        mockMvc.perform(get("/owners/find"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(OwnerController.VIEWS_OWNER_FIND))
                .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(ownerService);
    }

    @Test
    void processFindFormReturnMany() throws Exception {
        // Given
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(owners);

        // When
        mockMvc.perform(get("/owners"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(OwnerController.VIEWS_OWNERS_LIST))
                .andExpect(model().attribute("selections", hasSize(2)));

        verify(ownerService).findAllByLastNameLike(anyString());
    }

    @Test
    void processFindFormReturnOne() throws Exception {
        // Given
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(ImmutableSet.of(OWNER));

        // When
        mockMvc.perform(get("/owners"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/" + ID));

        verify(ownerService).findAllByLastNameLike(anyString());
    }

    @Test
    void processFindFormReturnNone() throws Exception {
        // Given
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(ImmutableSet.of());

        // When
        mockMvc.perform(get("/owners"))

                // Then
                .andExpect(view().name(OwnerController.VIEWS_OWNER_FIND))
                .andExpect(model().attributeExists("owner"));

        verify(ownerService).findAllByLastNameLike(anyString());
    }

    @Test
    void processFindFormEmptyReturnMany() throws Exception {
        // Given
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(owners);

        // When
        mockMvc.perform(get("/owners").param("lastName",""))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(OwnerController.VIEWS_OWNERS_LIST))
                .andExpect(model().attribute("selections", hasSize(2)));

        verify(ownerService).findAllByLastNameLike(anyString());
    }

    @Test
    void displayOwner() throws Exception {
        // Given
        when(ownerService.findById(ID)).thenReturn(OWNER);

        // When
        mockMvc.perform(get("/owners/" + ID))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(OwnerController.VIEWS_OWNER_DETAILS))
                .andExpect(model().attribute("owner", hasProperty("id", is(ID))));

        verify(ownerService).findById(ID);
    }

    @Test
    void initCreationForm() throws Exception {
        // When
        mockMvc.perform(get("/owners/new"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(ownerService);
    }

    @Test
    void processCreationForm() throws Exception {
        // Given
        when(ownerService.save(any())).thenReturn(OWNER);

        // When
        mockMvc.perform(post("/owners/new"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/" + ID))
                .andExpect(model().attributeExists("owner"));

        verify(ownerService).save(any());
    }

    @Test
    void initUpdateOwnerForm() throws Exception {
        // Given
        when(ownerService.findById(ID)).thenReturn(OWNER);

        // When
        mockMvc.perform(get("/owners/" + ID + "/edit"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists("owner"));

        verify(ownerService).findById(ID);
    }

    @Test
    void processUpdateOwnerForm() throws Exception {
        // Given
        when(ownerService.save(any())).thenReturn(OWNER);

        // When
        mockMvc.perform(post("/owners/" + ID + "/edit"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/" + ID))
                .andExpect(model().attributeExists("owner"));

        verify(ownerService).save(any());
    }
}