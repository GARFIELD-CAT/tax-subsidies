package ru.utmn.dyagunov.tax_subsidies.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("CsvEngine")
public class TaxSubsidiesServiceTest {

    @Autowired
    TaxSubsidiesService service;

    @Test
    void getAverageObservationValueImplementTest() {
        Float result = service.getAverageObservationValue();

        assertEquals(3.702639, result, 0.01);
    }
}
