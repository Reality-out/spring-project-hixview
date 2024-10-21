package site.hixview.domain.entity.article;

import lombok.Getter;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;
import site.hixview.domain.entity.article.parent.ArticleBufferSimple;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.lineSeparator;

@Getter
public class CompanyArticleBufferSimple extends ArticleBufferSimple {

    private final String subjectCompany;

    @Override
    @SuppressWarnings("unchecked")
    public List<CompanyArticle> parsedArticles() {
        List<String> parsedNameList = getParsedNameList();
        List<List<String>> parsedDatePressList = getParsedDatePressList();
        List<String> linkList = getLinkList();
        ArrayList<CompanyArticle> articleList = new ArrayList<>();
        for (int i = 0; i < getLength(); i++) {
            List<String> datePressElement = parsedDatePressList.get(i);
            articleList.add(CompanyArticle.builder()
                    .name(parsedNameList.get(i)).press(Press.valueOf(datePressElement.getLast()))
                    .date(LocalDate.of(Integer.parseInt(datePressElement.getFirst()),
                            Integer.parseInt(datePressElement.get(1)), Integer.parseInt(datePressElement.get(2))))
                    .importance(importance)
                    .subjectCompany(subjectCompany).link(linkList.get(i)).build());
        }
        return articleList;
    }

    public static CompanyArticleBufferSimpleBuilder builder() {
        return new CompanyArticleBufferSimpleBuilder();
    }

    private CompanyArticleBufferSimple(StringBuffer nameDatePressBuffer, StringBuffer linkBuffer, Integer importance, String subjectCompany) {
        super(nameDatePressBuffer, linkBuffer, importance);
        this.subjectCompany = subjectCompany;
    }

    public static final class CompanyArticleBufferSimpleBuilder extends ArticleBufferSimpleBuilder {
        private String subjectCompany;

        public CompanyArticleBufferSimpleBuilder() {}

        public CompanyArticleBufferSimpleBuilder nameDatePressString(String nameDatePressString) {
            if (this.nameDatePressBuffer == null) {
                this.nameDatePressBuffer = new StringBuffer(nameDatePressString);
            } else {
                this.nameDatePressBuffer.append(lineSeparator()).append(nameDatePressString);
            }
            return this;
        }

        public CompanyArticleBufferSimpleBuilder linkString(String linkString) {
            if (this.linkBuffer == null) {
                this.linkBuffer = new StringBuffer(linkString);
            } else {
                this.linkBuffer.append(lineSeparator()).append(linkString);
            }
            return this;
        }

        public CompanyArticleBufferSimpleBuilder importance(Integer importance) {
            this.importance = importance;
            return this;
        }

        public CompanyArticleBufferSimpleBuilder subjectCompany(String subjectCompany) {
            this.subjectCompany = subjectCompany;
            return this;
        }

        public CompanyArticleBufferSimpleBuilder article(CompanyArticle article) {
            StringBuffer concatenatedNameDatePress = article.getConcatenatedNameDatePress();
            if (nameDatePressBuffer == null) {
                nameDatePressBuffer = concatenatedNameDatePress;
                linkBuffer = new StringBuffer(article.getLink());
            } else {
                nameDatePressBuffer.append(lineSeparator()).append(concatenatedNameDatePress);
                linkBuffer.append(lineSeparator()).append(article.getLink());
            }
            importance = article.getImportance();
            subjectCompany = article.getSubjectCompany();
            return this;
        }

        public CompanyArticleBufferSimpleBuilder articles(CompanyArticle... articles) {
            for (CompanyArticle article : articles) {
                article(article);
            }
            return this;
        }

        public CompanyArticleBufferSimpleBuilder articleDto(CompanyArticleDto articleDto) {
            StringBuffer concatenatedNameDatePress = CompanyArticle.builder().articleDto(articleDto).build().getConcatenatedNameDatePress();
            if (nameDatePressBuffer == null) {
                nameDatePressBuffer = new StringBuffer(concatenatedNameDatePress);
                linkBuffer = new StringBuffer(articleDto.getLink());
            } else {
                nameDatePressBuffer.append(lineSeparator()).append(concatenatedNameDatePress);
                linkBuffer.append(lineSeparator()).append(articleDto.getLink());
            }
            importance = articleDto.getImportance();
            subjectCompany = articleDto.getSubjectCompany();
            return this;
        }

        public CompanyArticleBufferSimpleBuilder articleBuffer(CompanyArticleBufferSimple articleBuffer) {
            if (nameDatePressBuffer == null) {
                nameDatePressBuffer = new StringBuffer(articleBuffer.getNameDatePressString());
                linkBuffer = new StringBuffer(articleBuffer.getLinkString());
            } else {
                nameDatePressBuffer.append(lineSeparator()).append(articleBuffer.getNameDatePressString());
                linkBuffer.append(lineSeparator()).append(articleBuffer.getLinkString());
            }
            importance = articleBuffer.getImportance();
            subjectCompany = articleBuffer.getSubjectCompany();
            return this;
        }

        public CompanyArticleBufferSimple build() {
            return new CompanyArticleBufferSimple(this.nameDatePressBuffer, this.linkBuffer, this.importance, this.subjectCompany);
        }
    }
}
