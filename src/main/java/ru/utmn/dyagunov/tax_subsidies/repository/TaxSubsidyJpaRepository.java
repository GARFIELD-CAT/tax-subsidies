package ru.utmn.dyagunov.tax_subsidies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;


public interface TaxSubsidyJpaRepository extends JpaRepository<TaxSubsidy, String> {
    @Query("SELECT AVG(ts.observationValue) FROM TaxSubsidy ts")
    Float getAverageObservationValue();
}
