package springsideproject1.springsideproject1build.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.utility.test.CompanyTest;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CompanyRepositoryJdbcTest implements CompanyTest {

    @Autowired
    CompanyRepository companyRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyRepositoryJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, companyTable);
    }

    @DisplayName("기업 모두 가져오기")
    @Test
    public void findAll() {
        // given
        Company company1 = createSamsungElectronics();
        Company company2 = createSKHynix();

        // when
        companyRepository.saveCompany(company1);
        companyRepository.saveCompany(company2);

        // then
        assertThat(companyRepository.getCompanies()).usingRecursiveComparison().isEqualTo(List.of(company2, company1));
    }

    @DisplayName("기업 코드로 찾기")
    @Test
    public void findByCode() {
        // given
        Company company1 = createSamsungElectronics();
        Company company2 = createSKHynix();

        // when
        companyRepository.saveCompany(company1);
        companyRepository.saveCompany(company2);

        // then
        assertThat(companyRepository.getCompanyByCode(company1.getCode()).get()).usingRecursiveComparison().isEqualTo(company1);
        assertThat(companyRepository.getCompanyByCode(company2.getCode()).get()).usingRecursiveComparison().isEqualTo(company2);
    }

    @DisplayName("기업 이름으로 찾기")
    @Test
    public void findByName() {
        // given
        Company company1 = createSamsungElectronics();
        Company company2 = createSKHynix();

        // when
        companyRepository.saveCompany(company1);
        companyRepository.saveCompany(company2);

        // then
        assertThat(companyRepository.getCompanyByName(company1.getName()).get()).usingRecursiveComparison().isEqualTo(company1);
        assertThat(companyRepository.getCompanyByName(company2.getName()).get()).usingRecursiveComparison().isEqualTo(company2);
    }

    @DisplayName("기업 저장")
    @Test
    public void save() {
        // given
        Company company = createSamsungElectronics();

        // when
        companyRepository.saveCompany(company);

        // then
        assertThat(companyRepository.getCompanyByCode(company.getCode()).get())
                .usingRecursiveComparison().isEqualTo(company);
    }

    @DisplayName("기업 코드로 제거하기")
    @Test
    public void removeByCode() {
        // given
        Company company1 = createSamsungElectronics();
        Company company2 = createSKHynix();

        // when
        companyRepository.saveCompany(company1);
        companyRepository.saveCompany(company2);

        // then
        companyRepository.deleteCompanyByCode(company1.getCode());
        companyRepository.deleteCompanyByCode(company2.getCode());
        assertThat(companyRepository.getCompanies()).isEmpty();
    }
}