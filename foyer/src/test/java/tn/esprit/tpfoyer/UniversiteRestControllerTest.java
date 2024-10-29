package tn.esprit.tpfoyer.UniversityTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.control.UniversiteRestController;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.service.IUniversiteService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UniversiteRestController.class)
public class UniversiteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUniversiteService universiteService;

    @Test
    public void testGetUniversites_WhenEmpty_ShouldReturnMessage() throws Exception {
        when(universiteService.retrieveAllUniversites()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/universite/retrieve-all-universites"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andDo(result -> {
                    if (result.getResponse().getContentAsString().equals("[]")) {
                        System.out.println("No universities available.");
                    }
                });
    }

    @Test
    public void testRetrieveUniversite_NotFound_ShouldReturnMessage() throws Exception {
        Long nonExistentId = 99L;
        when(universiteService.retrieveUniversite(nonExistentId)).thenReturn(null);

        mockMvc.perform(get("/universite/retrieve-universite/" + nonExistentId))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(result -> {
                    if (result.getResponse().getContentAsString().isEmpty()) {
                        System.out.println("University with ID " + nonExistentId + " not found.");
                    }
                });
    }

    @Test
    public void testAddUniversite_NullInput_ShouldReturnMessage() throws Exception {
        when(universiteService.addUniversite(null)).thenReturn(null);

        mockMvc.perform(post("/universite/add-universite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(result -> {
                    if (result.getResponse().getContentAsString().isEmpty()) {
                        System.out.println("Invalid university data provided.");
                    }
                });
    }

    @Test
    public void testRemoveUniversite_NotFound_ShouldReturnMessage() throws Exception {
        Long nonExistentId = 99L;
        when(universiteService.retrieveUniversite(nonExistentId)).thenReturn(null);

        mockMvc.perform(delete("/universite/remove-universite/" + nonExistentId))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println("University with ID " + nonExistentId + " does not exist and cannot be removed.");
                });
    }

    @Test
    public void testModifyUniversite_NullInput_ShouldReturnMessage() throws Exception {
        when(universiteService.modifyUniversite(null)).thenReturn(null);

        mockMvc.perform(put("/universite/modify-universite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(result -> {
                    System.out.println("No university data provided for modification.");
                });
    }
}
