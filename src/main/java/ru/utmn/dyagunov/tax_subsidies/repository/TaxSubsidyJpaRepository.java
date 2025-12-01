package ru.utmn.dyagunov.tax_subsidies.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;


public interface TaxSubsidyJpaRepository extends CrudRepository<TaxSubsidy, String> {
    @Query("SELECT AVG(ts.observationValue) FROM TaxSubsidy ts")
    Float getAverageObservationValue();
}
