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
        Company company1 = new Company();
        company1.setCode("005930");
        company1.setCountry("South Korea");
        company1.setScale("big");
        company1.setName("삼성전자");
        company1.setCategory1st("electronics");
        company1.setCategory2nd("semiconductor");

        Company company2 = new Company();
        company2.setCode("000660");
        company2.setCountry("South Korea");
        company2.setScale("big");
        company2.setName("SK하이닉스");
        company2.setCategory1st("electronics");
        company2.setCategory2nd("semiconductor");

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
        Company company1 = new Company();
        company1.setCode("005930");
        company1.setCountry("South Korea");
        company1.setScale("big");
        company1.setName("삼성전자");
        company1.setCategory1st("electronics");
        company1.setCategory2nd("semiconductor");

        Company company2 = new Company();
        company2.setCode("000660");
        company2.setCountry("South Korea");
        company2.setScale("big");
        company2.setName("SK하이닉스");
        company2.setCategory1st("electronics");
        company2.setCategory2nd("semiconductor");

        // when
        companyRepository.saveCompany(company1);
        companyRepository.saveCompany(company2);

        // then
        assertThat(companyRepository.searchCompanyByCode("005930").get()).usingRecursiveComparison().isEqualTo(company1);
        assertThat(companyRepository.searchCompanyByCode("000660").get()).usingRecursiveComparison().isEqualTo(company2);
    }

    @DisplayName("기업 이름으로 찾기")
    @Test
    public void findByName() {
        // given
        Company company1 = new Company();
        company1.setCode("005930");
        company1.setCountry("South Korea");
        company1.setScale("big");
        company1.setName("삼성전자");
        company1.setCategory1st("electronics");
        company1.setCategory2nd("semiconductor");

        Company company2 = new Company();
        company2.setCode("000660");
        company2.setCountry("South Korea");
        company2.setScale("big");
        company2.setName("SK하이닉스");
        company2.setCategory1st("electronics");
        company2.setCategory2nd("semiconductor");

        // when
        companyRepository.saveCompany(company1);
        companyRepository.saveCompany(company2);

        // then
        assertThat(companyRepository.searchCompanyByName("삼성전자").get()).usingRecursiveComparison().isEqualTo(company1);
        assertThat(companyRepository.searchCompanyByName("SK하이닉스").get()).usingRecursiveComparison().isEqualTo(company2);
    }

    @DisplayName("기업 저장 테스트")
    @Test
    public void save() {
        // given
        Company company = new Company();
        company.setCode("005930");
        company.setCountry("South Korea");
        company.setScale("big");
        company.setName("삼성전자");
        company.setCategory1st("electronics");
        company.setCategory2nd("semiconductor");

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
        Company company1 = new Company();
        company1.setCode("005930");
        company1.setCountry("South Korea");
        company1.setScale("big");
        company1.setName("삼성전자");
        company1.setCategory1st("electronics");
        company1.setCategory2nd("semiconductor");

        Company company2 = new Company();
        company2.setCode("000660");
        company2.setCountry("South Korea");
        company2.setScale("big");
        company2.setName("SK하이닉스");
        company2.setCategory1st("electronics");
        company2.setCategory2nd("semiconductor");

        // when
        companyRepository.saveCompany(company1);
        companyRepository.saveCompany(company2);

        // then
        companyRepository.removeCompanyByCode("005930");
        companyRepository.removeCompanyByCode("000660");
        assertThat(companyRepository.findAllCompanies()).isEmpty();
    }
}