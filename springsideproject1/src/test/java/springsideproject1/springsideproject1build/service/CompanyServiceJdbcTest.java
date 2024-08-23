package springsideproject1.springsideproject1build.service;

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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static springsideproject1.springsideproject1build.error.constant.EXCEPTION_MESSAGE.ALREADY_EXIST_COMPANY_CODE;
import static springsideproject1.springsideproject1build.error.constant.EXCEPTION_MESSAGE.NO_COMPANY_WITH_THAT_CODE;

@SpringBootTest
@Transactional
class CompanyServiceJdbcTest implements CompanyTestUtility {

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyServiceJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, companyTable);
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

    @DisplayName("기업 존재하지 않는 코드로 제거")
    @Test
    public void removeCompanyByFaultCodeTest() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> companyService.removeCompany("123456"));
        assertThat(e.getMessage()).isEqualTo(NO_COMPANY_WITH_THAT_CODE);
    }
}