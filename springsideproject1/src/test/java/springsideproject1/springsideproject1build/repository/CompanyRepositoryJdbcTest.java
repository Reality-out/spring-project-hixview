package springsideproject1.springsideproject1build.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.company.Company;
import springsideproject1.springsideproject1build.utility.test.CompanyTestUtility;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CompanyRepositoryJdbcTest implements CompanyTestUtility {

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
        companyRepository.insertCompany(company1);
        companyRepository.insertCompany(company2);

        // then
        assertThat(companyRepository.selectCompanies()).usingRecursiveComparison().isEqualTo(List.of(company2, company1));
    }

    @DisplayName("기업 코드로 찾기")
    @Test
    public void findByCode() {
        // given
        Company company1 = createSamsungElectronics();
        Company company2 = createSKHynix();

        // when
        companyRepository.insertCompany(company1);
        companyRepository.insertCompany(company2);

        // then
        assertThat(companyRepository.selectCompanyByCode(company1.getCode()).get()).usingRecursiveComparison().isEqualTo(company1);
        assertThat(companyRepository.selectCompanyByCode(company2.getCode()).get()).usingRecursiveComparison().isEqualTo(company2);
    }

    @DisplayName("기업 이름으로 찾기")
    @Test
    public void findByName() {
        // given
        Company company1 = createSamsungElectronics();
        Company company2 = createSKHynix();

        // when
        companyRepository.insertCompany(company1);
        companyRepository.insertCompany(company2);

        // then
        assertThat(companyRepository.selectCompanyByName(company1.getName()).get()).usingRecursiveComparison().isEqualTo(company1);
        assertThat(companyRepository.selectCompanyByName(company2.getName()).get()).usingRecursiveComparison().isEqualTo(company2);
    }

    @DisplayName("기업 저장하기")
    @Test
    public void save() {
        // given
        Company company = createSamsungElectronics();

        // when
        companyRepository.insertCompany(company);

        // then
        assertThat(companyRepository.selectCompanyByCode(company.getCode()).get())
                .usingRecursiveComparison().isEqualTo(company);
    }

    @DisplayName("기업 갱신하기")
    @Test
    public void update() {
        // given
        Company updateCompany = createSamsungElectronics();
        String commonCode = updateCompany.getCode();
        Company initialCompany = Company.builder().company(createSKHynix()).code(commonCode).build();
        companyRepository.insertCompany(initialCompany);

        // when
        companyRepository.updateCompany(updateCompany);

        // then
        assertThat(companyRepository.selectCompanyByCode(commonCode).get())
                .usingRecursiveComparison().isEqualTo(updateCompany);
    }

    @DisplayName("기업 코드로 제거하기")
    @Test
    public void removeByCode() {
        // given
        Company company1 = createSamsungElectronics();
        Company company2 = createSKHynix();

        // when
        companyRepository.insertCompany(company1);
        companyRepository.insertCompany(company2);

        // then
        companyRepository.deleteCompanyByCode(company1.getCode());
        companyRepository.deleteCompanyByCode(company2.getCode());
        assertThat(companyRepository.selectCompanies()).isEmpty();
    }
}