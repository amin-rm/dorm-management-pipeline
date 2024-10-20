package tn.esprit.tpfoyer;

import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.service.FoyerServiceImpl;

import java.util.List;

import static org.mockito.Mockito.when;

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


}
