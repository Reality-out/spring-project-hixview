package site.hixview.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.Scale;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.entity.company.CompanyDto;
import site.hixview.domain.service.CompanyService;
import site.hixview.util.test.CompanyTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.*;
import static site.hixview.domain.vo.name.EntityName.Company.COMPANY;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.domain.vo.name.ViewName.*;

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
        String redirectedURL = fromPath(ADD_SINGLE_COMPANY_URL + FINISH_URL).queryParam(NAME, companyDto.getName()).build().toUriString();

        // then
        mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_VIEW + VIEW_SINGLE_FINISH),
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
                        view().name(UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS),
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
        Company company = Company.builder().company(skHynix)
                .name(commonName).code(beforeModifyCompany.getCode()).build();
        String redirectedURL = fromPath(UPDATE_COMPANY_URL + FINISH_URL).queryParam(NAME, commonName).build().toUriString();

        // when
        companyService.registerCompany(beforeModifyCompany);

        // then
        mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, company.toDto()))
                .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT),
                        model().attribute(VALUE, commonName));

        assertThat(companyService.findCompanyByName(commonName).orElseThrow())
                .usingRecursiveComparison()
                .isEqualTo(company);
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
                        view().name(REMOVE_COMPANY_URL_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT));
    }

    @DisplayName("기업 없애기 완료 페이지 접속")
    @Test
    public void accessCompanyRidFinish() throws Exception {
        // given
        Company company = samsungElectronics;
        String name = company.getName();
        String redirectedURL = fromPath(REMOVE_COMPANY_URL + FINISH_URL).queryParam(NAME, name).build().toUriString();

        // when & then
        companyService.registerCompany(company);

        mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", company.getCode()))
                .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

        companyService.registerCompany(company);

        mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", name))
                .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_URL_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT),
                        model().attribute(VALUE, name));

        assertThat(companyService.findCompanies()).isEmpty();
    }
}