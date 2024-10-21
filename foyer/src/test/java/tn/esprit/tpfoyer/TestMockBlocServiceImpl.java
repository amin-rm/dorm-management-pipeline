package tn.esprit.tpfoyer;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;

import tn.esprit.tpfoyer.service.BlocServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test") // To use the test profile "application-test.properties"
@ExtendWith(MockitoExtension.class)
class TestMockBlocServiceImpl  {

    @Mock
    private BlocRepository mockBlocRepository;

    @InjectMocks
    private BlocServiceImpl BlocService;

    private final Bloc bloc1 = Bloc.builder().idBloc(1)
            .nomBloc("A")
            .capaciteBloc(55).build();

    private final Bloc bloc2 = Bloc.builder().idBloc(2)
            .nomBloc("B")
            .capaciteBloc(70).build()
            ;

    private final Bloc bloc3 = Bloc.builder().idBloc(3)
            .nomBloc("C")
            .capaciteBloc(90).build()
            ;

    private final Bloc bloc4 = Bloc.builder().idBloc(4)
            .nomBloc("D")
            .capaciteBloc(150).build()
            ;

    private  List<Bloc> listBloc = List.of(bloc1,bloc2,bloc3);


    @Test
    void testretrieveAllBloc() {

        when(mockBlocRepository.findAll()).thenReturn(listBloc);
        List<Bloc> expectedBloc = BlocService.retrieveAllBlocs();
        Assertions.assertEquals(listBloc, expectedBloc);
    }

    @Test
    void testretrieveBloc() {

        when(mockBlocRepository.findById(2L)).thenReturn(Optional.of(bloc2));
        Optional<Bloc> expectedBloc = Optional.of(BlocService.retrieveBloc(2L));
        Assertions.assertEquals(expectedBloc.get(),bloc2);
    }


}

