package springsideproject1.springsideproject1build.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Company;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static springsideproject1.springsideproject1build.Utility.companyRowMapper;
import static springsideproject1.springsideproject1build.Utility.memberRowMapper;

@Repository
@Transactional(readOnly = true)
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
    public List<Company> findAllCompanies() {
        return jdbcTemplate.query("select * from testcompanies", companyRowMapper());
    }

    @Override
    public Optional<Company> searchCompanyByCode(String companyCode) {
        List<Company> oneCompanyOrNull = jdbcTemplate.query("select * from testcompanies where code = ?", companyRowMapper(), companyCode);
        return oneCompanyOrNull.isEmpty() ? Optional.empty() : Optional.of(oneCompanyOrNull.getFirst());
    }

    @Override
    public Optional<Company> searchCompanyByName(String companyName) {
        List<Company> oneCompanyOrNull = jdbcTemplate.query("select * from testcompanies where name = ?", companyRowMapper(), companyName);
        return oneCompanyOrNull.isEmpty() ? Optional.empty() : Optional.of(oneCompanyOrNull.getFirst());

    }

    /**
     * INSERT Company
     */
    @Override
    @Transactional
    public void saveCompany(Company company) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("testcompanies");

        Map<String, Object> insertParam = new HashMap<>() {{
            put("code", company.getCode());
            put("country", company.getCountry());
            put("scale", company.getScale());
            put("name", company.getName());
            put("category1st", company.getCategory1st());
            put("category2nd", company.getCategory2nd());
        }};

        jdbcInsert.execute(new MapSqlParameterSource(insertParam));
    }

    /**
     * REMOVE Company
     */
    @Override
    public void removeCompanyByCode(String companyCode) {
        jdbcTemplate.execute("delete from testcompanies where code = '" + companyCode + "'");
    }
}
