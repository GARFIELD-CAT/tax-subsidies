package ru.utmn.dyagunov.tax_subsidies.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import ru.utmn.dyagunov.tax_subsidies.model.TaxSubsidy;

import java.sql.ResultSet;
import java.util.*;

import static ru.utmn.dyagunov.tax_subsidies.utils.StringUtil.convertToSnakeCase;


@Repository()
@Profile("JdbcEngine")
@Primary
public class TaxSubsidyJdbcRepository implements CommonRepository<TaxSubsidy> {
    private static final String SQL_INSERT = """
            insert into tax_subsidy
                (id, reference_area, measure, unit_of_measure, regime, time_period, observation_value, regime_name)
            values
                (:id, :referenceArea, :measure, :unitOfMeasure, :regime, :timePeriod, :observationValue, :regimeName)
            """;

    private static final String SQL_UPDATE = """
            update tax_subsidy
                set reference_area = :referenceArea,
                measure = :measure,
                unit_of_measure = :unitOfMeasure,
                regime = :regime,
                time_period = :timePeriod,
                observation_value = :observationValue,
                regime_name = :regimeName
            where id = :id
            """;

    private static final String SQL_DELETE = "delete from tax_subsidy where id = :id";

    private static final String SQL_EXIST = "select count(*) > 0 from tax_subsidy where id = :id";

    private static final String SQL_FIND_ALL = """
            select
                id,
                reference_area,
                measure,
                unit_of_measure,
                regime,
                time_period,
                observation_value,
                regime_name
            from tax_subsidy
            """;

    private static final String SQL_FIND_BY_ID = SQL_FIND_ALL + " where id = :id";

    private static final String SQL_COUNT = "select count(*) from tax_subsidy";

    private final NamedParameterJdbcTemplate template;
    private final RowMapper<TaxSubsidy> TaxSubsidyRowMapper = (ResultSet rs, int rowNum) -> {
        TaxSubsidy taxSubsidy = new TaxSubsidy();
        taxSubsidy.setId(rs.getString("id"));
        taxSubsidy.setReferenceArea(rs.getString("reference_area"));
        taxSubsidy.setMeasure(rs.getString("measure"));
        taxSubsidy.setUnitOfMeasure(rs.getString("unit_of_measure"));
        taxSubsidy.setRegime(rs.getString("regime"));
        taxSubsidy.setTimePeriod(rs.getInt("time_period"));
        taxSubsidy.setObservationValue(rs.getDouble("observation_value"));
        taxSubsidy.setRegimeName(rs.getString("regime_name"));

        return taxSubsidy;
    };


    public TaxSubsidyJdbcRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public TaxSubsidy save(TaxSubsidy domain) {
        if (exists(domain.getId())) {
            return insertOrUpdate(SQL_UPDATE, domain);
        }

        return insertOrUpdate(SQL_INSERT, domain);
    }

    private TaxSubsidy insertOrUpdate(final String sql, TaxSubsidy domain) {
        HashMap<String, Object> namedParameter = new HashMap<>();
        namedParameter.put("id", domain.getId());
        namedParameter.put("referenceArea", domain.getReferenceArea());
        namedParameter.put("measure", domain.getMeasure());
        namedParameter.put("unitOfMeasure", domain.getUnitOfMeasure());
        namedParameter.put("regime", domain.getRegime());
        namedParameter.put("timePeriod", domain.getTimePeriod());
        namedParameter.put("observationValue", domain.getObservationValue());
        namedParameter.put("regimeName", domain.getRegimeName());

        template.update(sql, namedParameter);

        return findById(domain.getId());
    }

    @Override
    public Iterable<TaxSubsidy> save(Collection<TaxSubsidy> domains) {
        // ToDo: партиционировать domains по 100-1_000
        template.batchUpdate(SQL_INSERT, SqlParameterSourceUtils.createBatch(domains));

        return findAll();
    }

    @Override
    public void delete(String id) {
        Map<String, String> namedParameter = Collections.singletonMap("id", id);

        template.update(SQL_DELETE, namedParameter);
    }

    @Override
    public void delete(TaxSubsidy domain) {
        delete(domain.getId());
    }

    @Override
    public TaxSubsidy findById(String id) {
        Map<String, String> namedParameter = Collections.singletonMap("id", id);

        return template.queryForObject(SQL_FIND_BY_ID, namedParameter, TaxSubsidyRowMapper);
    }

    @Override
    public Iterable<TaxSubsidy> findAll() {
        return template.query(SQL_FIND_ALL, TaxSubsidyRowMapper);
    }

    @Override
    public Page<TaxSubsidy> findAll(Pageable pageable) {
        String sql = "SELECT * FROM tax_subsidy" + createOrderByClause(pageable) + " LIMIT :limit OFFSET :offset";
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("limit", pageable.getPageSize());
        namedParameters.put("offset", pageable.getOffset());

        List<TaxSubsidy> taxSubsidies = template.query(sql, namedParameters, TaxSubsidyRowMapper);

        long total = this.count();

        return new PageImpl<>(taxSubsidies, pageable, total);
    }

    private String createOrderByClause(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return "";
        }

        StringBuilder orderBy = new StringBuilder(" ORDER BY ");
        pageable.getSort().forEach(order -> {
            var snakeCaseOrder = convertToSnakeCase(order.getProperty());
            orderBy.append(snakeCaseOrder).append(" ").append(order.isAscending() ? "ASC" : "DESC");
        });

        return orderBy.toString();
    }

    @Override
    public boolean exists(String id) {
        Map<String, String> namedParameter = Collections.singletonMap("id", id);

        return template.queryForObject(SQL_EXIST, namedParameter, Boolean.class);
    }

    @Override
    public long count() {
        return template.queryForObject(SQL_COUNT, Collections.emptyMap(), Long.class);
    }
}
