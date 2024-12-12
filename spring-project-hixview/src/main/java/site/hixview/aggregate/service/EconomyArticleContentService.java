package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.service.supers.ServiceWithNumberIdentifier;

import java.util.List;

public interface EconomyArticleContentService extends ServiceWithNumberIdentifier<EconomyArticleContent> {
    List<EconomyArticleContent> getByEconomyArticle(EconomyArticle article);

    List<EconomyArticleContent> getByEconomyContent(EconomyContent economyContent);
}
