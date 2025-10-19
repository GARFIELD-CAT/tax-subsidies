package ru.utmn.dyagunov.tax_subsidies.service;

import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;


public interface TaxSubsidiesServiceInterface {

    Iterable<TaxSubsidy> getAll();

    TaxSubsidy getOne(String id);

    TaxSubsidy add(TaxSubsidy taxSubsidy);

    TaxSubsidy update(TaxSubsidy taxSubsidy);

    void delete(String id);
}
