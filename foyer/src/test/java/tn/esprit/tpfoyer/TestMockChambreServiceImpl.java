package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.repository.ChambreRepository;
import tn.esprit.tpfoyer.service.ChambreServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


/**
 * Test class for ChambreServiceImpl with Junit and Mockito
 */

@SpringBootTest
@ExtendWith(MockitoExtension.class) // Annotation that replaces the syntax : class extends class
public class TestMockChambreServiceImpl {


    // Create mock repository
    @Mock
    private ChambreRepository mockChambreRepository;

    // Inject mock repo into service
    @InjectMocks
    private ChambreServiceImpl chambreService;


    // Prepare testing entities
    private final Chambre chambre1 = Chambre.builder().numeroChambre(1)
            .typeC(TypeChambre.SIMPLE)
            .idChambre(1).build();

    private final Chambre chambre2 = Chambre.builder().numeroChambre(2)
            .typeC(TypeChambre.DOUBLE)
            .idChambre(2).build();

    private final List<Chambre> chambres = List.of(chambre1, chambre2);

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(TestMockChambreServiceImpl.class);
    }


    // Retrieve All Chambres tests:


    // if the list of chambers is not empty :
    @Test
    void testretrieveAllChambres_NonEmptyList() {

        when(mockChambreRepository.findAll()).thenReturn(chambres);

        List<Chambre> expectedChambers = chambreService.retrieveAllChambres();

        Assertions.assertEquals(chambres, expectedChambers);
    }

    // if the list of chambers is empty:
    @Test
    void testretrieveAllChambres_EmptyList() {

        when(mockChambreRepository.findAll()).thenReturn(Collections.emptyList());

        List<Chambre> expectedChambers = chambreService.retrieveAllChambres();

        Assertions.assertTrue(expectedChambers.isEmpty());
    }

    // if the list of chambers is null:

    @Test
    void testRetrieveAllChambres_NullList() {

        when(mockChambreRepository.findAll()).thenReturn(null);

        List<Chambre> expectedChambers = chambreService.retrieveAllChambres();

        Assertions.assertNull(expectedChambers);
    }

    // Get Chamber by id tests:

    // if the chamber exists:
    @Test
    void testRetrieveChambre_Exists () {

        when(mockChambreRepository.findById(1L)).thenReturn(Optional.of(chambre1));

        Optional<Chambre> expectedChambre = Optional.of(chambreService.retrieveChambre(1L));

        Assertions.assertTrue(true);
        Assertions.assertEquals(expectedChambre.get(), chambre1);
    }

    // if the chamber doesn't exist:

    @Test
    void testRetrieveChambre_NotExist () {

        when(mockChambreRepository.findById(5L)).thenReturn(null);

        Chambre expectedChambre = chambreService.retrieveChambre(5L);

        Assertions.assertNull(expectedChambre);

    }











}
