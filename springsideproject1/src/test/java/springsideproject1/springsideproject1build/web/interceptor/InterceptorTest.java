package springsideproject1.springsideproject1build.web.interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.company.CompanyArticle;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleMainService;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.util.test.CompanyArticleMainTestUtils;
import springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.USER_HOME_VIEW;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InterceptorTest implements CompanyArticleTestUtils, CompanyArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService companyArticleService;

    @Autowired
    CompanyArticleMainService companyArticleMainService;

    @DisplayName("URL 맨 끝 슬래시 제거 인터셉터 테스트")
    @Test
    public void HandleUrlLastSlashInterceptorTest() throws Exception {
        // given & when
        companyArticleMainService.registerArticle(testCompanyArticleMain);
        companyArticleService.registerArticle(CompanyArticle.builder()
                .article(testCompanyArticle).name(testCompanyArticleMain.getName()).build());

        // then
        mockMvc.perform(getWithNoParam(""))
                .andExpectAll(status().isOk(),
                        view().name(USER_HOME_VIEW),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT_PATH));

        mockMvc.perform(getWithNoParam("/"))
                .andExpectAll(status().isOk(),
                        view().name(USER_HOME_VIEW),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT_PATH));
    }
}
