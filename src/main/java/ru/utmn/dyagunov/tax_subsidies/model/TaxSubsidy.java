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
    String id;
    @Column()
    String referenceArea;
    @Column()
    String measure;
    @Column()
    String unitOfMeasure;
    @Column()
    String regime;
    @Column()
    Integer timePeriod;
    @Column()
    Double observationValue;
    @Column()
    String regimeName;

    public TaxSubsidy() {
        this.id = UUID.randomUUID().toString();
    }

    public int compareByField(String fieldName, TaxSubsidy other) {
        return switch (fieldName) {
            case "referenceArea" -> this.referenceArea.compareTo(other.referenceArea);
            case "measure" -> this.measure.compareTo(other.measure);
            case "unitOfMeasure" -> this.unitOfMeasure.compareTo(other.unitOfMeasure);
            case "regime" -> this.regime.compareTo(other.regime);
            case "timePeriod" -> this.timePeriod.compareTo(other.timePeriod);
            case "observationValue" -> this.observationValue.compareTo(other.observationValue);
            case "regimeName" -> this.regimeName.compareTo(other.regimeName);
            default -> throw new IllegalArgumentException("Unknown field: " + fieldName);
        };
    }
}
