package springsideproject1.springsideproject1build.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import springsideproject1.springsideproject1build.domain.entity.company.*;
import springsideproject1.springsideproject1build.domain.repository.CompanyRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.CODE;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

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
        return jdbcTemplate.query("select * from " + COMPANY_TABLE, companyRowMapper());
    }

    @Override
    public Optional<Company> getCompanyByCode(String code) {
        List<Company> oneCompanyOrNull = jdbcTemplate.query(
                "select * from " + COMPANY_TABLE + "  where code = ?", companyRowMapper(), code);
        return oneCompanyOrNull.isEmpty() ? Optional.empty() : Optional.of(oneCompanyOrNull.getFirst());
    }

    @Override
    public Optional<Company> getCompanyByName(String name) {
        List<Company> oneCompanyOrNull = jdbcTemplate.query(
                "select * from " + COMPANY_TABLE + " where name = ?", companyRowMapper(), name);
        return oneCompanyOrNull.isEmpty() ? Optional.empty() : Optional.of(oneCompanyOrNull.getFirst());
    }

    /**
     * INSERT Company
     */
    @Override
    public void saveCompany(Company company) {
        new SimpleJdbcInsert(jdbcTemplate).withTableName(COMPANY_TABLE).execute(new MapSqlParameterSource(company.toMap()));
    }

    /**
     * UPDATE Company
     */
    @Override
    public void updateCompany(Company company) {
        jdbcTemplate.update("update " + COMPANY_TABLE +
                        " set country = ?, scale = ?, name = ?, category1st = ?, category2nd = ? where code = ?",
                company.getCountry().name(), company.getScale().name(), company.getName(),
                company.getCategory1st().name(), company.getCategory2nd().name(), company.getCode());
    }

    /**
     * REMOVE Company
     */
    @Override
    public void deleteCompanyByCode(String code) {
        jdbcTemplate.execute("delete from " + COMPANY_TABLE + " where code = '" + code + "'");
    }

    /**
     * Other private methods
     */
    private RowMapper<Company> companyRowMapper() {
        return (resultSet, rowNumber) -> Company.builder()
                        .code(resultSet.getString(CODE))
                        .country(Country.valueOf(resultSet.getString("country")))
                        .scale(Scale.valueOf(resultSet.getString("scale")))
                        .name(resultSet.getString(NAME))
                        .category1st(FirstCategory.valueOf(resultSet.getString("category1st")))
                        .category2nd(SecondCategory.valueOf(resultSet.getString("category2nd")))
                        .build();
    }
}
