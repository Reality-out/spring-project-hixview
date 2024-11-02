package site.hixview.domain.entity.article.dto;

import lombok.Getter;
import lombok.Setter;
import site.hixview.domain.validation.annotation.ImportanceConstraint;
import site.hixview.domain.validation.annotation.PressConstraint;
import site.hixview.domain.validation.annotation.article.*;

@Getter
@Setter
public class CompanyArticleDto {
    @ArticleName
    private String name;

    @PressConstraint
    private String press;

    @ArticleLink
    private String link;

    @ArticleYear
    private Integer year;

    @ArticleMonth
    private Integer month;

    @ArticleDays
    private Integer days;

    @ImportanceConstraint
    private Integer importance;

    @ArticleSubjectCompany
    private String subjectCompany;
}
