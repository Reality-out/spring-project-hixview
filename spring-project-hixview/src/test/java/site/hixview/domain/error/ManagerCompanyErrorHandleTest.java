package site.hixview.domain.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.service.CompanyService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.CompanyTestUtils;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.REMOVE_COMPANY_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_COMPANY_URL;
import static site.hixview.domain.vo.manager.ViewName.REMOVE_COMPANY_URL_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_COMPANY_VIEW;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_COMPANY_ERROR;
import static site.hixview.domain.vo.name.ViewName.VIEW_BEFORE_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;

@OnlyRealControllerContext
class ManagerCompanyErrorHandleTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyService companyService;

    @DisplayName("존재하지 않는 기업 코드 또는 기업명을 사용하여 기업을 검색하는, 기업 변경")
    @Test
    void notFoundCodeOrNameCompanyModify() throws Exception {
        // given & when
        when(companyService.findCompanyByCodeOrName(any())).thenReturn(Optional.empty());

        // then
        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, CODE_OR_NAME, ""))
                .andExpectAll(view().name(UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, CODE_OR_NAME, "000000"))
                .andExpectAll(view().name(UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, CODE_OR_NAME, INVALID_VALUE))
                .andExpectAll(view().name(UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));
    }

    @DisplayName("존재하지 않는 기업 코드 또는 기업명을 사용하는, 기업 없애기")
    @Test
    void notFoundCodeOrNameCompanyRid() throws Exception {
        // given & when
        when(companyService.findCompanyByCodeOrName(any())).thenReturn(Optional.empty());

        // then
        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, CODE_OR_NAME, ""))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, CODE_OR_NAME, "000000"))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, CODE_OR_NAME, INVALID_VALUE))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));
    }
}
