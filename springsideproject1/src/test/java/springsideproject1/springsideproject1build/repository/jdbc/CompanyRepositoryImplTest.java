package springsideproject1.springsideproject1build.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.company.Company;
import springsideproject1.springsideproject1build.domain.repository.CompanyRepository;
import springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_TABLE;
import static springsideproject1.springsideproject1build.util.test.CompanyTestUtils.samsungElectronics;
import static springsideproject1.springsideproject1build.util.test.CompanyTestUtils.skHynix;

@SpringBootTest
@Transactional
class CompanyRepositoryImplTest implements CompanyArticleTestUtils {

    @Autowired
    CompanyRepository companyRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, COMPANY_TABLE);
    }

    @DisplayName("기업들 획득")
    @Test
    public void getCompaniesTest() {
        // given
        Company company1 = samsungElectronics;
        Company company2 = skHynix;

        // when
        companyRepository.saveCompany(company1);
        companyRepository.saveCompany(company2);

        // then
        assertThat(companyRepository.getCompanies()).usingRecursiveComparison().isEqualTo(List.of(company2, company1));
    }

    @DisplayName("기업 코드로 획득")
    @Test
    public void getCompanyByCodeTest() {
        // given
        Company company1 = samsungElectronics;
        Company company2 = skHynix;

        // when
        companyRepository.saveCompany(company1);
        companyRepository.saveCompany(company2);

        // then
        assertThat(companyRepository.getCompanyByCode(company1.getCode()).orElseThrow()).usingRecursiveComparison().isEqualTo(company1);
        assertThat(companyRepository.getCompanyByCode(company2.getCode()).orElseThrow()).usingRecursiveComparison().isEqualTo(company2);
    }

    @DisplayName("기업 이름으로 획득")
    @Test
    public void getCompanyByNameTest() {
        // given
        Company company1 = samsungElectronics;
        Company company2 = skHynix;

        // when
        companyRepository.saveCompany(company1);
        companyRepository.saveCompany(company2);

        // then
        assertThat(companyRepository.getCompanyByName(company1.getName()).orElseThrow()).usingRecursiveComparison().isEqualTo(company1);
        assertThat(companyRepository.getCompanyByName(company2.getName()).orElseThrow()).usingRecursiveComparison().isEqualTo(company2);
    }

    @DisplayName("기업 저장")
    @Test
    public void saveCompanyTest() {
        // given
        Company company = samsungElectronics;

        // when
        companyRepository.saveCompany(company);

        // then
        assertThat(companyRepository.getCompanyByCode(company.getCode()).orElseThrow())
                .usingRecursiveComparison().isEqualTo(company);
    }

    @DisplayName("기업 갱신")
    @Test
    public void updateCompanyTest() {
        // given
        Company updateCompany = samsungElectronics;
        String commonCode = updateCompany.getCode();
        Company initialCompany = Company.builder().company(skHynix).code(commonCode).build();
        companyRepository.saveCompany(initialCompany);

        // when
        companyRepository.updateCompany(updateCompany);

        // then
        assertThat(companyRepository.getCompanyByCode(commonCode).orElseThrow())
                .usingRecursiveComparison().isEqualTo(updateCompany);
    }

    @DisplayName("기업 코드로 제거")
    @Test
    public void removeCompanyByCodeTest() {
        // given
        Company company1 = samsungElectronics;
        Company company2 = skHynix;

        // when
        companyRepository.saveCompany(company1);
        companyRepository.saveCompany(company2);

        // then
        companyRepository.deleteCompanyByCode(company1.getCode());
        companyRepository.deleteCompanyByCode(company2.getCode());
        assertThat(companyRepository.getCompanies()).isEmpty();
    }
}