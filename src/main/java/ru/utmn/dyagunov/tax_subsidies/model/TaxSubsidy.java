package ru.utmn.dyagunov.tax_subsidies.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Entity
@Table(name = "tax_subsidy")
public class TaxSubsidy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    Float observationValue;
    @Column()
    String regimeName;
}
