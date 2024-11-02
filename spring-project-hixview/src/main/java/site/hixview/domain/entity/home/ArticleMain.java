package site.hixview.domain.entity.home;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.home.dto.ArticleMainDto;
import site.hixview.domain.validation.annotation.ClassificationConstraint;
import site.hixview.domain.validation.annotation.article.ArticleImagePath;
import site.hixview.domain.validation.annotation.article.ArticleName;
import site.hixview.domain.validation.annotation.article.ArticleSummary;

import java.util.HashMap;

import static site.hixview.domain.vo.Word.*;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleMain {

    private final Long number;

    @ArticleName
    private final String name;

    @ArticleImagePath
    private final String imagePath;

    @ArticleSummary
    private final String summary;

    @ClassificationConstraint
    private final Classification classification;

    public ArticleMainDto toDto() {
        ArticleMainDto companyArticleDto = new ArticleMainDto();
        companyArticleDto.setName(name);
        companyArticleDto.setImagePath(imagePath);
        companyArticleDto.setSummary(summary);
        companyArticleDto.setClassification(classification.name());
        return companyArticleDto;
    }

    public HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            put(NAME, name);
            put(IMAGE_PATH, imagePath);
            put(SUMMARY, summary);
            put(CLASSIFICATION, classification);
        }};
    }

    public static final class ArticleMainBuilder {
        public ArticleMainBuilder() {}

        public ArticleMainBuilder article(ArticleMain article) {
            number = article.getNumber();
            name = article.getName();
            imagePath = article.getImagePath();
            summary = article.getSummary();
            classification = article.getClassification();
            return this;
        }

        public ArticleMainBuilder articleDto(ArticleMainDto articleDto) {
            name = articleDto.getName();
            imagePath = articleDto.getImagePath();
            summary = articleDto.getSummary();
            classification = Classification.valueOf(articleDto.getClassification());
            return this;
        }
    }
}
