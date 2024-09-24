package site.hixview.domain.validator.company;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.config.annotation.OnlyRealControllerConfig;
import site.hixview.domain.entity.company.CompanyDto;
import site.hixview.domain.service.CompanyService;
import site.hixview.util.test.CompanyTestUtils;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.Word.ERROR;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_COMPANY_URL;
import static site.hixview.domain.vo.name.EntityName.Company.COMPANY;
import static site.hixview.domain.vo.name.ExceptionName.BEAN_VALIDATION_ERROR;

@OnlyRealControllerConfig
class CompanyBindingErrorTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyService companyService;

    @DisplayName("NotBlank(공백)에 대한 기업 추가 유효성 검증")
    @Test
    void validateNotBlankSpaceCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setCode(" ");
        companyDto.setCountry(" ");
        companyDto.setScale(" ");
        companyDto.setName(" ");
        companyDto.setFirstCategory(" ");
        companyDto.setSecondCategory(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("NotBlank(null)에 대한 기업 추가 유효성 검증")
    @Test
    void validateNotBlankNullCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setCode(null);
        companyDto.setCountry(null);
        companyDto.setScale(null);
        companyDto.setName(null);
        companyDto.setFirstCategory(null);
        companyDto.setSecondCategory(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Pattern에 대한 기업 추가 유효성 검증")
    @Test
    void validatePatternCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setCode(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Size에 대한 기업 추가 유효성 검증")
    @Test
    void validateBiggerSizeCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDtoLongCode = createSamsungElectronicsDto();
        companyDtoLongCode.setCode(getRandomLongString(7));

        CompanyDto companyDtoShortCode = createSamsungElectronicsDto();
        companyDtoShortCode.setCode(getRandomLongString(5));

        CompanyDto companyDtoLongName = createSamsungElectronicsDto();
        companyDtoLongName.setName(getRandomLongString(13));

        // then
        for (CompanyDto companyDto : List.of(companyDtoLongCode, companyDtoShortCode, companyDtoLongName)) {
            assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                    .andExpectAll(view().name(addSingleCompanyProcessPage),
                            model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                            model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                    .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                    .usingRecursiveComparison()
                    .isEqualTo(companyDto);
        }
    }

    @DisplayName("Country의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    void validateCountryTypeMismatchCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setCountry(INVALID_VALUE.toUpperCase());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Scale의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    void validateScaleTypeMismatchCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setScale(INVALID_VALUE.toUpperCase());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("FirstCategory의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    void validateFirstCategoryTypeMismatchCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setFirstCategory(INVALID_VALUE.toUpperCase());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("SecondCategory의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    void validateSecondCategoryTypeMismatchCompanyAdd() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setSecondCategory(INVALID_VALUE.toUpperCase());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                .andExpectAll(view().name(addSingleCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("NotBlank(공백)에 대한 기업 변경 유효성 검증")
    @Test
    void validateNotBlankSpaceCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setCode(" ");
        companyDto.setCountry(" ");
        companyDto.setScale(" ");
        companyDto.setName(" ");
        companyDto.setFirstCategory(" ");
        companyDto.setSecondCategory(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("NotBlank(null)에 대한 기업 변경 유효성 검증")
    @Test
    void validateNotBlankNullCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setCode(null);
        companyDto.setCountry(null);
        companyDto.setScale(null);
        companyDto.setName(null);
        companyDto.setFirstCategory(null);
        companyDto.setSecondCategory(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Pattern에 대한 기업 변경 유효성 검증")
    @Test
    void validatePatternCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setCode(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Size에 대한 기업 변경 유효성 검증")
    @Test
    void validateBiggerSizeCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDtoLongCode = createSamsungElectronicsDto();
        companyDtoLongCode.setCode(getRandomLongString(7));

        CompanyDto companyDtoShortCode = createSamsungElectronicsDto();
        companyDtoShortCode.setCode(getRandomLongString(5));

        CompanyDto companyDtoLongName = createSamsungElectronicsDto();
        companyDtoLongName.setName(getRandomLongString(13));

        // then
        for (CompanyDto companyDto : List.of(companyDtoLongCode, companyDtoShortCode, companyDtoLongName)) {
            assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                    .andExpectAll(view().name(modifyCompanyProcessPage),
                            model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                            model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                    .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                    .usingRecursiveComparison()
                    .isEqualTo(companyDto);
        }
    }

    @DisplayName("Country의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    void validateCountryTypeMismatchCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setCountry(INVALID_VALUE.toUpperCase());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("Scale의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    void validateScaleTypeMismatchCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setScale(INVALID_VALUE.toUpperCase());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("FirstCategory의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    void validateFirstCategoryTypeMismatchCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setFirstCategory(INVALID_VALUE.toUpperCase());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }

    @DisplayName("SecondCategory의 typeMismatch에 대한 기업 추가 유효성 검증")
    @Test
    void validateSecondCategoryTypeMismatchCompanyModify() throws Exception {
        // given & when
        CompanyDto companyDto = createSamsungElectronicsDto();
        companyDto.setSecondCategory(INVALID_VALUE.toUpperCase());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(companyDto);
    }
}
