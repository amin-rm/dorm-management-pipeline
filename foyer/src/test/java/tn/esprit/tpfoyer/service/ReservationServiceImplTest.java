package tn.esprit.tpfoyer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllReservations() {
        // Given
        List<Reservation> mockReservations = List.of(new Reservation(), new Reservation());
        when(reservationRepository.findAll()).thenReturn(mockReservations);

        // When
        List<Reservation> result = reservationService.retrieveAllReservations();

        // Then
        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveReservation() {
        // Given
        String reservationId = "123";
        Reservation mockReservation = new Reservation();
        mockReservation.setIdReservation(reservationId);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));

        // When
        Reservation result = reservationService.retrieveReservation(reservationId);

        // Then
        assertNotNull(result);
        assertEquals(reservationId, result.getIdReservation());
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testAddReservation() {
        // Given
        Reservation mockReservation = new Reservation();
        when(reservationRepository.save(mockReservation)).thenReturn(mockReservation);

        // When
        Reservation result = reservationService.addReservation(mockReservation);

        // Then
        assertNotNull(result);
        verify(reservationRepository, times(1)).save(mockReservation);
    }

    @Test
    void testModifyReservation() {
        // Given
        Reservation mockReservation = new Reservation();
        mockReservation.setIdReservation("123");
        when(reservationRepository.save(mockReservation)).thenReturn(mockReservation);

        // When
        Reservation result = reservationService.modifyReservation(mockReservation);

        // Then
        assertNotNull(result);
        assertEquals("123", result.getIdReservation());
        verify(reservationRepository, times(1)).save(mockReservation);
    }

    @Test
    void testRemoveReservation() {
        // Given
        String reservationId = "123";

        // When
        reservationService.removeReservation(reservationId);

        // Then
        verify(reservationRepository, times(1)).deleteById(reservationId);
    }

    @Test
    void testTrouverResSelonDateEtStatus() {
        // Given
        Date date = new Date();
        boolean status = true;
        List<Reservation> mockReservations = List.of(new Reservation(), new Reservation());
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(date, status)).thenReturn(mockReservations);

        // When
        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(date, status);

        // Then
        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(date, status);
    }
}
