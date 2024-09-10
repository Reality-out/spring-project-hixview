package springsideproject1.springsideproject1build.domain.entity.article.company;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanyArticleMain {

    private final Long number;

    @NotBlank
    private final String name;

    @NotBlank
    private final String imagePath;

    @NotBlank
    private final String summary;

    public CompanyArticleMainDto toDto() {
        CompanyArticleMainDto companyArticleDto = new CompanyArticleMainDto();
        companyArticleDto.setName(name);
        companyArticleDto.setImagePath(imagePath);
        companyArticleDto.setSummary(summary);
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
        }};
    }

    public static class CompanyArticleMainBuilder {
        public CompanyArticleMainBuilder() {}

        public CompanyArticleMain.CompanyArticleMainBuilder article(CompanyArticleMain article) {
            number = article.getNumber();
            name = article.getName();
            imagePath = article.getImagePath();
            summary = article.getSummary();
            return this;
        }

        public CompanyArticleMain.CompanyArticleMainBuilder articleDto(CompanyArticleMainDto articleDto) {
            name = articleDto.getName();
            imagePath = articleDto.getImagePath();
            summary = articleDto.getSummary();
            return this;
        }
    }
}
