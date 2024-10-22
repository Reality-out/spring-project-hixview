package site.hixview.domain.entity.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.domain.entity.article.parent.ArticleBufferSimple;
import site.hixview.util.JsonUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.lineSeparator;
import static site.hixview.domain.vo.Word.TARGET_ECONOMY_CONTENT;

@Getter
public class EconomyArticleBufferSimple extends ArticleBufferSimple {

    private final String subjectCountry;
    private final String targetEconomyContents;

    @Override
    @SuppressWarnings("unchecked")
    public List<EconomyArticle> getParsedArticles() {
        List<String> parsedNameList = getParsedNameList();
        List<List<String>> parsedDatePressList = getParsedDatePressList();
        List<String> linkList = getLinkList();
        List<String> targetEconomyContents = JsonUtils.deserializeWithOneMapToList(new ObjectMapper(), TARGET_ECONOMY_CONTENT, this.targetEconomyContents);
        ArrayList<EconomyArticle> articleList = new ArrayList<>();
        Country subjectCountry = Country.valueOf(this.subjectCountry);
        for (int i = 0; i < getLength(); i++) {
            List<String> datePressElement = parsedDatePressList.get(i);
            articleList.add(EconomyArticle.builder()
                    .name(parsedNameList.get(i)).press(Press.valueOf(datePressElement.getLast()))
                    .date(LocalDate.of(Integer.parseInt(datePressElement.getFirst()),
                            Integer.parseInt(datePressElement.get(1)), Integer.parseInt(datePressElement.get(2))))
                    .importance(importance)
                    .subjectCountry(subjectCountry)
                    .targetEconomyContents(targetEconomyContents).link(linkList.get(i)).build());
        }
        return articleList;
    }

    public static EconomyArticleBufferSimpleBuilder builder() {
        return new EconomyArticleBufferSimpleBuilder();
    }

    private EconomyArticleBufferSimple(StringBuffer nameDatePressBuffer, StringBuffer linkBuffer, Integer importance, String subjectCountry, String targetEconomyContents) {
        super(nameDatePressBuffer, linkBuffer, importance);
        this.subjectCountry = subjectCountry;
        this.targetEconomyContents = targetEconomyContents;
    }

    public static final class EconomyArticleBufferSimpleBuilder extends ArticleBufferSimpleBuilder {
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
            StringBuffer concatenatedNameDatePress = article.getConcatenatedNameDatePress();
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
            StringBuffer concatenatedNameDatePress = EconomyArticle.builder().articleDto(articleDto).build().getConcatenatedNameDatePress();
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
