package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.config.annotation.MockServiceConfig;
import site.hixview.domain.config.annotation.MockValidatorConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.MANAGER_HOME_VIEW;

@MockServiceConfig
@MockValidatorConfig
class ManagerMainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("관리자 메인 페이지 접속")
    @Test
    void accessManagerMainPage() throws Exception {
        mockMvc.perform(get("/manager"))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_HOME_VIEW),
                        model().attribute("addSingleCompanyArticle", ADD_SINGLE_COMPANY_ARTICLE_URL),
                        model().attribute("updateCompanyArticle", UPDATE_COMPANY_ARTICLE_URL),
                        model().attribute("removeCompanyArticle", REMOVE_COMPANY_ARTICLE_URL),
                        model().attribute("addCompanyArticlesWithString", ADD_COMPANY_ARTICLE_WITH_STRING_URL),
                        model().attribute("selectCompanyArticles", SELECT_COMPANY_ARTICLE_URL),
                        model().attribute("selectMembers", SELECT_MEMBER_URL));
    }
}