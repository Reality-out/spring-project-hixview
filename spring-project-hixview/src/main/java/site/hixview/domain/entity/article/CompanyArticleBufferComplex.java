package site.hixview.domain.entity.article;

import site.hixview.domain.entity.Press;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;

public class CompanyArticleBufferComplex {

    private final StringBuffer nameDatePressBuffer;
    private final StringBuffer subjectCompanyBuffer;
    private final StringBuffer linkBuffer;
    private final StringBuffer importanceBuffer;

    private List<CompanyArticle> parsedArticles() {
        List<String> nameDatePressElement = List.of(this.nameDatePressBuffer.toString().split("\\R"));
        List<String> subjectCompanyElement = List.of(subjectCompanyBuffer.toString().split("\\R"));
        List<String> linkElement = List.of(linkBuffer.toString().split("\\R"));
        List<Integer> importanceElement = Stream.of(importanceBuffer.toString().split("\\R")).map(Integer::parseInt).toList();

        ArrayList<CompanyArticle> articleList = new ArrayList<>();
        for (int i = 0; i < linkElement.size(); i++) {
            List<String> datePressElement = List.of(nameDatePressElement.get(2 * i + 1)
                    .replaceAll("^\\(|\\)$", "").split(",\\s|-"));

            articleList.add(CompanyArticle.builder()
                    .name(nameDatePressElement.get(2 * i)).press(Press.valueOf(datePressElement.getLast()))
                    .subjectCompany(subjectCompanyElement.get(i)).link(linkElement.get(i))
                    .date(LocalDate.of(Integer.parseInt(datePressElement.getFirst()),
                            Integer.parseInt(datePressElement.get(1)), Integer.parseInt(datePressElement.get(2))))
                    .importance(importanceElement.get(i)).build());
        }
        return articleList;
    }

    public String getNameDatePressString() {
        return String.valueOf(nameDatePressBuffer);
    }

    public String getSubjectCompanyString() {
        return String.valueOf(subjectCompanyBuffer);
    }

    public String getLinkString() {
        return String.valueOf(linkBuffer);
    }

    public String getImportanceString() {
        return String.valueOf(importanceBuffer);
    }

    public static CompanyArticleBufferComplexBuilder builder() {
        return new CompanyArticleBufferComplexBuilder();
    }

    private CompanyArticleBufferComplex(StringBuffer nameDatePressBuffer, StringBuffer subjectCompanyBuffer, StringBuffer linkBuffer, StringBuffer importanceBuffer) {
        this.nameDatePressBuffer = nameDatePressBuffer;
        this.subjectCompanyBuffer = subjectCompanyBuffer;
        this.linkBuffer = linkBuffer;
        this.importanceBuffer = importanceBuffer;
    }

    public static final class CompanyArticleBufferComplexBuilder {
        private StringBuffer nameDatePressBuffer;
        private StringBuffer subjectCompanyBuffer;
        private StringBuffer linkBuffer;
        private StringBuffer importanceBuffer;

        public CompanyArticleBufferComplexBuilder() {}

        public CompanyArticleBufferComplexBuilder nameDatePressString(String nameDatePressString) {
            if (this.nameDatePressBuffer == null) {
                this.nameDatePressBuffer = new StringBuffer(nameDatePressString);
            } else {
                this.nameDatePressBuffer.append(lineSeparator()).append(nameDatePressString);
            }
            return this;
        }

        public CompanyArticleBufferComplexBuilder subjectCompanyString(String subjectCompanyString) {
            if (this.subjectCompanyBuffer == null) {
                this.subjectCompanyBuffer = new StringBuffer(subjectCompanyString);
            } else {
                this.subjectCompanyBuffer.append(lineSeparator()).append(subjectCompanyString);
            }
            return this;
        }

        public CompanyArticleBufferComplexBuilder linkString(String linkString) {
            if (this.linkBuffer == null) {
                this.linkBuffer = new StringBuffer(linkString);
            } else {
                this.linkBuffer.append(lineSeparator()).append(linkString);
            }
            return this;
        }

        public CompanyArticleBufferComplexBuilder importanceString(String importanceString) {
            if (this.importanceBuffer == null) {
                this.importanceBuffer = new StringBuffer(importanceString);
            } else {
                this.importanceBuffer.append(lineSeparator()).append(importanceString);
            }
            return this;
        }

        public CompanyArticleBufferComplexBuilder article(CompanyArticle article) {
            String concatenatedNameDatePress = article.getName() + lineSeparator() +
                    "(" + article.getDate().getYear() + "-" + article.getDate().getMonthValue() + "-" +
                    article.getDate().getDayOfMonth() + ", " + article.getPress() + ")";
            if (nameDatePressBuffer == null) {
                nameDatePressBuffer = new StringBuffer(concatenatedNameDatePress);
                subjectCompanyBuffer = new StringBuffer(article.getSubjectCompany());
                linkBuffer = new StringBuffer(article.getLink());
                importanceBuffer = new StringBuffer(article.getImportance());
            } else {
                nameDatePressBuffer.append(lineSeparator()).append(concatenatedNameDatePress);
                subjectCompanyBuffer.append(lineSeparator()).append(article.getSubjectCompany());
                linkBuffer.append(lineSeparator()).append(article.getLink());
                importanceBuffer.append(lineSeparator()).append(article.getImportance());
            }
            return this;
        }

        public CompanyArticleBufferComplexBuilder articles(CompanyArticle... articles) {
            for (CompanyArticle article : articles) {
                article(article);
            }
            return this;
        }

        public CompanyArticleBufferComplexBuilder articleDto(CompanyArticleDto articleDto) {
            String concatenatedNameDatePress = articleDto.getName() + lineSeparator() +
                    "(" + articleDto.getYear() + "-" + articleDto.getMonth() + "-" + articleDto.getDays() + ", " + articleDto.getPress() + ")";
            if (nameDatePressBuffer == null) {
                nameDatePressBuffer = new StringBuffer(concatenatedNameDatePress);
                subjectCompanyBuffer = new StringBuffer(articleDto.getSubjectCompany());
                linkBuffer = new StringBuffer(articleDto.getLink());
                importanceBuffer = new StringBuffer(articleDto.getImportance());
            } else {
                nameDatePressBuffer.append(lineSeparator()).append(concatenatedNameDatePress);
                subjectCompanyBuffer.append(lineSeparator()).append(articleDto.getSubjectCompany());
                linkBuffer.append(lineSeparator()).append(articleDto.getLink());
                importanceBuffer.append(lineSeparator()).append(articleDto.getImportance());
            }
            return this;
        }

        public CompanyArticleBufferComplexBuilder articleBuffer(CompanyArticleBufferComplex articleBuffer) {
            if (nameDatePressBuffer == null) {
                nameDatePressBuffer = new StringBuffer(articleBuffer.getNameDatePressString());
                subjectCompanyBuffer = new StringBuffer(articleBuffer.getSubjectCompanyString());
                linkBuffer = new StringBuffer(articleBuffer.getLinkString());
                importanceBuffer = new StringBuffer(articleBuffer.getImportanceString());
            } else {
                nameDatePressBuffer.append(lineSeparator()).append(articleBuffer.getNameDatePressString());
                subjectCompanyBuffer.append(lineSeparator()).append(articleBuffer.getSubjectCompanyString());
                linkBuffer.append(lineSeparator()).append(articleBuffer.getLinkString());
                importanceBuffer.append(lineSeparator()).append(articleBuffer.getImportanceString());
            }
            return this;
        }

        public CompanyArticleBufferComplex build() {
            return new CompanyArticleBufferComplex(this.nameDatePressBuffer, this.subjectCompanyBuffer, this.linkBuffer, this.importanceBuffer);
        }
    }
}
