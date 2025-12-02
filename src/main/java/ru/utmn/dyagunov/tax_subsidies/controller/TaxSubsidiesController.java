package ru.utmn.dyagunov.tax_subsidies.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;
import ru.utmn.dyagunov.tax_subsidies.service.TaxSubsidiesServiceInterface;

import java.util.Objects;


@RestController
@RequestMapping("/api/tax-subsidies")
public class TaxSubsidiesController {
    private final TaxSubsidiesServiceInterface taxSubsidiesService;

    public TaxSubsidiesController(TaxSubsidiesServiceInterface taxSubsidiesService) {
        this.taxSubsidiesService = taxSubsidiesService;
    }

    @Operation(summary = "Возвращает все записи", description = "Есть пагинация и сортировка")
    @GetMapping
    public ResponseEntity<Object> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size,
        @RequestParam(defaultValue = "referenceArea") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir
    ) {
        if (page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Значение page не может быть отрицательным.");
        } else if (size <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Значение size не может быть меньше или равно 0.");
        }

        Sort sort;

        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TaxSubsidy> taxSubsidyPage = taxSubsidiesService.getAll(pageable);

        return ResponseEntity.ok(
                taxSubsidyPage.getContent()
                    .parallelStream()
                    .filter(Objects::nonNull)
                    .toList()
        );
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

    @Operation(summary = "Возвращает среднее значение налоговой субсидии")
    @GetMapping("/get-avg-observation-value")
    public Double getAverageObservationValue() {
        return taxSubsidiesService.getAverageObservationValue();
    }

    @Operation(summary = "Возвращает записи с фильтрацией по переданным полям")
    @GetMapping("/find-by-filter")
    public ResponseEntity<Object> findByFilter(
            @RequestParam(required = false) String referenceArea,
            @RequestParam(required = false) String measure,
            @RequestParam(required = false) String unitOfMeasure,
            @RequestParam(required = false) Integer timePeriod
    ) {
        if (referenceArea == null && measure == null && unitOfMeasure == null && timePeriod == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Как минимум 1 параметр должен быть указан.");
        }

        return ResponseEntity.ok(taxSubsidiesService.findByFilter(referenceArea, measure, unitOfMeasure, timePeriod));
    }
}
