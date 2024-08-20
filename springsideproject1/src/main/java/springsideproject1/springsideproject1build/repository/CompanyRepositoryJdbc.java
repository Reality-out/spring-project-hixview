package springsideproject1.springsideproject1build.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.company.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.utility.WordUtils.CODE;
import static springsideproject1.springsideproject1build.utility.WordUtils.NAME;
import static springsideproject1.springsideproject1build.utility.test.CompanyTestUtility.companyTable;

@Repository
public class CompanyRepositoryJdbc implements CompanyRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyRepositoryJdbc(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * SELECT Company
     */
    @Override
    public List<Company> getCompanies() {
        return jdbcTemplate.query("select * from " + companyTable, companyRowMapper());
    }

    @Override
    public Optional<Company> getCompanyByCode(String code) {
        List<Company> oneCompanyOrNull = jdbcTemplate.query(
                "select * from " + companyTable + "  where code = ?", companyRowMapper(), code);
        return oneCompanyOrNull.isEmpty() ? Optional.empty() : Optional.of(oneCompanyOrNull.getFirst());
    }

    @Override
    public Optional<Company> getCompanyByName(String name) {
        List<Company> oneCompanyOrNull = jdbcTemplate.query(
                "select * from " + companyTable + " where name = ?", companyRowMapper(), name);
        return oneCompanyOrNull.isEmpty() ? Optional.empty() : Optional.of(oneCompanyOrNull.getFirst());
    }

    /**
     * INSERT Company
     */
    @Override
    @Transactional
    public void saveCompany(Company company) {
        new SimpleJdbcInsert(jdbcTemplate).withTableName(companyTable).execute(new MapSqlParameterSource(company.toMap()));
    }

    /**
     * UPDATE Company
     */
    @Override
    public void updateCompany(Company company) {
        jdbcTemplate.update("update " + companyTable +
                        " set country = ?, scale = ?, name = ?, category1st = ?, category2nd = ? where code = ?",
                company.getCountry().name(), company.getScale().name(), company.getName(),
                company.getCategory1st().name(), company.getCategory2nd().name(), company.getCode());
    }

    /**
     * REMOVE Company
     */
    @Override
    @Transactional
    public void deleteCompanyByCode(String code) {
        jdbcTemplate.execute("delete from " + companyTable + " where code = '" + code + "'");
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
