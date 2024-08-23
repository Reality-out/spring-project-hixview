package springsideproject1.springsideproject1build.controller.manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerMainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("관리자 메인 페이지 접속")
    @Test
    public void accessManagerMainPage() throws Exception {
        mockMvc.perform(get("/manager"))
                .andExpectAll(status().isOk(),
                        view().name("manager/mainPage"),
                        model().attribute("addSingleCompanyArticle", ADD_SINGLE_COMPANY_ARTICLE_URL),
                        model().attribute("updateCompanyArticle", UPDATE_COMPANY_ARTICLE_URL),
                        model().attribute("removeCompanyArticle", REMOVE_COMPANY_ARTICLE_URL),
                        model().attribute("addCompanyArticlesWithString", ADD_COMPANY_ARTICLE_WITH_STRING_URL),
                        model().attribute("selectCompanyArticles", SELECT_COMPANY_ARTICLE_URL),
                        model().attribute("removeMember", REMOVE_MEMBER_URL),
                        model().attribute("selectMembers", SELECT_MEMBER_URL));
    }
}