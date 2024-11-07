package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.service.FoyerServiceImpl;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
 class TestMockFoyerServiceImpl {
    @Mock
    private FoyerRepository foyerRepository;
    @InjectMocks
    private FoyerServiceImpl foyerService;
    private final Foyer foyer1 = Foyer.builder()
            .idFoyer(11L)
            .capaciteFoyer(100)
            .nomFoyer("espoir")
            .build();
    private final Foyer foyer2 = Foyer.builder()
            .idFoyer(21L)
            .capaciteFoyer(100)
            .nomFoyer("espoirr")
            .build();
    private final Foyer foyer3 = Foyer.builder()
            .idFoyer(31L)
            .capaciteFoyer(100)
            .nomFoyer("espoirrr")
            .build();
    private List<Foyer> foyerList = List.of(foyer1, foyer2, foyer3);
    @Autowired
    private FoyerServiceImpl foyerServiceImpl;

    @Test
    void testFindAll() {
        when(foyerRepository.findAll()).thenReturn(foyerList);
        List<Foyer> expected = foyerService.retrieveAllFoyers();
        Assertions.assertEquals(expected, foyerList);
    }
    @Test
    void testRetrieveFoyer() {
        Long foyerId = 11L;
        when(foyerRepository.findById(foyerId)).thenReturn(java.util.Optional.of(foyer1));

        Foyer result = foyerService.retrieveFoyer(foyerId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(foyer1, result);

        // Cas où le foyer n'existe pas
        when(foyerRepository.findById(99L)).thenReturn(java.util.Optional.empty());
        Assertions.assertNull(foyerService.retrieveFoyer(99L));
    }
    @Test
    void testAddFoyer() {
        when(foyerRepository.save(foyer1)).thenReturn(foyer1);

        Foyer result = foyerService.addFoyer(foyer1);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(foyer1, result);
    }
    @Test
    void testModifyFoyer() {
        Foyer modifiedFoyer = Foyer.builder()
                .idFoyer(11L)
                .capaciteFoyer(120)  // Capacité modifiée
                .nomFoyer("espoir_modifie")
                .build();

        when(foyerRepository.save(modifiedFoyer)).thenReturn(modifiedFoyer);

        Foyer result = foyerService.modifyFoyer(modifiedFoyer);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(modifiedFoyer, result);
    }
    @Test
    void testRemoveFoyer() {
        Long foyerId = 11L;
        foyerService.removeFoyer(foyerId);
        verify(foyerRepository).deleteById(foyerId);
    }



}
