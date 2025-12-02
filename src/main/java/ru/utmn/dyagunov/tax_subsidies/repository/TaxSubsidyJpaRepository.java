package ru.utmn.dyagunov.tax_subsidies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;

import java.util.List;


public interface TaxSubsidyJpaRepository extends JpaRepository<TaxSubsidy, String> {
    @Query("SELECT AVG(ts.observationValue) FROM TaxSubsidy ts")
    Double getAverageObservationValue();

    @Query("SELECT ts FROM TaxSubsidy ts WHERE " +
            "(:referenceArea IS NULL OR ts.referenceArea = :referenceArea) AND " +
            "(:measure IS NULL OR ts.measure = :measure) AND " +
            "(:unitOfMeasure IS NULL OR ts.unitOfMeasure = :unitOfMeasure) AND " +
            "(:timePeriod IS NULL OR ts.timePeriod = :timePeriod)"
    )
    List<TaxSubsidy> findByFilter(
        @Param("referenceArea") String referenceArea,
        @Param("measure") String measure,
        @Param("unitOfMeasure") String unitOfMeasure,
        @Param("timePeriod") Integer timePeriod
    );
}
