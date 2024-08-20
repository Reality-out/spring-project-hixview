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

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT.*;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.utility.WordUtils.*;
import static springsideproject1.springsideproject1build.utility.MainUtils.encodeUTF8;

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

    @DisplayName("기업 추가 페이지 접속")
    @Test
    public void accessCompanyAdd() throws Exception {
        mockMvc.perform(get(ADD_SINGLE_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attributeExists(COMPANY),
                        model().attribute("countries", Country.values()),
                        model().attribute("scales", Scale.values()));
    }

    @DisplayName("기업 추가 완료 페이지 접속")
    @Test
    public void accessCompanyAddFinish() throws Exception {
        // given
        Company company = createSamsungElectronics();

        // then
        mockMvc.perform(postWithCompany(ADD_SINGLE_COMPANY_URL, company))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(ADD_SINGLE_COMPANY_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(company.getName())));

        mockMvc.perform(getWithSingleParam(ADD_SINGLE_COMPANY_URL + URL_FINISH_SUFFIX,
                        NAME, encodeUTF8(company.getName())))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_ADD_VIEW + VIEW_SINGLE_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(KEY, keyValue),
                        model().attribute(VALUE, company.getName()));
    }

    @DisplayName("기업 변경 페이지 접속")
    @Test
    public void accessCompanyModify() throws Exception {
        mockMvc.perform(get(UPDATE_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue));
    }

    @DisplayName("기업 변경 페이지 내 이름 검색")
    @Test
    public void searchNameCompanyModify() throws Exception {
        // given
        Company company = createSamsungElectronics();

        // when
        companyService.registerCompany(company);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, "codeOrName", company.getName()))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute("updateUrl", UPDATE_COMPANY_URL + URL_FINISH_SUFFIX),
                        model().attribute("countries", Country.values()),
                        model().attribute("scales", Scale.values()))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company.toCompanyDto());
    }

    @DisplayName("기업 변경 페이지 내 코드 검색")
    @Test
    public void searchCodeCompanyModify() throws Exception {
        // given
        Company company = createSamsungElectronics();

        // when
        companyService.registerCompany(company);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, "codeOrName", company.getCode()))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute("updateUrl", UPDATE_COMPANY_URL + URL_FINISH_SUFFIX))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company.toCompanyDto());
    }

    @DisplayName("기업 변경 완료 페이지 접속")
    @Test
    public void accessCompanyModifyFinish() throws Exception {
        // given
        Company company = createSamsungElectronics();

        // when
        companyService.registerCompany(company);

        // then
        mockMvc.perform(postWithCompany(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX, company))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(company.getName())));

        mockMvc.perform(getWithSingleParam(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX,
                        NAME, encodeUTF8(company.getName())))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_UPDATE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(KEY, keyValue),
                        model().attribute(VALUE, company.getName()));
    }

    @DisplayName("기업들 보기 페이지 접속")
    @Test
    public void accessCompaniesSee() throws Exception {
        // given
        companyService.registerCompanies(createSKHynix(), createSamsungElectronics());

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_SELECT_VIEW + "companiesPage"))
                .andReturn().getModelAndView()).getModelMap().get("companies"))
                .usingRecursiveComparison()
                .isEqualTo(List.of(createSKHynix(), createSamsungElectronics()));
    }

    @DisplayName("기업 없애기 페이지 접속")
    @Test
    public void accessCompanyRid() throws Exception {
        mockMvc.perform(get(REMOVE_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_REMOVE_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(DATA_TYPE_ENGLISH, COMPANY),
                        model().attribute(REMOVE_KEY, "codeOrName"));
    }

    @DisplayName("기업 없애기 완료 페이지 접속")
    @Test
    public void accessCompanyRidFinish() throws Exception {
        // given
        Company article = createSamsungElectronics();
        String name = article.getName();

        // when
        companyService.registerCompany(article);

        // then
        mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", name))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(REMOVE_COMPANY_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(name)));

        mockMvc.perform(getWithSingleParam(REMOVE_COMPANY_URL + URL_FINISH_SUFFIX, NAME, encodeUTF8(name)))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_REMOVE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(KEY, keyValue),
                        model().attribute(VALUE, name));
    }
}