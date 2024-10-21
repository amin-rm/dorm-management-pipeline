package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;
import tn.esprit.tpfoyer.service.EtudiantServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Test class for {@link EtudiantServiceImpl} using JUnit and Mockito.
 * <p>
 * Mockito is used to simulate the behavior of the JPA repository methods.
 * </p>
 * <p>
 * Note: This test is a mock test and does not require a database connection.
 * </p>
 */

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository mockEtudiantRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    // Prepare test data
    private final Etudiant etudiant1 = Etudiant.builder().cinEtudiant(12345L).idEtudiant(1L).build();
    private final Etudiant etudiant2 = Etudiant.builder().cinEtudiant(67890L).idEtudiant(2L).build();
    private final List<Etudiant> etudiants = List.of(etudiant1, etudiant2);

    // Test retrieve all Etudiants (non-empty)
    @Test
    void testRetrieveAllEtudiants_NonEmptyList() {
        when(mockEtudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        Assertions.assertEquals(etudiants, result);
    }

    // Test retrieve all Etudiants (empty)
    @Test
    void testRetrieveAllEtudiants_EmptyList() {
        when(mockEtudiantRepository.findAll()).thenReturn(Collections.emptyList());

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        Assertions.assertTrue(result.isEmpty());
    }

    // Test retrieve all Etudiants (null)
    @Test
    void testRetrieveAllEtudiants_NullList() {
        when(mockEtudiantRepository.findAll()).thenReturn(null);

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        Assertions.assertNull(result);
    }

    // Test retrieve Etudiant by id (exists)
    @Test
    void testRetrieveEtudiant_Exists() {
        when(mockEtudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant1));

        Etudiant result = etudiantService.retrieveEtudiant(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(etudiant1, result);
    }

    // Test retrieve Etudiant by id (doesn't exist)
    @Test
    void testRetrieveEtudiant_NotExist() {
        when(mockEtudiantRepository.findById(99L)).thenReturn(Optional.empty());

        Etudiant result = etudiantService.retrieveEtudiant(99L);

        Assertions.assertNull(result);
    }

    // Test add Etudiant (new)
    @Test
    void testAddEtudiant_New() {
        when(mockEtudiantRepository.save(etudiant1)).thenReturn(etudiant1);

        Etudiant result = etudiantService.addEtudiant(etudiant1);

        Assertions.assertEquals(etudiant1, result);
    }

    // Test add Etudiant (duplicate exists)
    @Test
    void testAddEtudiant_Exists() {
        when(mockEtudiantRepository.existsById(1L)).thenReturn(true);

        Etudiant result = etudiantService.addEtudiant(etudiant1);

        Assertions.assertNull(result);
    }

    // Test modify Etudiant
    @Test
    void testModifyEtudiant() {
        Etudiant modifiedEtudiant = etudiant2.toBuilder().cinEtudiant(11111L).build();

        when(mockEtudiantRepository.save(modifiedEtudiant)).thenReturn(modifiedEtudiant);

        Etudiant result = etudiantService.modifyEtudiant(modifiedEtudiant);

        Assertions.assertEquals(modifiedEtudiant.getCinEtudiant(), result.getCinEtudiant());
    }

    // Test remove Etudiant (null id)
    @Test
    void testRemoveEtudiant_NullId() {
        Long etudiantId = null;

        etudiantService.removeEtudiant(etudiantId);

        verify(mockEtudiantRepository, never()).deleteById(anyLong());
    }

    // Test remove Etudiant (valid id)
    @Test
    void testRemoveEtudiant_ValidId() {
        Long etudiantId = 1L;

        etudiantService.removeEtudiant(etudiantId);

        verify(mockEtudiantRepository, times(1)).deleteById(etudiantId);
    }
}
