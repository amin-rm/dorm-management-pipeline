package tn.esprit.tpfoyer;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.service.ChambreServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Test class for {@link ChambreServiceImpl} using JUnit and H2 Database.
 * <p>
 * H2 is used as an in-memory database to replace existing database server for testing purposes.
 * </p>
 * <p>
 * Note: This will create a temporary in-memory database and insert chambers from an existing sql file for unit testing.
 * </p>
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:chambres.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS) // this will only execute the script before the test class (otherwise it will repeat the execution on each method :v)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestChambreServiceImpl {


    @Autowired
    private ChambreServiceImpl chambreService;


    @Test
    @Order(1)
    void testFindAll() {
        List<Chambre> chambreList = chambreService.retrieveAllChambres();

        Assertions.assertNotNull(chambreList);
        Assertions.assertFalse(chambreList.isEmpty());
    }

    // Adding an existing chamber (same id)
    @Test
    @Order(2)
    void testAddChamber_AlreadyExists() {

        Chambre chambreToAdd = chambreService.retrieveChambre(2L);

        Assertions.assertNotNull(chambreToAdd);

        Chambre addedChambre = chambreService.addChambre(chambreToAdd);

        // Should be null
        Assertions.assertNull(addedChambre, "Added chambre should be null since it already exists");

    }



    @Test
    @Order(3)
    void testRetrieveChambre() {

        // Prepare IDs
        Long idExists = 1L;
        Long idNotExists = 999L;

        Chambre existsChambre = chambreService.retrieveChambre(idExists);
        Chambre notExistsChambre = chambreService.retrieveChambre(idNotExists);

        Assertions.assertNotNull(existsChambre);
        Assertions.assertNull(notExistsChambre);

    }



    @Test
    @Order(4)
    void testChambreStatistics() {

        Map<String, Integer> chambreStatistics = chambreService.chambreStatistics();

        Assertions.assertNotNull(chambreStatistics);
        Assertions.assertEquals(10, chambreStatistics.get("SIMPLE"));
        Assertions.assertEquals(10, chambreStatistics.get("DOUBLE"));
        Assertions.assertEquals(20, chambreStatistics.get("TOTAL"));
        Assertions.assertEquals(50, chambreStatistics.get("SIMPLE %"));
        Assertions.assertEquals(50, chambreStatistics.get("DOUBLE %"));

    }

    @Test
    @Order(5)
    void testRecupererChambresSelonTyp() {


        // retrieve chambres by type
        List<Chambre> simpleChambres = chambreService.recupererChambresSelonTyp(TypeChambre.SIMPLE);
        List<Chambre> doubleChambres = chambreService.recupererChambresSelonTyp(TypeChambre.DOUBLE);

        // verify the results of the simple type
        Assertions.assertEquals(10, simpleChambres.size(), "this should return 10 chambres of type SIMPLE");
        Assertions.assertTrue(simpleChambres.stream().allMatch(chambre -> chambre.getTypeC() == TypeChambre.SIMPLE),
                "rertirieved chambres should be of type SIMPLE");

        // verify the results of the double type
        Assertions.assertEquals(10, doubleChambres.size(), "Should return 10 chambres of type DOUBLE");
        Assertions.assertTrue(doubleChambres.stream().allMatch(chambre -> chambre.getTypeC() == TypeChambre.DOUBLE),
                "retrieved chambres should be of type DOUBLE");

    }


    // Modify a chamber
    @Test
    @Order(6)
    void testModifyChamber() {
        // Retrieve the chamber to modify
        Chambre chambreToModify = chambreService.retrieveChambre(1L).toBuilder().build();
        TypeChambre currentType = chambreToModify.getTypeC();

        // Determine the new type
        TypeChambre newType = (currentType == TypeChambre.SIMPLE) ? TypeChambre.DOUBLE : TypeChambre.SIMPLE;
        chambreToModify.setTypeC(newType);

        // Modify the chamber in the service
        chambreService.modifyChambre(chambreToModify);

        // Retrieve the modified chamber to assert changes
        Chambre modifiedChambre = chambreService.retrieveChambre(1L);

        // Assertions to verify the changes
        Assertions.assertEquals(modifiedChambre.getTypeC(), newType, "type should be modified to the new type.");
        Assertions.assertNotEquals(modifiedChambre.getTypeC(), currentType, "type should no longer be the original type.");
        Assertions.assertNotEquals(modifiedChambre, chambreToModify, "modified chamber should not reference the original object.");
    }

    @Test
    @Order(7)
    void testRemoveChambre() {

        Chambre chambreToRemove = chambreService.retrieveChambre(3L);
        Assertions.assertNotNull(chambreToRemove);

        chambreService.removeChambre(chambreToRemove.getIdChambre());

        Chambre removedChambre = chambreService.retrieveChambre(3L);

        Assertions.assertNull(removedChambre, "Removed chamber should be null since it no longer exists");

    }



}
