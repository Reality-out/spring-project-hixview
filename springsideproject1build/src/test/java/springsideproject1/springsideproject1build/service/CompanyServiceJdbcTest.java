package springsideproject1.springsideproject1build.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.repository.CompanyRepository;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CompanyServiceJdbcTest {

    @Autowired
    CompanyService companyService;

    @Autowired
    CompanyRepository companyRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyServiceJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable("testcompanies");
    }

    private void resetTable(String tableName) {
        jdbcTemplateTest.execute("DELETE FROM " + tableName);
    }

    @DisplayName("중복 코드 번호를 사용하는 기업 등록")
    @Test
    public void registerCompanyWithSameCode() {
        // given
        Company company1 = new Company();
        company1.setCode("005930");
        company1.setCountry("South Korea");
        company1.setScale("big");
        company1.setName("삼성전자");
        company1.setCategory1st("electronics");
        company1.setCategory2nd("semiconductor");

        Company company2 = new Company();
        company2.setCode("005930");
        company2.setCountry("South Korea");
        company2.setScale("big");
        company2.setName("삼성전자");
        company2.setCategory1st("electronics");
        company2.setCategory2nd("semiconductor");

        // when
        companyService.joinCompany(company1);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> companyService.joinCompany(company2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 코드 번호입니다.");
    }

    @DisplayName("존재하지 않는 코드 번호를 통한 기업 삭제")
    @Test
    public void removeCompanyByFaultCode() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> companyService.removeCompany("005930"));
        assertThat(e.getMessage()).isEqualTo("해당 코드 번호와 일치하는 기업이 없습니다.");
    }
}