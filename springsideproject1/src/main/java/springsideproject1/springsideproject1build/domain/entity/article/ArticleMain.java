package springsideproject1.springsideproject1build.domain.entity.article;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import springsideproject1.springsideproject1build.domain.validation.annotation.ArticleClassName;

import java.util.HashMap;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleMain {

    private final Long number;

    @NotBlank
    private final String name;

    @NotBlank
    private final String imagePath;

    @NotBlank
    private final String summary;

    @ArticleClassName
    private final String articleClassName;

    public ArticleMainDto toDto() {
        ArticleMainDto companyArticleDto = new ArticleMainDto();
        companyArticleDto.setName(name);
        companyArticleDto.setImagePath(imagePath);
        companyArticleDto.setSummary(summary);
        companyArticleDto.setArticleClassName(articleClassName);
        return companyArticleDto;
    }

    public HashMap<String, Object> toMap() {
        return new HashMap<>() {{
            put(NUMBER, number);
            putAll(toMapWithNoNumber());
        }};
    }

    public HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            put(NAME, name);
            put(IMAGE_PATH, imagePath);
            put(SUMMARY, summary);
            put(ARTICLE_CLASS_NAME, articleClassName);
        }};
    }

    public static class ArticleMainBuilder {
        public ArticleMainBuilder() {}

        public ArticleMainBuilder article(ArticleMain article) {
            number = article.getNumber();
            name = article.getName();
            imagePath = article.getImagePath();
            summary = article.getSummary();
            articleClassName = article.getArticleClassName();
            return this;
        }

        public ArticleMainBuilder articleDto(ArticleMainDto articleDto) {
            name = articleDto.getName();
            imagePath = articleDto.getImagePath();
            summary = articleDto.getSummary();
            articleClassName = articleDto.getArticleClassName();
            return this;
        }
    }
}
