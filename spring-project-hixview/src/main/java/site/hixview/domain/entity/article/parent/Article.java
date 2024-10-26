package site.hixview.domain.entity.article.parent;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.hixview.domain.entity.Press;
import site.hixview.domain.validation.annotation.ImportanceConstraint;

import java.time.LocalDate;
import java.util.HashMap;

import static java.lang.System.lineSeparator;
import static site.hixview.domain.vo.Regex.URL_REGEX;
import static site.hixview.domain.vo.Word.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Article<E extends Article<E>> {
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

    @ImportanceConstraint
    protected final Integer importance;

    public static String[] getFieldNamesWithNoNumber() {
        return new String[]{NAME, PRESS, LINK, DATE, IMPORTANCE};
    }

    public StringBuffer getConcatenatedNameDatePress() {
        return new StringBuffer(name + lineSeparator() + "(" + date.getYear() + "-" + date.getMonthValue() + "-" +
                date.getDayOfMonth() + ", " + press + ")");
    }

    protected HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            put(NAME, name);
            put(PRESS, press);
            put(LINK, link);
            put(DATE, date);
            put(IMPORTANCE, importance);
        }};
    }

    protected abstract static class ArticleBuilder {
        protected Long number;
        protected String name;
        protected Press press;
        protected String link;
        protected LocalDate date;
        protected Integer importance;

        protected abstract ArticleBuilder number(final Long number);

        protected abstract ArticleBuilder name(final String name);

        protected abstract ArticleBuilder press(final Press press);

        protected abstract ArticleBuilder link(final String link);

        protected abstract ArticleBuilder date(final LocalDate date);

        protected abstract ArticleBuilder importance(final Integer importance);
    }
}