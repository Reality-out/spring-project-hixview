package site.hixview.domain.entity.article;

import lombok.Getter;
import site.hixview.domain.entity.Press;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.lineSeparator;

public class CompanyArticleBufferSimple {

    private final StringBuffer nameDatePressBuffer;
    private final StringBuffer linkBuffer;

    @Getter private final Integer importance;
    @Getter private final String subjectCompany;

    private List<CompanyArticle> parsedArticles() {
        List<String> nameDatePressElement = List.of(this.nameDatePressBuffer.toString().split("\\R"));
        List<String> linkElement = List.of(linkBuffer.toString().split("\\R"));

        ArrayList<CompanyArticle> articleList = new ArrayList<>();
        for (int i = 0; i < linkElement.size(); i++) {
            List<String> datePressElement = List.of(nameDatePressElement.get(2 * i + 1)
                    .replaceAll("^\\(|\\)$", "").split(",\\s|-"));

            articleList.add(CompanyArticle.builder()
                    .name(nameDatePressElement.get(2 * i)).press(Press.valueOf(datePressElement.getLast()))
                    .date(LocalDate.of(Integer.parseInt(datePressElement.getFirst()),
                            Integer.parseInt(datePressElement.get(1)), Integer.parseInt(datePressElement.get(2))))
                    .importance(importance)
                    .subjectCompany(subjectCompany).link(linkElement.get(i)).build());
        }
        return articleList;
    }

    public String getNameDatePressString() {
        return String.valueOf(nameDatePressBuffer);
    }

    public String getLinkString() {
        return String.valueOf(linkBuffer);
    }

    public static CompanyArticleBufferSimpleBuilder builder() {
        return new CompanyArticleBufferSimpleBuilder();
    }

    private CompanyArticleBufferSimple(StringBuffer nameDatePressBuffer, StringBuffer linkBuffer, Integer importance, String subjectCompany) {
        this.nameDatePressBuffer = nameDatePressBuffer;
        this.linkBuffer = linkBuffer;
        this.importance = importance;
        this.subjectCompany = subjectCompany;
    }

    public static class CompanyArticleBufferSimpleBuilder {
        private StringBuffer nameDatePressBuffer;
        private StringBuffer linkBuffer;
        private Integer importance;
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
