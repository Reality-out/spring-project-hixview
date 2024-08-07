package springsideproject1.springsideproject1build.controller.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.company.Company;
import springsideproject1.springsideproject1build.domain.company.Country;
import springsideproject1.springsideproject1build.domain.company.Scale;
import springsideproject1.springsideproject1build.service.CompanyService;
import springsideproject1.springsideproject1build.utility.test.CompanyTestUtility;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;
import static springsideproject1.springsideproject1build.utility.ConstantUtility.*;
import static springsideproject1.springsideproject1build.utility.MainUtility.encodeUTF8;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerCompanyControllerTest implements CompanyTestUtility {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;
    private final String dataTypeKorValue = "기업";
    private final String keyValue = "기업명";

    @Autowired
    public ManagerCompanyControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, companyTable, true);
    }

    @DisplayName("단일 기업 등록 페이지 접속")
    @Test
    public void accessCompanyRegisterPage() throws Exception {
        mockMvc.perform(get(ADD_SINGLE_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attributeExists(COMPANY),
                        model().attribute("countries", Country.values()),
                        model().attribute("scales", Scale.values()));
    }

    @DisplayName("단일 기업 등록 완료 페이지 접속")
    @Test
    public void accessCompanyRegisterFinishPage() throws Exception {
        // given
        Company company = createSamsungElectronics();

        // then
        mockMvc.perform(processPostWithCompany(ADD_SINGLE_COMPANY_URL, company))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(ADD_SINGLE_COMPANY_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(company.getName())));

        mockMvc.perform(processGetWithSingleParam(ADD_SINGLE_COMPANY_URL + URL_FINISH_SUFFIX,
                        NAME, encodeUTF8(company.getName())))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_ADD_VIEW + VIEW_SINGLE_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(KEY, keyValue),
                        model().attribute(VALUE, company.getName()));
    }

    @DisplayName("단일 기업 갱신 페이지 접속")
    @Test
    public void accessCompanyUpdatePage() throws Exception {
        mockMvc.perform(get(UPDATE_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue));
    }

    @DisplayName("단일 기업 갱신 페이지 내 이름 검색")
    @Test
    public void searchNameInCompanyUpdatePage() throws Exception {
        // given
        Company company = createSamsungElectronics();

        // when
        companyService.joinCompany(company);

        // then
        assertThat(mockMvc.perform(processPostWithSingleParam(UPDATE_COMPANY_URL, "nameOrCode", company.getName()))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute("updateUrl", UPDATE_COMPANY_URL + URL_FINISH_SUFFIX),
                        model().attribute("countries", Country.values()),
                        model().attribute("scales", Scale.values()))
                .andReturn().getModelAndView().getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company.toCompanyDto());
    }

    @DisplayName("단일 기업 갱신 페이지 내 코드 검색")
    @Test
    public void searchCodeInCompanyUpdatePage() throws Exception {
        // given
        Company company = createSamsungElectronics();

        // when
        companyService.joinCompany(company);

        // then
        assertThat(mockMvc.perform(processPostWithSingleParam(UPDATE_COMPANY_URL, "nameOrCode", company.getCode()))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute("updateUrl", UPDATE_COMPANY_URL + URL_FINISH_SUFFIX))
                .andReturn().getModelAndView().getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company.toCompanyDto());
    }

    @DisplayName("단일 기업 갱신 완료 페이지 접속")
    @Test
    public void accessCompanyUpdateFinishPage() throws Exception {
        // given
        Company company = createSamsungElectronics();

        // when
        companyService.joinCompany(company);

        // then
        mockMvc.perform(processPostWithCompany(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX, company))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(company.getName())));

        mockMvc.perform(processGetWithSingleParam(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX,
                        NAME, encodeUTF8(company.getName())))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_UPDATE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(KEY, keyValue),
                        model().attribute(VALUE, company.getName()));
    }

    @DisplayName("모든 기업 테이블 페이지 접속")
    @Test
    public void accessCompaniesPage() throws Exception {
        // given
        companyService.joinCompanies(createSKHynix(), createSamsungElectronics());

        // then
        assertThat(mockMvc.perform(get(SELECT_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_SELECT_VIEW + "companiesPage"))
                .andReturn().getModelAndView().getModelMap().get("companies"))
                .usingRecursiveComparison()
                .isEqualTo(List.of(createSKHynix(), createSamsungElectronics()));
    }

    @DisplayName("단일 기업 삭제 페이지 접속")
    @Test
    public void accessCompanyRemovePage() throws Exception {
        mockMvc.perform(get(REMOVE_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_REMOVE_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(DATA_TYPE_ENGLISH, COMPANY),
                        model().attribute(REMOVE_KEY, "nameOrCode"));
    }

    @DisplayName("단일 기업 삭제 완료 페이지 접속")
    @Test
    public void accessCompanyRemoveFinishPage() throws Exception {
        // given
        Company article = createSamsungElectronics();
        String name = article.getName();

        // when
        companyService.joinCompany(article);

        // then
        mockMvc.perform(processPostWithSingleParam(REMOVE_COMPANY_URL, "nameOrCode", name))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(REMOVE_COMPANY_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(name)));

        mockMvc.perform(processGetWithSingleParam(REMOVE_COMPANY_URL + URL_FINISH_SUFFIX, NAME, encodeUTF8(name)))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_REMOVE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(KEY, keyValue),
                        model().attribute(VALUE, name));
    }
}