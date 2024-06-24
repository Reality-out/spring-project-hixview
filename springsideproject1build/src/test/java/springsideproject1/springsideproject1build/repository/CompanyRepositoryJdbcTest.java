package springsideproject1.springsideproject1build.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Company;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static springsideproject1.springsideproject1build.Utility.createSKHynix;
import static springsideproject1.springsideproject1build.Utility.createSamsungElectronics;

@SpringBootTest
@Transactional
class CompanyRepositoryJdbcTest {

    @Autowired
    CompanyRepository companyRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyRepositoryJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable("testcompanies");
    }

    private void resetTable(String presentTableName) {
        jdbcTemplateTest.execute("DELETE FROM " + presentTableName);
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
        assertThat(companyRepository.findAllCompanies()).usingRecursiveComparison().isEqualTo(List.of(company2, company1));
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
        assertThat(companyRepository.searchCompanyByCode(company1.getCode()).get()).usingRecursiveComparison().isEqualTo(company1);
        assertThat(companyRepository.searchCompanyByCode(company2.getCode()).get()).usingRecursiveComparison().isEqualTo(company2);
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
        assertThat(companyRepository.searchCompanyByName(company1.getName()).get()).usingRecursiveComparison().isEqualTo(company1);
        assertThat(companyRepository.searchCompanyByName(company2.getName()).get()).usingRecursiveComparison().isEqualTo(company2);
    }

    @DisplayName("기업 저장 테스트")
    @Test
    public void save() {
        // given
        Company company = createSamsungElectronics();

        // when
        companyRepository.saveCompany(company);

        // then
        assertThat(companyRepository.searchCompanyByCode(company.getCode()).get())
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
        companyRepository.removeCompanyByCode(company1.getCode());
        companyRepository.removeCompanyByCode(company2.getCode());
        assertThat(companyRepository.findAllCompanies()).isEmpty();
    }
}