package site.hixview.domain.entity.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;
import site.hixview.domain.entity.article.parent.Article;
import site.hixview.domain.validation.annotation.SubjectFirstCategoryConstraint;
import site.hixview.domain.validation.annotation.SubjectSecondCategoriesConstraint;
import site.hixview.util.JsonUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static site.hixview.domain.vo.Word.*;
import static site.hixview.util.JsonUtils.serializeEnumWithOneMap;

@Getter
@Builder(access = AccessLevel.PUBLIC)
public class IndustryArticle extends Article<IndustryArticle> {

    @SubjectFirstCategoryConstraint
    private final FirstCategory subjectFirstCategory;

    @SubjectSecondCategoriesConstraint
    private final List<SecondCategory> subjectSecondCategories;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getSerializedSubjectSecondCategories() {
        return serializeEnumWithOneMap(objectMapper, SUBJECT_SECOND_CATEGORY, subjectSecondCategories);
    }

    @SneakyThrows
    public IndustryArticleDto toDto() {
        IndustryArticleDto IndustryArticleDto = new IndustryArticleDto();
        IndustryArticleDto.setName(name);
        IndustryArticleDto.setPress(press.name());
        IndustryArticleDto.setLink(link);
        IndustryArticleDto.setYear(date.getYear());
        IndustryArticleDto.setMonth(date.getMonthValue());
        IndustryArticleDto.setDays(date.getDayOfMonth());
        IndustryArticleDto.setImportance(importance);
        IndustryArticleDto.setSubjectFirstCategory(subjectFirstCategory.name());
        IndustryArticleDto.setSubjectSecondCategories(getSerializedSubjectSecondCategories());
        return IndustryArticleDto;
    }

    @Override
    public HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            putAll(IndustryArticle.super.toMapWithNoNumber());
            put(SUBJECT_FIRST_CATEGORY, subjectFirstCategory);
            put(SUBJECT_SECOND_CATEGORIES, subjectSecondCategories);
        }};
    }

    public HashMap<String, Object> toSerializedMapWithNoNumber() {
        return new HashMap<>() {{
            putAll(IndustryArticle.super.toMapWithNoNumber());
            put(SUBJECT_FIRST_CATEGORY, subjectFirstCategory);
            put(SUBJECT_SECOND_CATEGORIES, getSerializedSubjectSecondCategories());
        }};
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IndustryArticle industryArticle = (IndustryArticle) obj;
        return new EqualsBuilder()
                .append(number, industryArticle.number)
                .append(name, industryArticle.name)
                .append(press, industryArticle.press)
                .append(link, industryArticle.link)
                .append(date, industryArticle.date)
                .append(importance, industryArticle.importance)
                .append(subjectFirstCategory, industryArticle.subjectFirstCategory)
                .append(subjectSecondCategories, industryArticle.subjectSecondCategories)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(subjectFirstCategory).append(subjectSecondCategories).toHashCode();
    }

    private IndustryArticle(Long number, String name, Press press, String link, LocalDate date,
                            Integer importance, FirstCategory subjectFirstCategory, List<SecondCategory> subjectSecondCategories) {
        super(number, name, press, link, date, importance);
        this.subjectFirstCategory = subjectFirstCategory;
        this.subjectSecondCategories = subjectSecondCategories;
    }

    public static final class IndustryArticleBuilder extends ArticleBuilder {
        private FirstCategory subjectFirstCategory;
        private List<SecondCategory> subjectSecondCategories;

        public IndustryArticleBuilder() {}

        public IndustryArticleBuilder number(final Long number) {
            this.number = number;
            return this;
        }

        public IndustryArticleBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public IndustryArticleBuilder press(final Press press) {
            this.press = press;
            return this;
        }

        public IndustryArticleBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public IndustryArticleBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public IndustryArticleBuilder importance(final Integer importance) {
            this.importance = importance;
            return this;
        }

        public IndustryArticleBuilder subjectSecondCategories(final List<SecondCategory> subjectSecondCategories) {
            this.subjectSecondCategories = subjectSecondCategories;
            return this;
        }

        public IndustryArticleBuilder subjectSecondCategories(final SecondCategory... subjectSecondCategories) {
            this.subjectSecondCategories = List.of(subjectSecondCategories);
            return this;
        }

        public IndustryArticleBuilder subjectSecondCategories(final SecondCategory subjectSecondCategory) {
            this.subjectSecondCategories = List.of(subjectSecondCategory);
            return this;
        }

        public IndustryArticleBuilder article(IndustryArticle article) {
            number = article.getNumber();
            name = article.getName();
            press = article.getPress();
            link = article.getLink();
            date = article.getDate();
            importance = article.getImportance();
            subjectFirstCategory = article.getSubjectFirstCategory();
            subjectSecondCategories = article.getSubjectSecondCategories();
            return this;
        }

        public IndustryArticleBuilder articleDto(IndustryArticleDto articleDto) {
            name = articleDto.getName();
            press = Press.valueOf(articleDto.getPress());
            link = articleDto.getLink();
            date = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDays());
            importance = articleDto.getImportance();
            subjectFirstCategory = FirstCategory.valueOf(articleDto.getSubjectFirstCategory());
            subjectSecondCategories = JsonUtils.deserializeEnumWithOneMapToList(new ObjectMapper(), SUBJECT_SECOND_CATEGORY,
                    articleDto.getSubjectSecondCategories(), SecondCategory.class);
            return this;
        }

        public IndustryArticle build() {
            return new IndustryArticle(this.number, this.name, this.press, this.link,
                    this.date, this.importance, this.subjectFirstCategory, this.subjectSecondCategories);
        }
    }
}
