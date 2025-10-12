package ru.utmn.dyagunov.tax_subsidies.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.service.TaxSubsidiesService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


@Repository
public class TaxSubsidyCsvRepository implements CommonRepository<TaxSubsidy> {
    private final HashMap<String, TaxSubsidy> taxSubsidies = new HashMap<>();

    @PostConstruct
    private void readAllLines() {
        try (
                InputStream InputStream = TaxSubsidiesService.class.getResourceAsStream("/tax_rates.csv");
                InputStreamReader streamReader = new InputStreamReader(InputStream);
                BufferedReader reader = new BufferedReader(streamReader);
                CSVReader csvReader = new CSVReaderBuilder(reader)
                        .withSkipLines(1)
                        .build()
        ) {
            List<String[]> lines = csvReader.readAll();

            for (String[] line : lines) {
                TaxSubsidy taxSubsidy = new TaxSubsidy();

                taxSubsidy.setReferenceArea(line[0]);
                taxSubsidy.setMeasure(line[1]);
                taxSubsidy.setUnitOfMeasure(line[2]);
                taxSubsidy.setRegime(line[3]);
                taxSubsidy.setTimePeriod(line[4] != null ? Integer.parseInt(line[4]) : null);
                taxSubsidy.setObservationValue(line[5] != null ? Float.parseFloat(line[5]) : null);
                taxSubsidy.setRegimeName(line[6]);

                taxSubsidies.put(taxSubsidy.getId(), taxSubsidy);
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TaxSubsidy save(TaxSubsidy domain) {
        taxSubsidies.put(domain.getId(), domain);

        return taxSubsidies.get(domain.getId());
    }

    @Override
    public Iterable<TaxSubsidy> save(Collection<TaxSubsidy> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(String id) {
        taxSubsidies.remove(id);
    }

    @Override
    public void delete(TaxSubsidy domain) {
        delete(domain.getId());
    }

    @Override
    public TaxSubsidy findById(String id) {
        return taxSubsidies.get(id);
    }

    @Override
    public Iterable<TaxSubsidy> findAll() {
        return taxSubsidies.values();
    }

    @Override
    public boolean exists(String id) {
        return taxSubsidies.containsKey(id);
    }

    @Override
    public long count() {
        return taxSubsidies.size();
    }
}

