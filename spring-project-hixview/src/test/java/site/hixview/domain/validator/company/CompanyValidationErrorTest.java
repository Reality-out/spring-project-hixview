package site.hixview.domain.validator.company;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.entity.company.dto.CompanyDto;
import site.hixview.domain.service.CompanyService;
import site.hixview.support.context.RealControllerAndValidatorContext;
import site.hixview.support.util.CompanyTestUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_COMPANY_URL;

@RealControllerAndValidatorContext
class CompanyValidationErrorTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyService companyService;

    @DisplayName("중복 기업 코드 또는 기업명을 사용하는 기업 추가")
    @Test
    public void duplicatedCodeOrNameCompanyAdd() throws Exception {
        // given
        Company company = samsungElectronics;
        when(companyService.findCompanyByCode(company.getCode())).thenReturn(Optional.of(company));
        when(companyService.findCompanyByName(company.getName())).thenReturn(Optional.of(company));
        doNothing().when(companyService).registerCompany(company);

        CompanyDto companyDtoDuplicatedCode = createSKHynixDto();
        companyDtoDuplicatedCode.setCode(company.getCode());
        CompanyDto companyDtoDuplicatedName = createSKHynixDto();
        companyDtoDuplicatedName.setName(company.getName());

        // when
        companyService.registerCompany(company);

        // then
        for (CompanyDto companyDto : List.of(companyDtoDuplicatedCode, companyDtoDuplicatedName)) {
            assertThat(requireNonNull(mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                    .andExpectAll(view().name(addSingleCompanyProcessPage),
                            model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                            model().attribute(ERROR, (String) null))
                    .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                    .usingRecursiveComparison()
                    .isEqualTo(companyDto);
        }
    }

    @DisplayName("기업 코드 또는 기업명까지 변경을 시도하는, 기업 변경")
    @Test
    public void changeCodeOrNameCompanyModify() throws Exception {
        // given
        Company company = samsungElectronics;
        when(companyService.findCompanyByName(company.getName())).thenReturn(Optional.empty());
        doNothing().when(companyService).registerCompany(company);

        // when
        companyService.registerCompany(company);

        // then
        requireNonNull(mockMvc.perform(postWithCompany(modifyCompanyFinishUrl,
                        Company.builder().company(company).code(skHynix.getCode()).build()))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null)));

        requireNonNull(mockMvc.perform(postWithCompany(modifyCompanyFinishUrl,
                        Company.builder().company(company).name(skHynix.getName()).build()))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null)));
    }
}
