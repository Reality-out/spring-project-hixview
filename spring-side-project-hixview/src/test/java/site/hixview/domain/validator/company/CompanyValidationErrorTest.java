package site.hixview.domain.validator.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.company.CompanyDto;
import site.hixview.domain.service.CompanyService;
import site.hixview.util.test.CompanyTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.name.EntityName.Company.COMPANY;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.domain.vo.Word.ERROR;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_COMPANY_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_COMPANY_URL;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyValidationErrorTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyValidationErrorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("중복 기업 코드를 사용하는 기업 추가")
    @Test
    public void duplicatedCodeCompanyAdd() throws Exception {
        // given
        String commonCode = samsungElectronics.getCode();
        CompanyDto companyDto2 = createSKHynixDto();
        companyDto2.setCode(commonCode);

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto2))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto2);
    }

    @DisplayName("중복 기업명을 사용하는 기업 추가")
    @Test
    public void duplicatedNameCompanyAdd() throws Exception {
        // given
        String commonName = samsungElectronics.getName();
        CompanyDto companyDto2 = createSKHynixDto();
        companyDto2.setName(commonName);

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto2))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto2);
    }

    @DisplayName("존재하지 않는 기업 코드를 사용하는 기업 변경")
    @Test
    public void notExistCodeCompanyModify() throws Exception {
        // given
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setCode(skHynix.getCode());

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(UPDATE_COMPANY_URL + FINISH_URL, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("존재하지 않는 기업명을 사용하는 기업 변경")
    @Test
    public void notExistNameCompanyModify() throws Exception {
        // given
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setName(skHynix.getName());

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(UPDATE_COMPANY_URL + FINISH_URL, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }
}
