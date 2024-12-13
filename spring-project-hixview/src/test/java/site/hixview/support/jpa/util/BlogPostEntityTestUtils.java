package site.hixview.support.jpa.util;

import site.hixview.aggregate.enums.Classification;
import site.hixview.jpa.entity.BlogPostEntity;

import java.time.LocalDate;

public interface BlogPostEntityTestUtils extends PostEntityTestUtils {
    /**
     * Create
     */
    default BlogPostEntity createBlogPostEntity() {
        return BlogPostEntity.builder()
                .post(createPostEntity())
                .name("이노와이어리스 투자 포인트 재확인")
                .link("https://blog.naver.com/akdnjs0308/223491404394")
                .date(LocalDate.of(2024, 6, 26))
                .classification(Classification.COMPANY.name())
                .build();
    }

    default BlogPostEntity createAnotherBlogPostEntity() {
        return BlogPostEntity.builder()
                .post(createPostEntity())
                .name("중국 경제 진단(중국은 어떤 처지에 놓여 있을까)")
                .link("https://blog.naver.com/akdnjs0308/223527440189")
                .date(LocalDate.of(2024, 7, 27))
                .classification(Classification.ECONOMY.name())
                .build();
    }
}
