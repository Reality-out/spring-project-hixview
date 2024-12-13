package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.enums.Classification;

import java.time.LocalDate;
import java.util.List;

public interface BlogPostTestUtils {
    BlogPost blogPost = BlogPost.builder()
            .number(1L)
            .name("이노와이어리스 투자 포인트 재확인")
            .link("https://blog.naver.com/akdnjs0308/223491404394")
            .date(LocalDate.of(2024, 6, 26))
            .classification(Classification.COMPANY)
            .mappedArticleNumbers(List.of(1L, 2L))
            .build();

    BlogPost anotherBlogPost = BlogPost.builder()
            .number(2L)
            .name("중국 경제 진단(중국은 어떤 처지에 놓여 있을까)")
            .link("https://blog.naver.com/akdnjs0308/223527440189")
            .date(LocalDate.of(2024, 7, 27))
            .classification(Classification.ECONOMY)
            .mappedArticleNumbers(List.of(3L, 4L))
            .build();
}
