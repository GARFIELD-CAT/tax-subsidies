package ru.utmn.dyagunov.tax_subsidies.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tax_subsidy")
public class TaxSubsidy {
    @Id
    @Column(name = "id")
    String id;
    @Column(name = "reference_area")
    String referenceArea;
    @Column(name = "measure")
    String measure;
    @Column(name = "unit_of_measure")
    String unitOfMeasure;
    @Column(name = "regime")
    String regime;
    @Column(name = "time_period")
    Integer timePeriod;
    @Column(name = "observation_value")
    Float observationValue;
    @Column(name = "regime_name")
    String regimeName;

    public TaxSubsidy() {
        this.id = UUID.randomUUID().toString();
    }
}
