package site.hixview.support.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.entity.home.dto.BlogPostDto;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_BLOG_POST_URL;
import static site.hixview.domain.vo.manager.ViewName.ADD_BLOG_POST_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_BLOG_POST_VIEW;
import static site.hixview.domain.vo.name.ViewName.VIEW_AFTER_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;

public interface BlogPostTestUtils extends ObjectTestUtils {
    // Assertion
    String addBlogPostProcessPage = ADD_BLOG_POST_VIEW + VIEW_PROCESS;
    String modifyBlogPostProcessPage = UPDATE_BLOG_POST_VIEW + VIEW_AFTER_PROCESS;
    String modifyBlogPostFinishUrl = UPDATE_BLOG_POST_URL + FINISH_URL;

    // Schema Name
    String TEST_BLOG_POSTS_SCHEMA = "test_blog_posts";

    // Test Object
    BlogPost testBlogPostCompany = BlogPost.builder()
            .name("이노와이어리스 투자 포인트 재확인")
            .link("https://blog.naver.com/akdnjs0308/223491404394")
            .date(LocalDate.of(2024, 6, 26))
            .classification(Classification.COMPANY)
            .targetName("이노와이어리스")
            .targetImagePath("/images/company/logo/inno_wireless.png")
            .targetArticleNames(List.of("6G, 초연결 시대를 열다", "\"다가오는 6G 시대, 5G에서 교훈 삼아야\"", "5G 성장세 '뚝'...킬러 콘텐츠 없고 5G 단말에 LTE 요금제 허용까지", "\"빅테크 주도로 전세계 인터넷 트래픽 23% 증가\"", "中 5.5G 열올릴 때… 韓, 한발 앞서 ‘6G 주도권’ 선점"))
            .targetArticleLinks(List.of("https://www.etri.re.kr/webzine/20210709/sub01.html", "https://www.topdaily.kr/articles/95860", "https://www.digitaltoday.co.kr/news/articleView.html?idxno=494324", "https://www.fnnews.com/news/202301181417178001", "https://www.fnnews.com/news/202404231827480715"))
            .build();

    BlogPost testBlogPostEconomy = BlogPost.builder()
            .name("중국 경제 진단(중국은 어떤 처지에 놓여 있을까)")
            .link("https://blog.naver.com/akdnjs0308/223527440189")
            .date(LocalDate.of(2024, 7, 27))
            .classification(Classification.ECONOMY)
            .targetName("중국")
            .targetImagePath("/images/economy/flag/chinese_flag_001.png")
            .targetArticleNames(List.of("'경제 위기론' 인정…中, 부동산 부채에 칼 뺀다", "2024년 상반기 중국 경제, 소비 기여도 60.5% 기록", "중국 총부채 비율 300% 육박…일본식 장기침체 현실화되나", "지방정부 인프라 남발 부메랑…中 '숨은 부채'만 최대 11조弗", "中 정부, 사상 최악 실업률로 통계 발표 잠정 중단"))
            .targetArticleLinks(List.of("https://www.sedaily.com/NewsView/2DBSVTPLGN", "https://www.nvp.co.kr/news/articleView.html?idxno=312386", "https://www.sedaily.com/NewsView/2D9E1I8EAF", "https://www.sedaily.com/NewsView/2DBR29XBZG", "https://csf.kiep.go.kr/issueInfoView.es?article_id=51325&mid=a20200000000&board_id=2"))
            .build();

    /**
     * Create
     */
    default BlogPostDto createTestBlogPostCompanyDto() {
        return testBlogPostCompany.toDto();
    }

    default BlogPostDto createTestBlogPostEconomyDto() {
        return testBlogPostEconomy.toDto();
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder postWithBlogPost(String url, BlogPost post) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NUMBER, String.valueOf(post.getNumber()))
                .param(NAME, post.getName())
                .param(LINK, post.getLink())
                .param(DATE, post.getDate().toString())
                .param(CLASSIFICATION, post.getClassification().name())
                .param(TARGET_NAME, post.getTargetName())
                .param(TARGET_IMAGE_PATH, post.getTargetImagePath())
                .param(TARGET_ARTICLE_NAMES, post.getSerializedTargetArticleNames())
                .param(TARGET_ARTICLE_LINKS, post.getSerializedTargetArticleLinks());
    }

    default MockHttpServletRequestBuilder postWithBlogPostDto(
            String url, BlogPostDto postDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NAME, postDto.getName())
                .param(LINK, postDto.getLink())
                .param(YEAR, postDto.getYear().toString())
                .param(MONTH, postDto.getMonth().toString())
                .param(DAYS, postDto.getDays().toString())
                .param(CLASSIFICATION, postDto.getClassification())
                .param(TARGET_NAME, postDto.getTargetName())
                .param(TARGET_IMAGE_PATH, postDto.getTargetImagePath())
                .param(TARGET_ARTICLE_NAMES, postDto.getTargetArticleNames())
                .param(TARGET_ARTICLE_LINKS, postDto.getTargetArticleLinks());
    }
}
