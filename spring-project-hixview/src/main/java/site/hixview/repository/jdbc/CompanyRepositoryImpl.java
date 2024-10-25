package site.hixview.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import site.hixview.domain.entity.ListedCountry;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.Scale;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.repository.CompanyRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static site.hixview.domain.vo.Word.*;

@Repository
@Primary
public class CompanyRepositoryImpl implements CompanyRepository {

    @Value("${schema.companies}")
    private String CURRENT_SCHEMA;

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
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA, companyRowMapper());
    }

    @Override
    public Optional<Company> getCompanyByCode(String code) {
        List<Company> oneCompanyOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + "  where code = ?", companyRowMapper(), code);
        return oneCompanyOrNull.isEmpty() ? Optional.empty() : Optional.of(oneCompanyOrNull.getFirst());
    }

    @Override
    public Optional<Company> getCompanyByName(String name) {
        List<Company> oneCompanyOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where name = ?", companyRowMapper(), name);
        return oneCompanyOrNull.isEmpty() ? Optional.empty() : Optional.of(oneCompanyOrNull.getFirst());
    }

    /**
     * INSERT Company
     */
    @Override
    public void saveCompany(Company company) {
        new SimpleJdbcInsert(jdbcTemplate).withTableName(CURRENT_SCHEMA).execute(new MapSqlParameterSource(company.toMap()));
    }

    /**
     * UPDATE Company
     */
    @Override
    public void updateCompany(Company company) {
        jdbcTemplate.update("update " + CURRENT_SCHEMA +
                        " set listedCountry = ?, scale = ?, name = ?, firstCategory = ?, secondCategory = ? where code = ?",
                company.getListedCountry().name(), company.getScale().name(), company.getName(),
                company.getFirstCategory().name(), company.getSecondCategory().name(), company.getCode());
    }

    /**
     * REMOVE Company
     */
    @Override
    public void deleteCompanyByCode(String code) {
        jdbcTemplate.execute("delete from " + CURRENT_SCHEMA + " where code = '" + code + "'");
    }

    /**
     * Other private methods
     */
    private RowMapper<Company> companyRowMapper() {
        return (resultSet, rowNumber) -> Company.builder()
                        .code(resultSet.getString(CODE))
                        .listedCountry(ListedCountry.valueOf(resultSet.getString(LISTED_COUNTRY)))
                        .scale(Scale.valueOf(resultSet.getString(SCALE)))
                        .name(resultSet.getString(NAME))
                        .firstCategory(FirstCategory.valueOf(resultSet.getString(FIRST_CATEGORY)))
                        .secondCategory(SecondCategory.valueOf(resultSet.getString(SECOND_CATEGORY)))
                        .build();
    }
}
