package springsideproject1.springsideproject1build.domain.entity.article;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import springsideproject1.springsideproject1build.domain.validation.annotation.Importance;

import java.time.LocalDate;
import java.util.HashMap;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REGEX.URL_REGEX;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    protected final Long number;

    @NotBlank
    @Size(max = 80)
    protected final String name;

    @NotNull
    protected final Press press;

    @NotBlank
    @Size(max = 400)
    @Pattern(regexp = URL_REGEX)
    protected final String link;

    @NotNull
    @PastOrPresent
    protected final LocalDate date;

    @Importance
    protected final Integer importance;

    protected HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            put(NAME, name);
            put(PRESS, press);
            put(LINK, link);
            put(DATE, date);
            put(IMPORTANCE, importance);
        }};
    }

    protected static class ArticleBuilder {
        protected Long number;
        protected String name;
        protected Press press;
        protected String link;
        protected LocalDate date;
        protected Integer importance;
    }
}
