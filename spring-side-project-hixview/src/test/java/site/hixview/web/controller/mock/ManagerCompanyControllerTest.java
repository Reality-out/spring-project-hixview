package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.Scale;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.entity.company.CompanyDto;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.validation.validator.CompanyAddValidator;
import site.hixview.domain.validation.validator.CompanyModifyValidator;
import site.hixview.support.util.CompanyTestUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.*;
import static site.hixview.domain.vo.name.EntityName.Company.COMPANY;
import static site.hixview.domain.vo.name.ViewName.*;

@OnlyRealControllerContext
class ManagerCompanyControllerTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyAddValidator companyAddValidator;

    @Autowired
    private CompanyModifyValidator companyModifyValidator;

    @DisplayName("기업 추가 페이지 접속")
    @Test
    void accessCompanyAdd() throws Exception {
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
    void accessCompanyAddFinish() throws Exception {
        // given & when
        Company company = samsungElectronics;
        when(companyService.findCompanyByName(company.getName())).thenReturn(Optional.of(company));
        doNothing().when(companyService).registerCompany(argThat(Objects::nonNull));
        doNothing().when(companyAddValidator).validate(any(), any());

        CompanyDto companyDto = company.toDto();
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

    @DisplayName("기업들 조회 페이지 접속")
    @Test
    void accessCompaniesInquiry() throws Exception {
        // given & when
        List<Company> storedList = List.of(samsungElectronics, skHynix);
        when(companyService.findCompanies()).thenReturn(storedList);
        doNothing().when(companyService).registerCompanies(samsungElectronics, skHynix);

        companyService.registerCompanies(samsungElectronics, skHynix);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "companies-page"))
                .andReturn().getModelAndView()).getModelMap().get("companies"))
                .usingRecursiveComparison()
                .isEqualTo(storedList);
    }

    @DisplayName("기업 변경 페이지 접속")
    @Test
    void accessCompanyModify() throws Exception {
        mockMvc.perform(get(UPDATE_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT));
    }

    @DisplayName("기업 변경 페이지 검색")
    @Test
    void searchCompanyModify() throws Exception {
        // given
        Company company = samsungElectronics;
        when(companyService.findCompanyByCodeOrName(String.valueOf(company.getCode()))).thenReturn(Optional.of(company));
        when(companyService.findCompanyByCodeOrName(company.getName())).thenReturn(Optional.of(company));
        doNothing().when(companyService).registerCompany(company);

        CompanyDto companyDto = company.toDto();

        // when
        companyService.registerCompany(company);

        // then
        for (String str : List.of(company.getCode(), company.getName())) {
            assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, "codeOrName", str))
                    .andExpectAll(status().isOk(),
                            view().name(modifyCompanyProcessPage),
                            model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                            model().attribute("updateUrl", modifyCompanyFinishUrl))
                    .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                    .usingRecursiveComparison()
                    .isEqualTo(companyDto);
        }
    }

    @DisplayName("기업 변경 완료 페이지 접속")
    @Test
    void accessCompanyModifyFinish() throws Exception {
        // given
        Company company = Company.builder().company(skHynix)
                .name(samsungElectronics.getName()).code(samsungElectronics.getCode()).build();
        when(companyService.findCompanyByCode(company.getCode())).thenReturn(Optional.of(company));
        when(companyService.findCompanyByName(company.getName())).thenReturn(Optional.of(company));
        doNothing().when(companyService).registerCompany(samsungElectronics);
        doNothing().when(companyService).correctCompany(company);

        String commonName = samsungElectronics.getName();
        String redirectedURL = fromPath(UPDATE_COMPANY_URL + FINISH_URL).queryParam(NAME, commonName).build().toUriString();

        // when
        companyService.registerCompany(samsungElectronics);

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

    @DisplayName("기업 없애기 페이지 접속")
    @Test
    void accessCompanyRid() throws Exception {
        mockMvc.perform(get(REMOVE_COMPANY_URL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_URL_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT));
    }

    @DisplayName("기업 없애기 완료 페이지 접속")
    @Test
    void accessCompanyRidFinish() throws Exception {
        // given & when
        Company company = samsungElectronics;
        when(companyService.findCompanies()).thenReturn(emptyList());
        when(companyService.findCompanyByCode(company.getCode())).thenReturn(Optional.of(company));
        when(companyService.findCompanyByName(company.getName())).thenReturn(Optional.of(company));
        when(companyService.findCompanyByCodeOrName(String.valueOf(company.getCode()))).thenReturn(Optional.of(company));
        when(companyService.findCompanyByCodeOrName(company.getName())).thenReturn(Optional.of(company));
        doNothing().when(companyService).registerCompany(argThat(Objects::nonNull));
        doNothing().when(companyService).removeCompanyByCode(company.getCode());

        String name = company.getName();
        String redirectedURL = fromPath(REMOVE_COMPANY_URL + FINISH_URL).queryParam(NAME, name).build().toUriString();

        // then
        for (String str : List.of(company.getCode(), name)) {
            companyService.registerCompany(company);

            mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", str))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));
        }

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_URL_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT),
                        model().attribute(VALUE, name));

        assertThat(companyService.findCompanies()).isEmpty();
    }
}