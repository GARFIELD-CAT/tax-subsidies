package ru.utmn.dyagunov.tax_subsidies.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.repository.TaxSubsidyCsvRepository;
import ru.utmn.dyagunov.tax_subsidies.repository.TaxSubsidyJpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaxSubsidiesJpaServiceTest {

    TaxSubsidy e1, e2;
    @Mock
    private TaxSubsidyJpaRepository repository;
    @Mock
    private TaxSubsidyCsvRepository repository2;

    /*@BeforeEach
    void setUp() {
        repository = Mockito.mock(EarthquakeJpaRepository.class);
        service = new EarthquakesJpaService(repository);
    }*/
    @InjectMocks
    private TaxSubsidiesJpaService service;

    @BeforeEach
    void setUp() {
        e1 = new TaxSubsidy();
        e1.setObservationValue(5.0);

        e2 = new TaxSubsidy();
        e2.setObservationValue(7.0);
    }

    @Test
    void getAllTest() {
        when(repository.findAll()).thenReturn(List.of(e1, e2));

        List<TaxSubsidy> result = (List<TaxSubsidy>) service.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        TaxSubsidy e = result.getFirst();
        assertEquals(5.0f, e.getObservationValue());
    }

    @Test
    void deleteTest() {
        when(repository.existsById("1")).thenReturn(true);
        doNothing().when(repository).deleteById("1");

        service.delete("1");

        verify(repository, times(1)).existsById("1");
        verify(repository, times(1)).deleteById("1");
        verify(repository, times(1)).count();
        verify(repository2, times(1)).count();
        verify(repository, never()).findById("1");

        verifyNoMoreInteractions(repository);
    }

    @Test
    void updateNotFoundTest() {
        when(repository.existsById("999")).thenReturn(false);

        var ts = new TaxSubsidy();
        ts.setId("999");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.update(ts));

        assertEquals(404, ex.getStatusCode().value());
        assertEquals("Запись с id=999 не существует", ex.getReason());
        verify(repository, times(1)).existsById("999");
    }
}
