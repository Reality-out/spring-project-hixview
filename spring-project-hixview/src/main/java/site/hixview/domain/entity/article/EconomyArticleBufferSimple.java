package site.hixview.domain.entity.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.util.JsonUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.lineSeparator;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_SECOND_CATEGORY;

public class EconomyArticleBufferSimple {

    private final StringBuffer nameDatePressBuffer;
    private final StringBuffer linkBuffer;

    @Getter private final Integer importance;
    @Getter private final String subjectCountry;
    @Getter private final String targetEconomyContents;

    private List<EconomyArticle> parsedArticles() {
        List<String> nameDatePressElement = List.of(this.nameDatePressBuffer.toString().split("\\R"));
        List<String> linkElement = List.of(linkBuffer.toString().split("\\R"));
        List<String> targetEconomyContents = JsonUtils.deserializeWithOneMapToList(new ObjectMapper(), SUBJECT_SECOND_CATEGORY, this.targetEconomyContents);

        ArrayList<EconomyArticle> articleList = new ArrayList<>();
        for (int i = 0; i < linkElement.size(); i++) {
            List<String> datePressElement = List.of(nameDatePressElement.get(2 * i + 1)
                    .replaceAll("^\\(|\\)$", "").split(",\\s|-"));

            articleList.add(EconomyArticle.builder()
                    .name(nameDatePressElement.get(2 * i)).press(Press.valueOf(datePressElement.getLast()))
                    .date(LocalDate.of(Integer.parseInt(datePressElement.getFirst()),
                            Integer.parseInt(datePressElement.get(1)), Integer.parseInt(datePressElement.get(2))))
                    .importance(importance)
                    .subjectCountry(Country.valueOf(subjectCountry))
                    .targetEconomyContents(targetEconomyContents).link(linkElement.get(i)).build());
        }
        return articleList;
    }

    public String getNameDatePressString() {
        return String.valueOf(nameDatePressBuffer);
    }

    public String getLinkString() {
        return String.valueOf(linkBuffer);
    }

    public static EconomyArticleBufferSimpleBuilder builder() {
        return new EconomyArticleBufferSimpleBuilder();
    }

    private EconomyArticleBufferSimple(StringBuffer nameDatePressBuffer, StringBuffer linkBuffer, Integer importance, String subjectCountry, String targetEconomyContents) {
        this.nameDatePressBuffer = nameDatePressBuffer;
        this.linkBuffer = linkBuffer;
        this.importance = importance;
        this.subjectCountry = subjectCountry;
        this.targetEconomyContents = targetEconomyContents;
    }

    public static final class EconomyArticleBufferSimpleBuilder {
        private StringBuffer nameDatePressBuffer;
        private StringBuffer linkBuffer;
        private Integer importance;
        private String subjectCountry;
        private String targetEconomyContents;

        public EconomyArticleBufferSimpleBuilder() {}

        public EconomyArticleBufferSimpleBuilder nameDatePressString(String nameDatePressString) {
            if (this.nameDatePressBuffer == null) {
                this.nameDatePressBuffer = new StringBuffer(nameDatePressString);
            } else {
                this.nameDatePressBuffer.append(lineSeparator()).append(nameDatePressString);
            }
            return this;
        }

        public EconomyArticleBufferSimpleBuilder linkString(String linkString) {
            if (this.linkBuffer == null) {
                this.linkBuffer = new StringBuffer(linkString);
            } else {
                this.linkBuffer.append(lineSeparator()).append(linkString);
            }
            return this;
        }

        public EconomyArticleBufferSimpleBuilder importance(Integer importance) {
            this.importance = importance;
            return this;
        }

        public EconomyArticleBufferSimpleBuilder subjectCountry(String subjectCountry) {
            this.subjectCountry = subjectCountry;
            return this;
        }

        public EconomyArticleBufferSimpleBuilder targetEconomyContents(String targetEconomyContents) {
            this.targetEconomyContents = targetEconomyContents;
            return this;
        }

        public EconomyArticleBufferSimpleBuilder article(EconomyArticle article) {
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
            subjectCountry = article.getSubjectCountry().name();
            targetEconomyContents = article.getSerializedTargetEconomyContents();
            return this;
        }

        public EconomyArticleBufferSimpleBuilder articles(EconomyArticle... articles) {
            for (EconomyArticle article : articles) {
                article(article);
            }
            return this;
        }

        public EconomyArticleBufferSimpleBuilder articleDto(EconomyArticleDto articleDto) {
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
            subjectCountry = articleDto.getSubjectCountry();
            targetEconomyContents = articleDto.getTargetEconomyContents();
            return this;
        }

        public EconomyArticleBufferSimpleBuilder articleBuffer(EconomyArticleBufferSimple articleBuffer) {
            if (nameDatePressBuffer == null) {
                nameDatePressBuffer = new StringBuffer(articleBuffer.getNameDatePressString());
                linkBuffer = new StringBuffer(articleBuffer.getLinkString());
            } else {
                nameDatePressBuffer.append(lineSeparator()).append(articleBuffer.getNameDatePressString());
                linkBuffer.append(lineSeparator()).append(articleBuffer.getLinkString());
            }
            importance = articleBuffer.getImportance();
            subjectCountry = articleBuffer.getSubjectCountry();
            targetEconomyContents = articleBuffer.getTargetEconomyContents();
            return this;
        }

        public EconomyArticleBufferSimple build() {
            return new EconomyArticleBufferSimple(this.nameDatePressBuffer, this.linkBuffer, this.importance,
                    this.subjectCountry, this.targetEconomyContents);
        }
    }
}
