package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.repository.ChambreRepository;
import tn.esprit.tpfoyer.service.ChambreServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


/**
 * Test class for ChambreServiceImpl with Junit and Mockito
 */

@SpringBootTest
@ExtendWith(MockitoExtension.class) // Annotation that replaces the syntax : class extends class
class TestMockChambreServiceImpl {


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

        when(mockChambreRepository.findById(5L)).thenReturn(Optional.empty());

        Chambre expectedChambre = chambreService.retrieveChambre(5L);

        Assertions.assertNull(expectedChambre);

    }

    // Add a chamber:

    // Id/Chamber doesn't exist in the DB, should return the added room/chamber

    @Test
    void testAddChambre_New () {

        when(mockChambreRepository.save(chambre1)).thenReturn(chambre1);

        Chambre addedChambre = chambreService.addChambre(chambre1);

        Assertions.assertEquals(addedChambre, chambre1);
    }

    // Id/Chamber already exists in the DB, should return null/ log message

    @Test
    void testAddChambre_Exists () {


        when(mockChambreRepository.existsById(1L)).thenReturn(false);
        when(mockChambreRepository.save(chambre1)).thenReturn(chambre1);
        Chambre addedChambre = chambreService.addChambre(chambre1);

        Assertions.assertEquals(addedChambre, chambre1);

        when(mockChambreRepository.existsById(1L)).thenReturn(true);
        Chambre duplicateChambre = chambreService.addChambre(chambre1); // this should return null since the same chamber is added twice

        Assertions.assertNull(duplicateChambre);


    }


    // Modify a chamber

    @Test
    void testModifyChambre () {


        Chambre chambreToAdd = chambre2.toBuilder().build(); // create a copy of chamber2


        // Arrange: repo behavior
        when(mockChambreRepository.save(chambre2)).thenReturn(chambre2);
        when(mockChambreRepository.save(chambreToAdd)).thenReturn(chambreToAdd);

        // Add chamber
        Chambre addedChambre = chambreService.addChambre(chambreToAdd);



        // Modify chamber type attribute
        chambre2.setTypeC(TypeChambre.SIMPLE);


        // Act: Modify existing chamber
        Chambre modifiedChambre = chambreService.modifyChambre(chambre2);


        // Assert
        Assertions.assertEquals(modifiedChambre.getTypeC(), chambre2.getTypeC());
        Assertions.assertEquals(addedChambre.getIdChambre(), modifiedChambre.getIdChambre());
        Assertions.assertNotEquals(addedChambre, modifiedChambre);
        Assertions.assertNotEquals(addedChambre.getTypeC(), modifiedChambre.getTypeC());


    }

    // Modify a chamber


    // if the passed id is null, the method should never be called
    @Test
    void testRemoveChambre_NullId() {
        Long chambreId = null;

        // call the method to remove the chambre
        chambreService.removeChambre(chambreId);

        // verify that deleteById was not called
        verify(mockChambreRepository, never()).deleteById(anyLong());
    }

    // if the passed id is not null (could exist or not exist)
    @Test
    void testRemoveChambre_NotNullId () {
        Long chambreId = 1L;

        // call the method
        chambreService.removeChambre(chambreId);

        // verify that deleteById is called once
        verify(mockChambreRepository, times(1)).deleteById(anyLong());
    }







}
