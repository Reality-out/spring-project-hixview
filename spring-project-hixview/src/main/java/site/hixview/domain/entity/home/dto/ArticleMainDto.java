package site.hixview.domain.entity.home.dto;

import lombok.Getter;
import lombok.Setter;
import site.hixview.domain.validation.annotation.ClassificationConstraint;
import site.hixview.domain.validation.annotation.article.ArticleImagePath;
import site.hixview.domain.validation.annotation.article.ArticleName;
import site.hixview.domain.validation.annotation.article.ArticleSummary;

@Getter
@Setter
public class ArticleMainDto {

    @ArticleName
    private String name;

    @ArticleImagePath
    private String imagePath;

    @ArticleSummary
    private String summary;

    @ClassificationConstraint
    private String classification;
}
