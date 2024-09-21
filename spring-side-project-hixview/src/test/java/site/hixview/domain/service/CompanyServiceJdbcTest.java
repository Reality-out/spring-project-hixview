package site.hixview.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.company.Company;
import site.hixview.util.test.CompanyArticleTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_COMPANY_CODE;
import static site.hixview.domain.vo.ExceptionMessage.NO_COMPANY_WITH_THAT_CODE;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.util.test.CompanyTestUtils.samsungElectronics;
import static site.hixview.util.test.CompanyTestUtils.skHynix;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@Transactional
class CompanyServiceJdbcTest implements CompanyArticleTestUtils {

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyServiceJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("기업 코드와 이름으로 찾기")
    @Test
    public void findCompanyWithCodeAndNameTest() {
        // given
        Company company = samsungElectronics;

        // when
        companyService.registerCompany(company);

        // then
        assertThat(companyService.findCompanyByCodeOrName(company.getCode()))
                .usingRecursiveComparison()
                .isEqualTo(companyService.findCompanyByCodeOrName(company.getName()));
    }

    @DisplayName("기업 등록")
    @Test
    public void registerCompanyTest() {
        // given
        Company company = samsungElectronics;

        // when
        companyService.registerCompany(company);

        // then
        assertThat(companyService.findCompanies().getFirst()).usingRecursiveComparison().isEqualTo(company);
    }

    @DisplayName("기업들 등록")
    @Test
    public void registerCompaniesTest() {
        // given & when
        companyService.registerCompanies(samsungElectronics, skHynix);

        // then
        assertThat(companyService.findCompanies())
                .usingRecursiveComparison().isEqualTo(List.of(skHynix, samsungElectronics));
    }

    @DisplayName("기업 중복 코드로 등록")
    @Test
    public void registerDuplicatedCompanyWithSameCodeTest() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> companyService.registerCompanies(samsungElectronics,
                        Company.builder().company(skHynix).code(samsungElectronics.getCode()).build()));

        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_COMPANY_CODE);
    }

    @DisplayName("기업 존재하지 않는 코드로 수정")
    @Test
    public void correctCompanyByFaultCodeTest() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> companyService.correctCompany(samsungElectronics));
        assertThat(e.getMessage()).isEqualTo(NO_COMPANY_WITH_THAT_CODE);
    }

    @DisplayName("기업 제거")
    @Test
    public void removeCompanyTest() {
        // given
        companyService.registerCompany(samsungElectronics);

        // when
        companyService.removeCompanyByCode(samsungElectronics.getCode());

        // then
        assertThat(companyService.findCompanies()).isEmpty();
    }

    @DisplayName("기업 존재하지 않는 코드로 제거")
    @Test
    public void removeCompanyByFaultCodeTest() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> companyService.removeCompanyByCode(INVALID_VALUE));
        assertThat(e.getMessage()).isEqualTo(NO_COMPANY_WITH_THAT_CODE);
    }
}