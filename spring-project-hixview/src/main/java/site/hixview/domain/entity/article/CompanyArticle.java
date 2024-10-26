package site.hixview.domain.entity.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;
import site.hixview.domain.entity.article.parent.Article;

import java.time.LocalDate;
import java.util.HashMap;

import static site.hixview.domain.vo.Word.SUBJECT_COMPANY;

@Getter
@Builder(access = AccessLevel.PUBLIC)
public class CompanyArticle extends Article<CompanyArticle> {

    @NotBlank(message = "{NotBlank.article.subjectCompany}")
    @Size(max = 12, message = "{Size.article.subjectCompany}")
    private final String subjectCompany;

    public CompanyArticleDto toDto() {
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        companyArticleDto.setName(name);
        companyArticleDto.setPress(press.name());
        companyArticleDto.setLink(link);
        companyArticleDto.setYear(date.getYear());
        companyArticleDto.setMonth(date.getMonthValue());
        companyArticleDto.setDays(date.getDayOfMonth());
        companyArticleDto.setImportance(importance);
        companyArticleDto.setSubjectCompany(subjectCompany);
        return companyArticleDto;
    }

    @Override
    public HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            putAll(CompanyArticle.super.toMapWithNoNumber());
            put(SUBJECT_COMPANY, subjectCompany);
        }};
    }

    public static String[] getFieldNamesWithNoNumber() {
        String[] superArr = Article.getFieldNamesWithNoNumber();
        String[] arr = {SUBJECT_COMPANY};
        String[] combinedArr = new String[superArr.length + arr.length];
        System.arraycopy(superArr, 0, combinedArr, 0, superArr.length);
        System.arraycopy(arr, 0, combinedArr, superArr.length, arr.length);
        return combinedArr;
    }

    private CompanyArticle(final Long number, final String name, final Press press, final String link,
                           final LocalDate date, final Integer importance, final String subjectCompany) {
        super(number, name, press, link, date, importance);
        this.subjectCompany = subjectCompany;
    }

    public static final class CompanyArticleBuilder extends ArticleBuilder {

        public CompanyArticleBuilder() {}

        public CompanyArticleBuilder number(final Long number) {
            this.number = number;
            return this;
        }

        public CompanyArticleBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public CompanyArticleBuilder press(final Press press) {
            this.press = press;
            return this;
        }

        public CompanyArticleBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public CompanyArticleBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public CompanyArticleBuilder importance(final Integer importance) {
            this.importance = importance;
            return this;
        }

        public CompanyArticleBuilder article(CompanyArticle article) {
            number = article.getNumber();
            name = article.getName();
            press = article.getPress();
            link = article.getLink();
            date = article.getDate();
            importance = article.getImportance();
            subjectCompany = article.getSubjectCompany();
            return this;
        }

        public CompanyArticleBuilder articleDto(CompanyArticleDto articleDto) {
            name = articleDto.getName();
            press = Press.valueOf(articleDto.getPress());
            link = articleDto.getLink();
            date = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDays());
            importance = articleDto.getImportance();
            subjectCompany = articleDto.getSubjectCompany();
            return this;
        }

        public CompanyArticle build() {
            return new CompanyArticle(this.number, this.name, this.press, this.link,
                    this.date, this.importance, this.subjectCompany);
        }
    }
}
