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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        owners = ImmutableSet.of(
                Owner.builder().id(1L).build(),
                Owner.builder().id(2L).build()
        );
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    private void assertListOwners(String url) throws Exception {
        // When
        mockMvc.perform(get(url))

                //Then
                .andExpect(status().isOk())
                .andExpect(view().name("owners/index"))
                .andExpect(model().attribute("owners", hasSize(2)));
    }

    @Test
    void listOwnersTest() throws Exception {
        // Given
        when(ownerService.findAll()).thenReturn(owners);

        // When Then
        assertListOwners("/owners");
    }

    @Test
    void listOwnersByIndexTest() throws Exception {
        // Given
        when(ownerService.findAll()).thenReturn(owners);

        // When Then
        assertListOwners("/owners/index");
    }

    @Test
    void findOwnersTest() throws Exception {

        // When
        mockMvc.perform(get("/owners/find"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("notimplemented"));

        verifyNoInteractions(ownerService);
    }
}