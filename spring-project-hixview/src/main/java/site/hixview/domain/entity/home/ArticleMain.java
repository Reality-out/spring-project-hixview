package site.hixview.domain.entity.home;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.home.dto.ArticleMainDto;

import java.util.HashMap;

import static site.hixview.domain.vo.Word.*;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleMain {

    private final Long number;

    @NotBlank(message = "{NotBlank.article.name}")
    private final String name;

    @NotBlank(message = "{NotBlank.article.imagePath}")
    @Size(max = 80, message = "{Size.article.imagePath}")
    private final String imagePath;

    @NotBlank(message = "{NotBlank.article.summary}")
    @Size(max = 36, message = "{Size.article.summary}")
    private final String summary;

    @NotNull
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
