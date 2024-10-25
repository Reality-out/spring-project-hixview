package site.hixview.web.controller.mock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.service.ArticleMainService;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.service.HomeService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.ArticleMainTestUtils;
import site.hixview.support.util.CompanyArticleTestUtils;
import site.hixview.support.util.CompanyTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.ViewName.VIEW_SUB;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestUrl.*;
import static site.hixview.domain.vo.user.ViewName.COMPANY_VIEW;

@OnlyRealControllerContext
class UserCompanyControllerTest implements CompanyTestUtils, CompanyArticleTestUtils, ArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HomeService homeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyArticleService companyArticleService;

    @Autowired
    private ArticleMainService articleMainService;

    @DisplayName("기업 서브 페이지 접속")
    @Test
    void accessCompanySubPage() throws Exception {
        mockMvc.perform(get(COMPANY_SUB_URL))
                .andExpectAll(status().isOk(),
                        view().name(COMPANY_VIEW + VIEW_SUB),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT));
    }

    @DisplayName("유효한 기업 코드로 기업 검색")
    @Test
    void validCodeSearchCompany() throws Exception {
        // given
        Company company = samsungElectronics;
        String code = company.getCode();
        doNothing().when(companyService).registerCompany(company);
        when(companyService.findCompanyByCodeOrName(code)).thenReturn(Optional.of(company));
        when(companyService.findCompanyByCode(code)).thenReturn(Optional.of(company));

        // when
        companyService.registerCompany(company);

        // then
        Map<String, String> returnMap = new ObjectMapper().readValue(mockMvc.perform(get(COMPANY_SEARCH_URL + CHECK_URL + code))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(returnMap.get(CODE)).isEqualTo(code);

        assertThat(Objects.requireNonNull(mockMvc.perform(get(COMPANY_SEARCH_URL + code))
                .andExpect(status().isOk())
                .andReturn().getModelAndView()).getModelMap().getAttribute(COMPANY))
                .usingRecursiveComparison().isEqualTo(company);
    }

    @DisplayName("유효하지 않은 기업 코드로 기업 검색")
    @Test
    void invalidCodeSearchCompany() throws Exception {
        // given
        when(companyArticleService.registerArticle(testCompanyArticle)).thenReturn(testCompanyArticle);
        when(articleMainService.registerArticle(testCompanyArticleMain)).thenReturn(testCompanyArticleMain);
        when(companyService.findCompanyByCode(any())).thenReturn(Optional.empty());
        when(homeService.findUsableLatestCompanyArticles()).thenReturn(List.of(testCompanyArticle));
        when(companyService.findCompanyByName(testCompanyArticle.getSubjectCompany())).thenReturn(Optional.of(samsungElectronics));

        // when
        companyService.registerCompany(samsungElectronics);
        companyArticleService.registerArticle(testCompanyArticle);
        articleMainService.registerArticle(testCompanyArticleMain);

        // then
        assertThat(Objects.requireNonNull(mockMvc.perform(get(COMPANY_SEARCH_URL))
                .andExpect(status().isOk())
                .andReturn().getModelAndView()).getModelMap().getAttribute(COMPANY))
                .usingRecursiveComparison().isEqualTo(samsungElectronics);

        assertThat(Objects.requireNonNull(mockMvc.perform(get(COMPANY_SEARCH_URL + INVALID_VALUE))
                .andExpect(status().isOk())
                .andReturn().getModelAndView()).getModelMap().getAttribute(COMPANY))
                .usingRecursiveComparison().isEqualTo(samsungElectronics);
    }
}