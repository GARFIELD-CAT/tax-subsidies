package ru.utmn.dyagunov.tax_subsidies.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


@Service
public class TaxSubsidiesService {
    private final HashMap<String, TaxSubsidy> taxSubsidies = new HashMap<>();

    public String hello() {
        return "Hello, World!";
    }

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

    public Collection<TaxSubsidy> getAll() {
        return taxSubsidies.values();
    }

    public TaxSubsidy getOne(String id){
        if (!taxSubsidies.containsKey(id))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", id)
            );

        return taxSubsidies.get(id);
    }

//    Нужен кеш, чтобы при ретрае случайно не сделать запись с теми же данными
    public TaxSubsidy add(TaxSubsidy taxSubsidy){
        if (taxSubsidies.containsKey(taxSubsidy.getId()))
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, String.format("Запись с id=%s создана ранее", taxSubsidy.getId())
            );

        taxSubsidies.put(taxSubsidy.getId(), taxSubsidy);

        return taxSubsidy;
    }

    public TaxSubsidy update(TaxSubsidy taxSubsidy){
        if (!taxSubsidies.containsKey(taxSubsidy.getId()))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", taxSubsidy.getId())
            );

        taxSubsidies.put(taxSubsidy.getId(), taxSubsidy);

        return taxSubsidy;
    }

    public void delete(String id){
        if (!taxSubsidies.containsKey(id))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Запись с id=%s не существует", id)
            );

        taxSubsidies.remove(id);
    }
}
