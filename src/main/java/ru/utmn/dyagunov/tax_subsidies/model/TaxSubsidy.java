package ru.utmn.dyagunov.tax_subsidies.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class TaxSubsidy {
    String id;
    String referenceArea;
    String measure;
    String unitOfMeasure;
    String regime;
    Integer timePeriod;
    Float observationValue;
    String regimeName;

    public TaxSubsidy() {
        this.id = UUID.randomUUID().toString();
    }
}
