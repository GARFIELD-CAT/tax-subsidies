package ru.utmn.dyagunov.tax_subsidies.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.repository.CommonRepository;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("CsvEngine")
public class TaxSubsidiesServiceTest {

    @Autowired
    @Qualifier("TaxSubsidiesServiceTest")
    TaxSubsidiesService service;

    @Test
    void getAverageObservationValueImplementTest() {
        Float result = service.getAverageObservationValue();

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
                return null;
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
                return null;
            }

            @Override
            public Iterable<TaxSubsidy> findAll() {
                var e1 = new TaxSubsidy();
                e1.setObservationValue(5.0f);

                var e2 = new TaxSubsidy();
                e2.setObservationValue(7.0f);

                return List.of(e1, e2);
            }

            @Override
            public boolean exists(String id) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }
        }
    }
}
