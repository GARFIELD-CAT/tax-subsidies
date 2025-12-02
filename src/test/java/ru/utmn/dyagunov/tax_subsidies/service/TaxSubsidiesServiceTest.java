package ru.utmn.dyagunov.tax_subsidies.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.repository.CommonRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("CsvEngine")
public class TaxSubsidiesServiceTest {

    TaxSubsidy e1, e2;

    @Autowired
    @Qualifier("TaxSubsidiesServiceTest")
    TaxSubsidiesService service;

    @BeforeEach
    void setUp() {
        // New record
        e1 = new TaxSubsidy();
        e1.setObservationValue(5.0);
        e1.setId("0001");

        // Exist record
        e2 = new TaxSubsidy();
        e2.setObservationValue(7.0);
        e2.setId("0002");
    }

    @Test
    void addSuccessTest() {
        TaxSubsidy result = service.add(e1);
        assertNotNull(result);

        assertEquals(e1.getId(), result.getId());
    }

    @Test
    void addConflictErrorTest() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.add(e2));

        assertEquals(409, ex.getStatusCode().value());
        assertEquals("Запись с id=0002 создана ранее", ex.getReason());
    }

    @Test
    void getAllSuccessTest() {
        List<TaxSubsidy> result = (List<TaxSubsidy>) service.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        TaxSubsidy e = result.getFirst();
        assertEquals(e2.getId(), e.getId());
    }

    @Test
    void getOneSuccessTest() {
        TaxSubsidy result = service.getOne("0002");

        assertNotNull(result);

        assertEquals(e2.getId(), result.getId());
    }

    @Test
    void getOneNotFoundErrorTest() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.getOne("0001"));

        assertEquals(404, ex.getStatusCode().value());
        assertEquals("Запись с id=0001 не существует", ex.getReason());
    }

    @Test
    void updateSuccessTest() {
        e2.setRegime("Regime 1");
        TaxSubsidy result = service.update(e2);

        assertNotNull(result);

        assertEquals(e2.getId(), result.getId());
        assertEquals(e2.getRegime(), result.getRegime());
    }

    @Test
    void updateNotFoundErrorTest() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.update(e1));

        assertEquals(404, ex.getStatusCode().value());
        assertEquals("Запись с id=0001 не существует", ex.getReason());
    }

    @Test
    void deleteNotFoundErrorTest() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.delete(e1.getId()));

        assertEquals(404, ex.getStatusCode().value());
        assertEquals("Запись с id=0001 не существует", ex.getReason());
    }

    @Test
    void getAverageObservationValueImplementTest() {
        Double result = service.getAverageObservationValue();

        assertNotNull(result);
        assertEquals(6.0, result, 0.00000001);
    }

    @TestConfiguration
    static class TaxSubsidiesServiceImplTestContextConfiguration {
        @Bean
        @Qualifier("TaxSubsidiesServiceTest")
        public TaxSubsidiesService getTaxSubsidiesService() {
            return new TaxSubsidiesService(null, null) {
                public void init(CommonRepository<TaxSubsidy> repository2) {
                    repository = new MockRepository();
                }
            };
        }

        static class MockRepository implements CommonRepository<TaxSubsidy> {

            @Override
            public TaxSubsidy save(TaxSubsidy domain) {
                var e1 = new TaxSubsidy();
                e1.setObservationValue(5.0);
                e1.setId("0001");

                return e1;
            }

            @Override
            public Iterable<TaxSubsidy> save(Collection<TaxSubsidy> domains) {
                return null;
            }

            @Override
            public void delete(String id) {
            }

            @Override
            public void delete(TaxSubsidy domain) {
            }

            @Override
            public TaxSubsidy findById(String id) {
                if (Objects.equals(id, "0002")) {
                    var e2 = new TaxSubsidy();
                    e2.setObservationValue(7.0);
                    e2.setId("0002");

                    return e2;

                }

                return null;
            }

            @Override
            public Iterable<TaxSubsidy> findAll() {
                var e2 = new TaxSubsidy();
                e2.setObservationValue(7.0);
                e2.setId("0002");

                var e3 = new TaxSubsidy();
                e3.setObservationValue(5.0);
                e3.setId("0003");

                return List.of(e2, e3);
            }

            @Override
            public Page<TaxSubsidy> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public boolean exists(String id) {
                return Objects.equals(id, "0002");
            }

            @Override
            public long count() {
                return 2;
            }

            @Override
            public Iterable<TaxSubsidy> findByFilter(String referenceArea, String measure, String unitOfMeasure, Integer timePeriod) {
                return null;
            }
        }
    }
}
