package springsideproject1.springsideproject1build.domain.article;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.*;

@Getter
public class CompanyArticleStringBuffer {

    private final StringBuffer nameDatePressString;
    private final StringBuffer subjectCompanyString;
    private final StringBuffer linkString;
    private final StringBuffer importanceString;

    public List<List<String>> parsedNameDatePressString() {
        String parsedString = nameDatePressString.toString();
        List<String> dividedArticle = List.of(parsedString.split("\\R"));
        List<List<String>> returnArticle = new ArrayList<>();

        for (int i = 0; i < dividedArticle.size(); i++) {
            if (i % 2 == 0) {
                returnArticle.add(new ArrayList<>(List.of(dividedArticle.get(i))));
            } else {
                returnArticle.getLast().addAll(List.of(dividedArticle.get(i)
                        .replaceAll("^\\(|\\)$", "").split(",\\s|-")));
                if (returnArticle.getLast().size() != 5) {
                    returnArticle.remove(i);
                    break;
                }
            }
        }
        return returnArticle;
    }

    public List<String> parsedSubjectCompanyString() {
        return List.of(subjectCompanyString.toString().split("\\R"));
    }

    public List<String> parsedLinkString() {
        return List.of(linkString.toString().split("\\R"));
    }

    public List<String> parsedImportanceString() {
        return List.of(importanceString.toString().split("\\R"));
    }

    public static CompanyArticleStringBufferBuilder builder() {
        return new CompanyArticleStringBufferBuilder();
    }

    private CompanyArticleStringBuffer(StringBuffer nameDatePressString, StringBuffer subjectCompanyString, StringBuffer linkString, StringBuffer importanceString) {
        this.nameDatePressString = nameDatePressString;
        this.subjectCompanyString = subjectCompanyString;
        this.linkString = linkString;
        this.importanceString = importanceString;
    }

    public static class CompanyArticleStringBufferBuilder {
        private StringBuffer nameDatePressString;
        private StringBuffer subjectCompanyString;
        private StringBuffer linkString;
        private StringBuffer importanceString;

        public CompanyArticleStringBufferBuilder() {}

        public CompanyArticleStringBufferBuilder nameDatePressString(String nameDatePressString) {
            if (this.nameDatePressString == null) {
                this.nameDatePressString = new StringBuffer(nameDatePressString);
            } else {
                this.nameDatePressString.append(lineSeparator()).append(nameDatePressString);
            }
            return this;
        }

        public CompanyArticleStringBufferBuilder subjectCompanyString(String subjectCompanyString) {
            if (this.subjectCompanyString == null) {
                this.subjectCompanyString = new StringBuffer(subjectCompanyString);
            } else {
                this.subjectCompanyString.append(lineSeparator()).append(subjectCompanyString);
            }
            return this;
        }

        public CompanyArticleStringBufferBuilder linkString(String linkString) {
            if (this.linkString == null) {
                this.linkString = new StringBuffer(linkString);
            } else {
                this.linkString.append(lineSeparator()).append(linkString);
            }
            return this;
        }

        public CompanyArticleStringBufferBuilder importanceString(String importanceString) {
            if (this.importanceString == null) {
                this.importanceString = new StringBuffer(importanceString);
            } else {
                this.importanceString.append(lineSeparator()).append(importanceString);
            }
            return this;
        }

        public CompanyArticleStringBufferBuilder article(CompanyArticle article) {
            return articleDto(article.toDto());
        }

        public CompanyArticleStringBufferBuilder articleDto(CompanyArticleDto articleDto) {
            String concatenatedNameDatePress = articleDto.getName() + lineSeparator() +
                    "(" + articleDto.getYear() + "-" + articleDto.getMonth() + "-" + articleDto.getDate() + ", " + articleDto.getPress() + ")";
            if (nameDatePressString == null) {
                nameDatePressString = new StringBuffer(concatenatedNameDatePress);
                subjectCompanyString = new StringBuffer(articleDto.getSubjectCompany());
                linkString = new StringBuffer(articleDto.getLink());
                importanceString = new StringBuffer(articleDto.getImportance());
            } else {
                nameDatePressString.append(lineSeparator()).append(concatenatedNameDatePress);
                subjectCompanyString.append(lineSeparator()).append(articleDto.getSubjectCompany());
                linkString.append(lineSeparator()).append(articleDto.getLink());
                importanceString.append(lineSeparator()).append(articleDto.getImportance());
            }
            return this;
        }

        public CompanyArticleStringBuffer build() {
            return new CompanyArticleStringBuffer(this.nameDatePressString, this.subjectCompanyString, this.linkString, this.importanceString);
        }
    }
}
