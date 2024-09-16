package springsideproject1.springsideproject1build.web.controller.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.Country;
import springsideproject1.springsideproject1build.domain.entity.Scale;
import springsideproject1.springsideproject1build.domain.entity.company.Company;
import springsideproject1.springsideproject1build.domain.entity.company.CompanyDto;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.util.test.CompanyTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.domain.vo.EntityName.Company.COMPANY;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.FINISH_URL;
import static springsideproject1.springsideproject1build.domain.vo.SchemaName.TEST_COMPANIES_SCHEMA;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.*;
import static springsideproject1.springsideproject1build.domain.vo.Word.*;
import static springsideproject1.springsideproject1build.domain.vo.manager.Layout.*;
import static springsideproject1.springsideproject1build.domain.vo.manager.RequestUrl.*;
import static springsideproject1.springsideproject1build.domain.vo.manager.ViewName.*;
import static springsideproject1.springsideproject1build.util.ControllerUtils.encodeWithUTF8;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerCompanyControllerTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ManagerCompanyControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("기업 추가 페이지 접속")
    @Test
    public void accessCompanyAdd() throws Exception {
        mockMvc.perform(get(ADD_SINGLE_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attributeExists(COMPANY),
                        model().attribute("countries", Country.values()),
                        model().attribute("scales", Scale.values()));
    }

    @DisplayName("기업 추가 완료 페이지 접속")
    @Test
    public void accessCompanyAddFinish() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();

        // then
        mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(ADD_SINGLE_COMPANY_URL + FINISH_URL + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(companyDto.getName())));

        mockMvc.perform(getWithSingleParam(ADD_SINGLE_COMPANY_URL + FINISH_URL,
                        NAME, encodeWithUTF8(companyDto.getName())))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_VIEW + SINGLE_FINISH_VIEW),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                        model().attribute(VALUE, companyDto.getName()));

        assertThat(companyService.findCompanyByName(companyDto.getName()).orElseThrow())
                .usingRecursiveComparison()
                .isEqualTo(samsungElectronics);
    }

    @DisplayName("기업 변경 페이지 접속")
    @Test
    public void accessCompanyModify() throws Exception {
        mockMvc.perform(get(UPDATE_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_VIEW + BEFORE_PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT));
    }

    @DisplayName("기업 변경 페이지 검색")
    @Test
    public void searchCompanyModify() throws Exception {
        // given
        Company company = samsungElectronics;

        // when
        companyService.registerCompany(company);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, "codeOrName", company.getCode()))
                .andExpectAll(status().isOk(),
                        view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute("updateUrl", modifyCompanyFinishUrl))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company.toDto());

        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, "codeOrName", company.getName()))
                .andExpectAll(status().isOk(),
                        view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute("updateUrl", modifyCompanyFinishUrl),
                        model().attribute("countries", Country.values()),
                        model().attribute("scales", Scale.values()))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company.toDto());
    }

    @DisplayName("기업 변경 완료 페이지 접속")
    @Test
    public void accessCompanyModifyFinish() throws Exception {
        // given
        Company beforeModifyCompany = samsungElectronics;
        String commonName = samsungElectronics.getName();

        // when
        companyService.registerCompany(beforeModifyCompany);

        // then
        mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, Company.builder().company(skHynix)
                .name(commonName).code(beforeModifyCompany.getCode()).build().toDto()))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(modifyCompanyFinishUrl + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(commonName)));

        mockMvc.perform(getWithSingleParam(modifyCompanyFinishUrl, NAME, encodeWithUTF8(beforeModifyCompany.getName())))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_VIEW + FINISH_VIEW),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT),
                        model().attribute(VALUE, commonName));

        assertThat(companyService.findCompanyByName(commonName).orElseThrow())
                .usingRecursiveComparison()
                .isEqualTo(beforeModifyCompany);
    }

    @DisplayName("기업들 조회 페이지 접속")
    @Test
    public void accessCompaniesInquiry() throws Exception {
        // given & when
        companyService.registerCompanies(skHynix, samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "companies-page"))
                .andReturn().getModelAndView()).getModelMap().get("companies"))
                .usingRecursiveComparison()
                .isEqualTo(List.of(skHynix, samsungElectronics));
    }

    @DisplayName("기업 없애기 페이지 접속")
    @Test
    public void accessCompanyRid() throws Exception {
        mockMvc.perform(get(REMOVE_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_URL_VIEW + PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT));
    }

    @DisplayName("기업 없애기 완료 페이지 접속")
    @Test
    public void accessCompanyRidFinish() throws Exception {
        // given
        Company company = samsungElectronics;
        String name = company.getName();

        // when & then
        companyService.registerCompany(company);

        mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", company.getCode()))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(REMOVE_COMPANY_URL + FINISH_URL + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(name)));

        companyService.registerCompany(company);

        mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", name))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(REMOVE_COMPANY_URL + FINISH_URL + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(name)));

        mockMvc.perform(getWithSingleParam(REMOVE_COMPANY_URL + FINISH_URL, NAME, encodeWithUTF8(name)))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_URL_VIEW + FINISH_VIEW),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT),
                        model().attribute(VALUE, name));

        assertThat(companyService.findCompanies()).isEmpty();
    }
}