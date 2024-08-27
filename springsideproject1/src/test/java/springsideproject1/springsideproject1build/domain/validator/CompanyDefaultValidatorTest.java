package springsideproject1.springsideproject1build.domain.validator;

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
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.BEAN_VALIDATION_ERROR;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.ERROR;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.COMPANY;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.ADD_COMPANY_VIEW;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.VIEW_SINGLE_PROCESS_SUFFIX;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyDefaultValidatorTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyDefaultValidatorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, COMPANY_TABLE, false);
    }

    @DisplayName("NotBlank(공백)에 대한 기업 추가 유효성 검증")
    @Test
    public void validateNotBlankSpaceCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(" ");
        companyDto.setCountry(" ");
        companyDto.setScale(" ");
        companyDto.setName(" ");
        companyDto.setFirstCategory(" ");
        companyDto.setSecondCategory(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("NotBlank(null)에 대한 기업 추가 유효성 검증")
    @Test
    public void validateNotBlankNullCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(null);
        companyDto.setCountry(null);
        companyDto.setScale(null);
        companyDto.setName(null);
        companyDto.setFirstCategory(null);
        companyDto.setSecondCategory(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Pattern에 대한 기업 추가 유효성 검증")
    @Test
    public void validatePatternCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("6자리보다 큰 Size에 대한 기업 추가 유효성 검증")
    @Test
    public void validateBiggerSizeCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode("0000000");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("6자리보다 작은 Size에 대한 기업 추가 유효성 검증")
    @Test
    public void validateSmallerSizeCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode("00000");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("NotBlank(공백)에 대한 기업 변경 유효성 검증")
    @Test
    public void validateNotBlankSpaceCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(" ");
        companyDto.setCountry(" ");
        companyDto.setScale(" ");
        companyDto.setName(" ");
        companyDto.setFirstCategory(" ");
        companyDto.setSecondCategory(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("NotBlank(null)에 대한 기업 변경 유효성 검증")
    @Test
    public void validateNotBlankNullCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(null);
        companyDto.setCountry(null);
        companyDto.setScale(null);
        companyDto.setName(null);
        companyDto.setFirstCategory(null);
        companyDto.setSecondCategory(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Pattern에 대한 기업 변경 유효성 검증")
    @Test
    public void validatePatternCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("6자리보다 큰 Size에 대한 기업 변경 유효성 검증")
    @Test
    public void validateBiggerSizeCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode("0000000");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("6자리보다 작은 Size에 대한 기업 변경 유효성 검증")
    @Test
    public void validateSmallerSizeCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode("00000");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }
}
