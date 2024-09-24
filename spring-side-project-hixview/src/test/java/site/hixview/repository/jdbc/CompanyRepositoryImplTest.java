package site.hixview.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.repository.CompanyRepository;
import site.hixview.support.util.CompanyArticleTestUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.support.util.CompanyTestUtils.samsungElectronics;
import static site.hixview.support.util.CompanyTestUtils.skHynix;

@OnlyRealRepositoryContext
class CompanyRepositoryImplTest implements CompanyArticleTestUtils {

    @Autowired
    private CompanyRepository companyRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    CompanyRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("기업들 획득")
    @Test
    void getCompaniesTest() {
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
    void getCompanyByCodeTest() {
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
    void getCompanyByNameTest() {
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

    @DisplayName("비어 있는 기업 획득")
    @Test
    void getEmptyCompanyTest() {
        // given & when
        Company company = samsungElectronics;

        // then
        for (Optional<Company> emptyCompany : List.of(
                companyRepository.getCompanyByCode(company.getCode()),
                companyRepository.getCompanyByName(company.getName()))) {
            assertThat(emptyCompany).isEmpty();
        }
    }

    @DisplayName("기업 저장")
    @Test
    void saveCompanyTest() {
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
    void updateCompanyTest() {
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
    void removeCompanyByCodeTest() {
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