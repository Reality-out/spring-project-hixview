package springsideproject1.springsideproject1build.domain.validator.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.company.CompanyDto;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.util.test.CompanyTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.ERROR;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.COMPANY;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.ADD_SINGLE_COMPANY_URL;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyConstraintValidatorTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyConstraintValidatorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, COMPANY_TABLE);
    }

    @DisplayName("code의 Exist에 대한 기업 추가 유효성 검증")
    @Test
    public void validateCodeExistCompanyAdd() throws Exception {
        // given
        CompanyDto companyDto = createSKHynixDto();
        companyDto.setCode(samsungElectronics.getCode());

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("name의 Exist에 대한 기업 추가 유효성 검증")
    @Test
    public void validateNameExistCompanyAdd() throws Exception {
        // given
        CompanyDto companyDto = createSKHynixDto();
        companyDto.setName(samsungElectronics.getName());

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Country의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    public void validateCountryTypeMismatchCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setCountry(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Scale의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    public void validateScaleTypeMismatchCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setScale(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("FirstCategory의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    public void validateFirstCategoryTypeMismatchCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setFirstCategory(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("SecondCategory의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    public void validateSecondCategoryTypeMismatchCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setSecondCategory(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("code의 Exist에 대한 기업 변경 유효성 검증")
    @Test
    public void validateCodeExistCompanyModify() throws Exception {
        // given
        CompanyDto companyDto = createSKHynixDto();
        companyDto.setCode(samsungElectronics.getCode());

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("name의 Exist에 대한 기업 추가 유효성 검증")
    @Test
    public void validateNameExistCompanyModify() throws Exception {
        // given
        CompanyDto companyDto = createSKHynixDto();
        companyDto.setName(samsungElectronics.getName());

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Country의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    public void validateCountryTypeMismatchCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setCountry(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Scale의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    public void validateScaleTypeMismatchCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setScale(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("FirstCategory의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    public void validateFirstCategoryTypeMismatchCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setFirstCategory(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("SecondCategory의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    public void validateSecondCategoryTypeMismatchCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setSecondCategory(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }
}
