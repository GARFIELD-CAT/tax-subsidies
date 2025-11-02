package ru.utmn.dyagunov.tax_subsidies.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Возвращает все записи", description = "Может работать медленно из-за отсутствия пагинации")
    @GetMapping
    public Iterable<TaxSubsidy> getAll() {
        return taxSubsidiesService.getAll();
    }

    @Operation(summary = "Возвращает одну запись по ее id")
    @GetMapping("/{id}")
    public TaxSubsidy getOne(
            @PathVariable("id") String id
    ) {
        return taxSubsidiesService.getOne(id);
    }

    @Operation(summary = "Создает новую запись")
    @PostMapping
    public ResponseEntity<TaxSubsidy> add(
            @RequestBody TaxSubsidy taxSubsidy
    ) {
        TaxSubsidy entity = taxSubsidiesService.add(taxSubsidy);

        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновляет одну запись по ее id")
    @PutMapping()
    public TaxSubsidy update(
            @RequestBody TaxSubsidy taxSubsidy
    ) {
        return taxSubsidiesService.update(taxSubsidy);
    }

    @Operation(summary = "Удаляет одну запись по ее id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") String id
    ) {
        taxSubsidiesService.delete(id);
    }
}
