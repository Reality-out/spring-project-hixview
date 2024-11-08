package site.hixview.domain.entity.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.SubjectCountry;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.domain.entity.article.parent.Article;
import site.hixview.domain.validation.annotation.SubjectCountryConstraint;
import site.hixview.domain.validation.annotation.TargetEconomyContentsConstraint;
import site.hixview.util.JsonUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static site.hixview.domain.vo.Word.*;
import static site.hixview.util.JsonUtils.serializeWithOneMap;

@Getter
@Builder(access = AccessLevel.PUBLIC)
public class EconomyArticle extends Article<EconomyArticle> {

    @SubjectCountryConstraint
    private final SubjectCountry subjectCountry;

    @TargetEconomyContentsConstraint
    private final List<String> targetEconomyContents;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getSerializedTargetEconomyContents() {
        return serializeWithOneMap(objectMapper, TARGET_ECONOMY_CONTENT, targetEconomyContents);
    }

    @SneakyThrows
    public EconomyArticleDto toDto() {
        EconomyArticleDto economyArticleDto = new EconomyArticleDto();
        economyArticleDto.setName(name);
        economyArticleDto.setPress(press.name());
        economyArticleDto.setLink(link);
        economyArticleDto.setYear(date.getYear());
        economyArticleDto.setMonth(date.getMonthValue());
        economyArticleDto.setDays(date.getDayOfMonth());
        economyArticleDto.setImportance(importance);
        economyArticleDto.setSubjectCountry(subjectCountry.name());
        economyArticleDto.setTargetEconomyContents(getSerializedTargetEconomyContents());
        return economyArticleDto;
    }

    @Override
    public HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            putAll(EconomyArticle.super.toMapWithNoNumber());
            put(SUBJECT_COUNTRY, subjectCountry);
            put(TARGET_ECONOMY_CONTENTS, targetEconomyContents);
        }};
    }

    public HashMap<String, Object> toSerializedMapWithNoNumber() {
        return new HashMap<>() {{
            putAll(EconomyArticle.super.toMapWithNoNumber());
            put(SUBJECT_COUNTRY, subjectCountry);
            put(TARGET_ECONOMY_CONTENTS, getSerializedTargetEconomyContents());
        }};
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EconomyArticle economyArticle = (EconomyArticle) obj;
        return new EqualsBuilder()
                .append(number, economyArticle.number)
                .append(name, economyArticle.name)
                .append(press, economyArticle.press)
                .append(link, economyArticle.link)
                .append(date, economyArticle.date)
                .append(importance, economyArticle.importance)
                .append(subjectCountry, economyArticle.subjectCountry)
                .append(targetEconomyContents, economyArticle.targetEconomyContents)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(subjectCountry).append(targetEconomyContents).toHashCode();
    }

    private EconomyArticle(Long number, String name, Press press, String link, LocalDate date,
                           Integer importance, SubjectCountry subjectCountry, List<String> targetEconomyContents) {
        super(number, name, press, link, date, importance);
        this.subjectCountry = subjectCountry;
        this.targetEconomyContents = targetEconomyContents;
    }

    public static final class EconomyArticleBuilder extends Article.ArticleBuilder {
        private SubjectCountry subjectCountry;
        private List<String> targetEconomyContents;

        public EconomyArticleBuilder() {}

        public EconomyArticle.EconomyArticleBuilder number(final Long number) {
            this.number = number;
            return this;
        }

        public EconomyArticle.EconomyArticleBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public EconomyArticle.EconomyArticleBuilder press(final Press press) {
            this.press = press;
            return this;
        }

        public EconomyArticle.EconomyArticleBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public EconomyArticle.EconomyArticleBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public EconomyArticle.EconomyArticleBuilder importance(final Integer importance) {
            this.importance = importance;
            return this;
        }

        public EconomyArticle.EconomyArticleBuilder targetEconomyContents(final List<String> targetEconomyArticles) {
            this.targetEconomyContents = targetEconomyArticles;
            return this;
        }

        public EconomyArticle.EconomyArticleBuilder targetEconomyContents(final String... targetEconomyArticles) {
            this.targetEconomyContents = List.of(targetEconomyArticles);
            return this;
        }

        public EconomyArticle.EconomyArticleBuilder targetEconomyContents(final String targetEconomyArticles) {
            this.targetEconomyContents = List.of(targetEconomyArticles);
            return this;
        }

        public EconomyArticle.EconomyArticleBuilder article(EconomyArticle article) {
            number = article.getNumber();
            name = article.getName();
            press = article.getPress();
            link = article.getLink();
            date = article.getDate();
            importance = article.getImportance();
            subjectCountry = article.getSubjectCountry();
            targetEconomyContents = article.getTargetEconomyContents();
            return this;
        }

        public EconomyArticle.EconomyArticleBuilder articleDto(EconomyArticleDto articleDto) {
            name = articleDto.getName();
            press = Press.valueOf(articleDto.getPress());
            link = articleDto.getLink();
            date = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDays());
            importance = articleDto.getImportance();
            subjectCountry = SubjectCountry.valueOf(articleDto.getSubjectCountry());
            targetEconomyContents = JsonUtils.deserializeWithOneMapToList(new ObjectMapper(), TARGET_ECONOMY_CONTENT,
                    articleDto.getTargetEconomyContents());
            return this;
        }

        public EconomyArticle build() {
            return new EconomyArticle(this.number, this.name, this.press, this.link,
                    this.date, this.importance, this.subjectCountry, this.targetEconomyContents);
        }
    }
}
