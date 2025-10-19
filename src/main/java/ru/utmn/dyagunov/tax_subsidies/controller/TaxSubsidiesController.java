package ru.utmn.dyagunov.tax_subsidies.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.service.TaxSubsidiesServiceInterface;


@RestController
@RequestMapping("/api/tax-subsidies")
public class TaxSubsidiesController {
    private final TaxSubsidiesServiceInterface taxSubsidiesService;

    public TaxSubsidiesController(TaxSubsidiesServiceInterface taxSubsidiesService) {
        this.taxSubsidiesService = taxSubsidiesService;
    }

    @GetMapping
    public Iterable<TaxSubsidy> getAll() {
        return taxSubsidiesService.getAll();
    }

    @GetMapping("/{id}")
    public TaxSubsidy getOne(
            @PathVariable("id") String id
    ) {
        return taxSubsidiesService.getOne(id);
    }

    @PostMapping
    public ResponseEntity<TaxSubsidy> add(
            @RequestBody TaxSubsidy taxSubsidy
    ) {
        TaxSubsidy entity = taxSubsidiesService.add(taxSubsidy);

        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @PutMapping()
    public TaxSubsidy update(
            @RequestBody TaxSubsidy taxSubsidy
    ) {
        return taxSubsidiesService.update(taxSubsidy);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") String id
    ) {
        taxSubsidiesService.delete(id);
    }
}
