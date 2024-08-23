package springsideproject1.springsideproject1build.domain.article;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.lineSeparator;

public class CompanyArticleBufferSimple {

    private final StringBuffer nameDatePressBuffer;
    private final StringBuffer linkBuffer;

    @Getter private final String subjectCompany;
    @Getter private final Integer importance;

    private List<CompanyArticle> parsedArticles() {
        List<String> nameDatePressElement = List.of(this.nameDatePressBuffer.toString().split("\\R"));
        List<String> linkElement = List.of(linkBuffer.toString().split("\\R"));

        ArrayList<CompanyArticle> articleList = new ArrayList<>();
        for (int i = 0; i < linkElement.size(); i++) {
            List<String> datePressElement = List.of(nameDatePressElement.get(2 * i + 1)
                    .replaceAll("^\\(|\\)$", "").split(",\\s|-"));

            articleList.add(CompanyArticle.builder()
                    .name(nameDatePressElement.get(2 * i)).press(Press.valueOf(datePressElement.getLast()))
                    .subjectCompany(subjectCompany).link(linkElement.get(i))
                    .date(LocalDate.of(Integer.parseInt(datePressElement.getFirst()),
                            Integer.parseInt(datePressElement.get(1)), Integer.parseInt(datePressElement.get(2))))
                    .importance(importance).build());
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

    private CompanyArticleBufferSimple(StringBuffer nameDatePressBuffer, String subjectCompany, StringBuffer linkBuffer, Integer importance) {
        this.nameDatePressBuffer = nameDatePressBuffer;
        this.linkBuffer = linkBuffer;
        this.subjectCompany = subjectCompany;
        this.importance = importance;
    }

    public static class CompanyArticleBufferSimpleBuilder {
        private StringBuffer nameDatePressBuffer;
        private StringBuffer linkBuffer;
        private String subjectCompany;
        private Integer importance;

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

        public CompanyArticleBufferSimpleBuilder subjectCompany(String subjectCompany) {
            this.subjectCompany = subjectCompany;
            return this;
        }

        public CompanyArticleBufferSimpleBuilder importance(Integer importance) {
            this.importance = importance;
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
            subjectCompany = article.getSubjectCompany();
            importance = article.getImportance();
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
                    "(" + articleDto.getYear() + "-" + articleDto.getMonth() + "-" + articleDto.getDate() + ", " + articleDto.getPress() + ")";
            if (nameDatePressBuffer == null) {
                nameDatePressBuffer = new StringBuffer(concatenatedNameDatePress);
                linkBuffer = new StringBuffer(articleDto.getLink());
            } else {
                nameDatePressBuffer.append(lineSeparator()).append(concatenatedNameDatePress);
                linkBuffer.append(lineSeparator()).append(articleDto.getLink());
            }
            subjectCompany = articleDto.getSubjectCompany();
            importance = articleDto.getImportance();
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
            subjectCompany = articleBuffer.getSubjectCompany();
            importance = articleBuffer.getImportance();
            return this;
        }

        public CompanyArticleBufferSimple build() {
            return new CompanyArticleBufferSimple(this.nameDatePressBuffer, this.subjectCompany, this.linkBuffer, this.importance);
        }
    }
}
