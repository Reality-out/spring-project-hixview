package springsideproject1.springsideproject1build.web.interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.service.ArticleMainService;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.domain.service.IndustryArticleService;
import springsideproject1.springsideproject1build.util.test.ArticleMainTestUtils;
import springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils;
import springsideproject1.springsideproject1build.util.test.IndustryArticleTestUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.domain.vo.LAYOUT.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.LAYOUT.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.VIEW_NAME.USER_HOME_VIEW;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InterceptorTest implements CompanyArticleTestUtils, IndustryArticleTestUtils, ArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService companyArticleService;

    @Autowired
    IndustryArticleService industryArticleService;

    @Autowired
    ArticleMainService articleMainService;

    @DisplayName("URL 맨 끝 슬래시 제거 인터셉터 테스트")
    @Test
    public void HandleUrlLastSlashInterceptorTest() throws Exception {
        // given & when
        articleMainService.registerArticle(testCompanyArticleMain);
        articleMainService.registerArticle(testIndustryArticleMain);
        companyArticleService.registerArticle(testCompanyArticle);
        industryArticleService.registerArticle(testIndustryArticle);

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
