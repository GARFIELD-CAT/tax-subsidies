DROP TABLE IF EXISTS tax_subsidy;
CREATE TABLE tax_subsidy
(
    id varchar(36) not null primary key,
    reference_area varchar(255),
    measure varchar(255),
    unit_of_measure varchar(255),
    regime varchar(255),
    time_period int4,
    observation_value float4,
    regime_name varchar(255)
);