package ru.utmn.dyagunov.tax_subsidies.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.repository.TaxSubsidyCsvRepository;
import ru.utmn.dyagunov.tax_subsidies.repository.TaxSubsidyJpaRepository;
import ru.utmn.dyagunov.tax_subsidies.service.TaxSubsidiesJpaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = TaxSubsidiesController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@Import({TaxSubsidiesJpaService.class, TaxSubsidyCsvRepository.class})
@ActiveProfiles("JpaEngine")
public class TaxSubsidiesControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
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
    void getAllTest() throws Exception {
        TaxSubsidy ts1 = new TaxSubsidy();
        ts1.setId("0001");
        ts1.setReferenceArea("Argentina");
        ts1.setMeasure("Effective average tax rate");
        ts1.setUnitOfMeasure("Percentage of taxable income");
        ts1.setRegime("Regime 1");
        ts1.setTimePeriod(2000);
        ts1.setObservationValue(30.55);
        ts1.setRegimeName("Software Promotional Regime - ARG");
        List<TaxSubsidy> taxSubsidyList = new ArrayList<>();
        taxSubsidyList.add(ts1);

        Sort sort = Sort.by("referenceArea").ascending();
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<TaxSubsidy> pages = new PageImpl<>(taxSubsidyList, pageable, 100);
        given(repository.findAll(pageable)).willReturn(pages);

        mvc.perform(
                        get("/api/tax-subsidies")
                                .contentType(MediaType.APPLICATION_JSON
                                )
                                .param("page", "0")
                                .param("size", "10")
                                .param("sortBy", "referenceArea")
                                .param("sortDir", "asc"))
                .andExpect(status().isOk());
    }

    @Test
    void getOneSuccessTest() throws Exception {
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

    @Test
    void getOneNotFoundErrorTest() throws Exception {
        String id = "2";

        when(repository.findById(id)).thenReturn(Optional.ofNullable(null));

        mvc.perform(
                        get("/api/tax-subsidies/{id}", id)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSuccessTest() throws Exception {
        TaxSubsidy ts1 = new TaxSubsidy();
        ts1.setId("0001");
        ts1.setReferenceArea("Argentina");
        ts1.setMeasure("Effective average tax rate");
        ts1.setUnitOfMeasure("Percentage of taxable income");
        ts1.setRegime("Regime 1");
        ts1.setTimePeriod(2000);
        ts1.setObservationValue(30.55);
        ts1.setRegimeName("Software Promotional Regime - ARG");

        given(repository.existsById("0001")).willReturn(true);
        when(repository.save(ts1)).thenReturn(ts1);

        mvc.perform(
                        put("/api/tax-subsidies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ts1))
                )
                .andExpect(status().isOk());
    }

    @Test
    void updateNotFoundErrorTest() throws Exception {
        TaxSubsidy ts1 = new TaxSubsidy();
        ts1.setId("0001");
        ts1.setReferenceArea("Argentina");
        ts1.setMeasure("Effective average tax rate");
        ts1.setUnitOfMeasure("Percentage of taxable income");
        ts1.setRegime("Regime 1");
        ts1.setTimePeriod(2000);
        ts1.setObservationValue(30.55);
        ts1.setRegimeName("Software Promotional Regime - ARG");

        given(repository.existsById("0001")).willReturn(false);
        when(repository.save(ts1)).thenReturn(ts1);

        mvc.perform(
                        put("/api/tax-subsidies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ts1))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSuccessTest() throws Exception {
        String id = "0001";

        given(repository.existsById("0001")).willReturn(true);

        mvc.perform(
                        delete("/api/tax-subsidies/{id}", id)
                )
                .andExpect(status().isNoContent());

        verify(repository, times(1)).deleteById("0001");
    }

    @Test
    void deleteNotFoundErrorTest() throws Exception {
        String id = "0001";

        given(repository.existsById("0001")).willReturn(false);

        mvc.perform(
                        delete("/api/tax-subsidies/{id}", id)
                )
                .andExpect(status().isNotFound());

        verify(repository, times(0)).deleteById("0001");
    }

    @Test
    void getAverageObservationValueTest() throws Exception {
        double averageValue = 15.5;
        when(repository.getAverageObservationValue()).thenReturn(averageValue);

        mvc.perform(
                        get("/api/tax-subsidies/get-avg-observation-value")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(String.valueOf(averageValue)));
    }

    @Test
    void findByFilterNoParamsReturnsBadRequestTest() throws Exception {
        mvc.perform(
                        get("/api/tax-subsidies/find-by-filter")
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Как минимум 1 параметр должен быть указан."));
    }

    @Test
    void FindByFilterSuccessTest() throws Exception {
        List<TaxSubsidy> taxSubsidyList = new ArrayList<>();
        TaxSubsidy ts1 = new TaxSubsidy();
        ts1.setId("0001");
        ts1.setReferenceArea("Argentina");
        ts1.setMeasure("Effective average tax rate");
        ts1.setUnitOfMeasure("Percentage of taxable income");
        ts1.setRegime("Regime 1");
        ts1.setTimePeriod(2000);
        ts1.setObservationValue(30.55);
        ts1.setRegimeName("Software Promotional Regime - ARG");

        taxSubsidyList.add(ts1);

        when(repository.findByFilter(
                "Argentina",
                "Show history for selection",
                "Percentage of taxable income",
                2000)
        ).thenReturn(taxSubsidyList);

        mvc.perform(
                        get("/api/tax-subsidies/find-by-filter")
                                .param("referenceArea", "Argentina")
                                .param("measure", "Show history for selection")
                                .param("unitOfMeasure", "Percentage of taxable income")
                                .param("timePeriod", "2000")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(taxSubsidyList.getFirst().getId()));
    }
}
