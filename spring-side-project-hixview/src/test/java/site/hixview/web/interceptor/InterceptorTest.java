package site.hixview.web.interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.service.ArticleMainService;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.util.test.ArticleMainTestUtils;
import site.hixview.util.test.CompanyArticleTestUtils;
import site.hixview.util.test.IndustryArticleTestUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.ViewName.USER_HOME_VIEW;

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
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT));

        mockMvc.perform(getWithNoParam("/"))
                .andExpectAll(status().isOk(),
                        view().name(USER_HOME_VIEW),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT));
    }
}
