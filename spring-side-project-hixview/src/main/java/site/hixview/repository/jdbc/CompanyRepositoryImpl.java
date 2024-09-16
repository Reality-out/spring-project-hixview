package site.hixview.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.Scale;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.repository.CompanyRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static site.hixview.domain.vo.name.EntityName.Company.*;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.domain.vo.Word.NAME;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * SELECT Company
     */
    @Override
    public List<Company> getCompanies() {
        return jdbcTemplate.query("select * from " + TEST_COMPANIES_SCHEMA, companyRowMapper());
    }

    @Override
    public Optional<Company> getCompanyByCode(String code) {
        List<Company> oneCompanyOrNull = jdbcTemplate.query(
                "select * from " + TEST_COMPANIES_SCHEMA + "  where code = ?", companyRowMapper(), code);
        return oneCompanyOrNull.isEmpty() ? Optional.empty() : Optional.of(oneCompanyOrNull.getFirst());
    }

    @Override
    public Optional<Company> getCompanyByName(String name) {
        List<Company> oneCompanyOrNull = jdbcTemplate.query(
                "select * from " + TEST_COMPANIES_SCHEMA + " where name = ?", companyRowMapper(), name);
        return oneCompanyOrNull.isEmpty() ? Optional.empty() : Optional.of(oneCompanyOrNull.getFirst());
    }

    /**
     * INSERT Company
     */
    @Override
    public void saveCompany(Company company) {
        new SimpleJdbcInsert(jdbcTemplate).withTableName(TEST_COMPANIES_SCHEMA).execute(new MapSqlParameterSource(company.toMap()));
    }

    /**
     * UPDATE Company
     */
    @Override
    public void updateCompany(Company company) {
        jdbcTemplate.update("update " + TEST_COMPANIES_SCHEMA +
                        " set country = ?, scale = ?, name = ?, firstCategory = ?, secondCategory = ? where code = ?",
                company.getCountry().name(), company.getScale().name(), company.getName(),
                company.getFirstCategory().name(), company.getSecondCategory().name(), company.getCode());
    }

    /**
     * REMOVE Company
     */
    @Override
    public void deleteCompanyByCode(String code) {
        jdbcTemplate.execute("delete from " + TEST_COMPANIES_SCHEMA + " where code = '" + code + "'");
    }

    /**
     * Other private methods
     */
    private RowMapper<Company> companyRowMapper() {
        return (resultSet, rowNumber) -> Company.builder()
                        .code(resultSet.getString(CODE))
                        .country(Country.valueOf(resultSet.getString(COUNTRY)))
                        .scale(Scale.valueOf(resultSet.getString(SCALE)))
                        .name(resultSet.getString(NAME))
                        .firstCategory(FirstCategory.valueOf(resultSet.getString(FIRST_CATEGORY)))
                        .secondCategory(SecondCategory.valueOf(resultSet.getString(SECOND_CATEGORY)))
                        .build();
    }
}
