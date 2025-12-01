package ru.utmn.dyagunov.tax_subsidies.controller;


import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.repository.TaxSubsidyCsvRepository;
import ru.utmn.dyagunov.tax_subsidies.repository.TaxSubsidyJpaRepository;
import ru.utmn.dyagunov.tax_subsidies.service.TaxSubsidiesJpaService;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = TaxSubsidiesController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@Import({TaxSubsidiesJpaService.class, TaxSubsidyCsvRepository.class})
@ActiveProfiles("JpaEngine")
public class TaxSubsidiesControllerTest {

    @MockitoBean
    TaxSubsidyJpaRepository repository;
    @MockitoBean
    TaxSubsidyCsvRepository repository2;
    @Autowired
    private MockMvc mvc;

    @Test
    void addTest() throws Exception {
        mvc.perform(
                        post("/api/tax-subsidies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "id": "0001",
                                          "referenceArea": "Argentina",
                                          "measure": "Effective average tax rate",
                                          "unitOfMeasure": "Percentage of taxable income",
                                          "regime": "Regime 1",
                                          "timePeriod": 2000,
                                          "observationValue": 30.55,
                                          "regimeName": "Software Promotional Regime - ARG"
                                        }
                                        """
                                ))
                .andExpect(status().isCreated());
    }

    @Test
    void getOneTest() throws Exception {
        TaxSubsidy ts1 = new TaxSubsidy();
        ts1.setId("0001");
        ts1.setReferenceArea("Argentina");
        ts1.setMeasure("Effective average tax rate");
        ts1.setUnitOfMeasure("Percentage of taxable income");
        ts1.setRegime("Regime 1");
        ts1.setTimePeriod(2000);
        ts1.setObservationValue(30.55);
        ts1.setRegimeName("Software Promotional Regime - ARG");
        given(repository.findById("0001")).willReturn(Optional.of(ts1));

        mvc.perform(
                        get("/api/tax-subsidies/0001")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(ts1.getId())))
                .andExpect(jsonPath("$.referenceArea", Is.is(ts1.getReferenceArea())))
                .andExpect(jsonPath("$.measure", Is.is(ts1.getMeasure())))
                .andExpect(jsonPath("$.unitOfMeasure", Is.is(ts1.getUnitOfMeasure())))
                .andExpect(jsonPath("$.regime", Is.is(ts1.getRegime())))
                .andExpect(jsonPath("$.timePeriod", Is.is(ts1.getTimePeriod())))
                .andExpect(jsonPath("$.regimeName", Is.is(ts1.getRegimeName())))
                .andExpect(jsonPath("$.observationValue", Is.is(ts1.getObservationValue())));

    }
}
