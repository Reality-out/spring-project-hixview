package springsideproject1.springsideproject1build.controller.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.repository.CompanyRepository;
import springsideproject1.springsideproject1build.service.CompanyService;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserCompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyService companyService;

    @Autowired
    CompanyRepository companyRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public UserCompanyControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable("testcompanies");
    }

    private void resetTable(String tableName) {
        jdbcTemplateTest.execute("DELETE FROM " + tableName);
    }

    @DisplayName("기업 페이지 접속")
    @Test
    public void accessCompanyPage() throws Exception {
        mockMvc.perform(get("/company"))
                .andExpect(status().isOk());
    }

    @DisplayName("기업 코드로 검색")
    @Test
    public void searchCompanyByCode() throws Exception {
        // given
        Company company = new Company();
        company.setCode("005930");
        company.setCountry("South Korea");
        company.setScale("big");
        company.setName("삼성전자");
        company.setCategory1st("electronics");
        company.setCategory2nd("semiconductor");

        // when
        companyService.joinCompany(company);

        // then
        mockMvc.perform(get("/company")
                        .param("nameOrCode", "005930"))
                .andExpect(status().isOk());
    }

    @DisplayName("기업 이름으로 검색")
    @Test
    public void searchCompanyByName() throws Exception {
        // given
        Company company = new Company();
        company.setCode("005930");
        company.setCountry("South Korea");
        company.setScale("big");
        company.setName("삼성전자");
        company.setCategory1st("electronics");
        company.setCategory2nd("semiconductor");

        // when
        companyService.joinCompany(company);

        // then
        mockMvc.perform(get("/company")
                        .param("nameOrCode", "삼성전자"))
                .andExpect(status().isOk());
    }
}