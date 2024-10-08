package site.hixview.domain.entity.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.lineSeparator;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_SECOND_CATEGORY;
import static site.hixview.util.JsonUtils.deserializeWithOneMapToList;

public class IndustryArticleBufferSimple {

    private final StringBuffer nameDatePressBuffer;
    private final StringBuffer linkBuffer;

    @Getter private final Integer importance;
    @Getter private final String subjectFirstCategory;
    @Getter private final String subjectSecondCategories;

    private List<IndustryArticle> parsedArticles() {
        List<String> nameDatePressElement = List.of(this.nameDatePressBuffer.toString().split("\\R"));
        List<String> linkElement = List.of(linkBuffer.toString().split("\\R"));
        List<SecondCategory> subjectSecondCategories = deserializeWithOneMapToList(new ObjectMapper(), SUBJECT_SECOND_CATEGORY, this.subjectSecondCategories, SecondCategory.class);

        ArrayList<IndustryArticle> articleList = new ArrayList<>();
        for (int i = 0; i < linkElement.size(); i++) {
            List<String> datePressElement = List.of(nameDatePressElement.get(2 * i + 1)
                    .replaceAll("^\\(|\\)$", "").split(",\\s|-"));

            articleList.add(IndustryArticle.builder()
                    .name(nameDatePressElement.get(2 * i)).press(Press.valueOf(datePressElement.getLast()))
                    .date(LocalDate.of(Integer.parseInt(datePressElement.getFirst()),
                            Integer.parseInt(datePressElement.get(1)), Integer.parseInt(datePressElement.get(2))))
                    .importance(importance)
                    .subjectFirstCategory(FirstCategory.valueOf(subjectFirstCategory))
                    .subjectSecondCategories(subjectSecondCategories).link(linkElement.get(i)).build());
        }
        return articleList;
    }

    public String getNameDatePressString() {
        return String.valueOf(nameDatePressBuffer);
    }

    public String getLinkString() {
        return String.valueOf(linkBuffer);
    }

    public static IndustryArticleBufferSimpleBuilder builder() {
        return new IndustryArticleBufferSimpleBuilder();
    }

    private IndustryArticleBufferSimple(StringBuffer nameDatePressBuffer, StringBuffer linkBuffer, Integer importance, String subjectFirstCategory, String subjectSecondCategories) {
        this.nameDatePressBuffer = nameDatePressBuffer;
        this.linkBuffer = linkBuffer;
        this.importance = importance;
        this.subjectFirstCategory = subjectFirstCategory;
        this.subjectSecondCategories = subjectSecondCategories;
    }

    public static final class IndustryArticleBufferSimpleBuilder {
        private StringBuffer nameDatePressBuffer;
        private StringBuffer linkBuffer;
        private Integer importance;
        private String subjectFirstCategory;
        private String subjectSecondCategories;

        public IndustryArticleBufferSimpleBuilder() {}

        public IndustryArticleBufferSimpleBuilder nameDatePressString(String nameDatePressString) {
            if (this.nameDatePressBuffer == null) {
                this.nameDatePressBuffer = new StringBuffer(nameDatePressString);
            } else {
                this.nameDatePressBuffer.append(lineSeparator()).append(nameDatePressString);
            }
            return this;
        }

        public IndustryArticleBufferSimpleBuilder linkString(String linkString) {
            if (this.linkBuffer == null) {
                this.linkBuffer = new StringBuffer(linkString);
            } else {
                this.linkBuffer.append(lineSeparator()).append(linkString);
            }
            return this;
        }

        public IndustryArticleBufferSimpleBuilder importance(Integer importance) {
            this.importance = importance;
            return this;
        }

        public IndustryArticleBufferSimpleBuilder subjectFirstCategory(String subjectFirstCategory) {
            this.subjectFirstCategory = subjectFirstCategory;
            return this;
        }

        public IndustryArticleBufferSimpleBuilder subjectSecondCategories(String subjectSecondCategories) {
            this.subjectSecondCategories = subjectSecondCategories;
            return this;
        }

        public IndustryArticleBufferSimpleBuilder article(IndustryArticle article) {
            String concatenatedNameDatePress = article.getName() + lineSeparator() +
                    "(" + article.getDate().getYear() + "-" + article.getDate().getMonthValue() + "-" +
                    article.getDate().getDayOfMonth() + ", " + article.getPress() + ")";
            if (nameDatePressBuffer == null) {
                nameDatePressBuffer = new StringBuffer(concatenatedNameDatePress);
                linkBuffer = new StringBuffer(article.getLink());
            } else {
                nameDatePressBuffer.append(lineSeparator()).append(concatenatedNameDatePress);
                linkBuffer.append(lineSeparator()).append(article.getLink());
            }
            importance = article.getImportance();
            subjectFirstCategory = article.getSubjectFirstCategory().name();
            subjectSecondCategories = article.getSerializedSubjectSecondCategories();
            return this;
        }

        public IndustryArticleBufferSimpleBuilder articles(IndustryArticle... articles) {
            for (IndustryArticle article : articles) {
                article(article);
            }
            return this;
        }

        public IndustryArticleBufferSimpleBuilder articleDto(IndustryArticleDto articleDto) {
            String concatenatedNameDatePress = articleDto.getName() + lineSeparator() +
                    "(" + articleDto.getYear() + "-" + articleDto.getMonth() + "-" + articleDto.getDays() + ", " + articleDto.getPress() + ")";
            if (nameDatePressBuffer == null) {
                nameDatePressBuffer = new StringBuffer(concatenatedNameDatePress);
                linkBuffer = new StringBuffer(articleDto.getLink());
            } else {
                nameDatePressBuffer.append(lineSeparator()).append(concatenatedNameDatePress);
                linkBuffer.append(lineSeparator()).append(articleDto.getLink());
            }
            importance = articleDto.getImportance();
            subjectFirstCategory = articleDto.getSubjectFirstCategory();
            subjectSecondCategories = articleDto.getSubjectSecondCategories();
            return this;
        }

        public IndustryArticleBufferSimpleBuilder articleBuffer(IndustryArticleBufferSimple articleBuffer) {
            if (nameDatePressBuffer == null) {
                nameDatePressBuffer = new StringBuffer(articleBuffer.getNameDatePressString());
                linkBuffer = new StringBuffer(articleBuffer.getLinkString());
            } else {
                nameDatePressBuffer.append(lineSeparator()).append(articleBuffer.getNameDatePressString());
                linkBuffer.append(lineSeparator()).append(articleBuffer.getLinkString());
            }
            importance = articleBuffer.getImportance();
            subjectFirstCategory = articleBuffer.getSubjectFirstCategory();
            subjectSecondCategories = articleBuffer.getSubjectSecondCategories();
            return this;
        }

        public IndustryArticleBufferSimple build() {
            return new IndustryArticleBufferSimple(this.nameDatePressBuffer, this.linkBuffer, this.importance,
                    this.subjectFirstCategory, this.subjectSecondCategories);
        }
    }
}
